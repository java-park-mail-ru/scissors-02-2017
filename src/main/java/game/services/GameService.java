package game.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import game.objects.Bullet;
import game.snapshots.ClientSnap;
import game.objects.Coords;
import game.mechanic.GameSession;
import game.objects.Player;
import game.transport.websocket.Message;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class GameService {
    private Set<GameSession> gameSessions = new HashSet<>();

    private @NotNull RemotePointService remotePointService;

    private @NotNull ObjectMapper objectMapper = new ObjectMapper();

    private @NotNull ServerSnapshotService serverSnapshotService;

    private @NotNull ClientSnapshotService clientSnapshotService;

    private @NotNull MovementService movementService;

    private static final @NotNull Logger LOGGER = LoggerFactory.getLogger(GameService.class);


    public GameService(@NotNull RemotePointService remotePointService,
                       @NotNull ServerSnapshotService serverSnapshotService,
                       @NotNull ClientSnapshotService clientSnapshotService,
                       @NotNull MovementService movementService) {
        this.remotePointService = remotePointService;
        this.serverSnapshotService = serverSnapshotService;
        this.clientSnapshotService = clientSnapshotService;
        this.movementService = movementService;
    }

    public void startGame(List<Player> players) {
        final GameSession gameSession = new GameSession();
        //TODO:начальные позиции игроков
        gameSession.addPlayers(players);
        gameSessions.add(gameSession);

        final Message message = new Message();
        message.setType("StartGame");
        try {
            message.setContent(objectMapper.writeValueAsString(players));
            LOGGER.info("Message type: "+message.getType()+" content: " + message.getContent());
        } catch (JsonProcessingException e) {
            LOGGER.error("json format error: ", e);
        }
        for (Player player : players) {
            remotePointService.sendMessage(message, player.getUser());
        }
    }

    public void createAndSendMessages() {
        for (GameSession gameSession : gameSessions) {
            final Message message = serverSnapshotService.createSnapshot(gameSession);
            LOGGER.info("Message type: "+message.getType()+" content: " + message.getContent());
            final Set<Player> players = gameSession.getPlayers();
            for (Player player : players) {
                remotePointService.sendMessage(message, player.getUser());
            }
        }
    }

    public void processSnapshots() {
        for (GameSession gameSession : gameSessions) {
            processSnapshotsForSession(gameSession);
        }
    }

    public void processSnapshotsForSession(GameSession forGameSession) {
        final Set<Player> players = forGameSession.getPlayers();
        for (Player player : players) {
            final ClientSnap snap = clientSnapshotService.getLastClientSnaps(player.getUser());
            if (snap != null) {
                player.setDirection(snap.getDirection());
                player.setDesirablePosition(new Coords(snap.getX(), snap.getY()));
                LOGGER.info("Player {}, x {}, y {}", player.getUser(), snap.getX(), snap.getY());
                movementService.addObject(player);

                if (player.isFiring()) {
                    LOGGER.info("isFiring");
                    final String type = snap.getWeapon();
                    switch (type) {
                        case ("pistol"): {  }
                    }
                }

                final List<Bullet> snapBullets = snap.getBullets();
                if (!snapBullets.isEmpty()) {
                    for (Bullet snapBullet : snapBullets) {
                        final Bullet bullet = forGameSession.getBullet(snapBullet.getId());
                        bullet.setDesirablePosition(snapBullet.getPresentPosition());
                        movementService.addObject(bullet);
                    }
                }
            }
        }
        movementService.move();
        movementService.clear();
    }

    public void stopGameSessions() {
        for (GameSession gameSession : gameSessions) {
            if (gameSession.isGameOver()) {
                LOGGER.info("game {} is over", gameSession);
                final Message message = serverSnapshotService.gameIsOver(gameSession);
                final Set<Player> players = gameSession.getPlayers();
                for (Player player : players) {
                    remotePointService.sendMessage(message, player.getUser());
                }
                gameSessions.remove(gameSession);
            }
        }
    }

    public Set<GameSession> getGameSessions() {
        return gameSessions;
    }
}

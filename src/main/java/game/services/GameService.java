package game.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import game.objects.Bullet;
import game.resourses.ResourseFactory;
import game.snapshots.ClientSnap;
import game.objects.Coords;
import game.mechanic.GameSession;
import game.objects.Player;
import game.transport.websocket.Message;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GameService {
    private Set<GameSession> gameSessions = new HashSet<>();

    private @NotNull RemotePointService remotePointService;

    private @NotNull ObjectMapper objectMapper = new ObjectMapper();

    private @NotNull ServerSnapshotService serverSnapshotService;

    private @NotNull ClientSnapshotService clientSnapshotService;

    private @NotNull MovementService movementService;

    private @NotNull ResourseFactory resourseFactory;

    private static final @NotNull Logger LOGGER = LoggerFactory.getLogger(GameService.class);


    public GameService(@NotNull RemotePointService remotePointService,
                       @NotNull ServerSnapshotService serverSnapshotService,
                       @NotNull ClientSnapshotService clientSnapshotService,
                       @NotNull MovementService movementService,
                       @NotNull ResourseFactory resourseFactory) {
        this.remotePointService = remotePointService;
        this.serverSnapshotService = serverSnapshotService;
        this.clientSnapshotService = clientSnapshotService;
        this.movementService = movementService;
        this.resourseFactory = resourseFactory;
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
            LOGGER.info("Message type: " + message.getType() + " content: " + message.getContent());
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
            LOGGER.info("Message type: " + message.getType() + " content: " + message.getContent());
            final Set<Player> players = gameSession.getPlayers();
            for (Player player : players) {
                try {
                    remotePointService.sendMessage(message, player.getUser());
                } catch (RuntimeException ex) {
                    remotePointService.disconnect(player.getUser());
                    gameSession.stopGameFor(player);
                }
            }
        }
    }

    public void processSnapshots() {
        for (GameSession gameSession : gameSessions) {
            processSnapshotsForSession(gameSession);
        }
        clientSnapshotService.clear();
    }

    public void processSnapshotsForSession(GameSession forGameSession) {
        final Set<Player> players = forGameSession.getPlayers();
        for (Player player : players) {
            final List<ClientSnap> snaps = clientSnapshotService.getClientSnaps(player.getUser());
            if (snaps != null) {
                for (ClientSnap snap : snaps) {
                    player.setDirection(snap.getDirection());
                    player.setDesirablePosition(new Coords(snap.getX(), snap.getY()));
                    movementService.addObject(player);

                    if (snap.isFiring()) {
                        LOGGER.info("isFiring");
                        final String type = snap.getWeapon();
                        final String descriptor = "game/weapon/" + type + ".json";
                        final Bullet bullet = resourseFactory.get(descriptor, Bullet.class);
                        if (bullet != null) {
                            forGameSession.addBullet(player, bullet);
                            //начальные координаты снаряда:положение игрока
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
        }
        movementService.move();
        movementService.clear();
    }

    public void stopGameSessions(long gameTime) {
        for (GameSession gameSession : gameSessions) {
            if (gameSession.isGameOver(gameTime)) {
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

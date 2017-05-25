package game.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import game.objects.Bullet;
import game.objects.Coords;
import game.objects.Way;
import game.resourses.ResourseFactory;
import game.snapshots.ClientSnap;
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

    private long GAME_TIME;

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

    public void setGameTime(long time) {
        this.GAME_TIME = time;
    }

    public void startGame(List<Player> players) {
        final GameSession gameSession = new GameSession();
        //TODO:начальные позиции игроков
        int i = 0;
        final List<Coords> initPos=new ArrayList<>();
        initPos.add(new Coords(300,300));
        initPos.add(new Coords(500,300));
        for (Player player : players) {
            player.setPresentPosition(initPos.get(i));
            i=+1;
        }
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

    public void createAndSendMessages(GameSession gameSession) {
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

    public void processGameForSession(GameSession forGameSession, int serverTime) {
        if (forGameSession.isGameOver(GAME_TIME)) {
            stopGameSession(forGameSession);
            return;
        }
        final Set<Player> players = forGameSession.getPlayers();
        for (Player player : players) {
            final List<ClientSnap> snaps = clientSnapshotService.getClientSnaps(player.getUser());
            if (snaps != null) {
                for (ClientSnap snap : snaps) {
                    player.setDirection(snap.getDirection());
                    movement(snap.getWay(), player, snap.getFrameTime());

                    if (snap.isFiring()) {
                        LOGGER.info("isFiring");
                        final String type = snap.getWeapon();
                        final Bullet bullet = resourseFactory.get(type, Bullet.class);
                        if (bullet != null) {
                            bullet.setPresentPosition(player.getPresentPosition());
                            forGameSession.addBullet(player, bullet);
                        }
                    }
                }
            }
        }
        movementService.setGameSession(forGameSession);
        movementService.move(serverTime);
        movementService.clear();

        createAndSendMessages(forGameSession);
    }

    private void movement(Way way, Player player, int frameTime) {
        int dx = 0;
        int dy = 0;
        switch (way) {
            case RIGHT:
                dy = frameTime * player.getSpeed();
                break;
            case LEFT:
                dy = -frameTime * player.getSpeed();
                break;
            case UP:
                dx = -frameTime * player.getSpeed();
                break;
            case DOUW:
                dx = frameTime * player.getSpeed();
                break;
            case NONE:
                return;
        }
        player.addDesirableShift(dx, dy);
        movementService.addObject(player);
    }

    public void stopGameSession(GameSession gameSession) {
        LOGGER.info("game {} is over", gameSession);
        final Message message = serverSnapshotService.gameIsOver(gameSession);
        final Set<Player> players = gameSession.getPlayers();
        for (Player player : players) {
            remotePointService.sendMessage(message, player.getUser());
        }
        gameSessions.remove(gameSession);
    }

    public Set<GameSession> getGameSessions() {
        return gameSessions;
    }
}

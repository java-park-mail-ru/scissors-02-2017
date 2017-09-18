package game.mechanic;


import game.resourses.ResourseFactory;
import game.snapshots.ClientSnap;
import game.objects.Player;
import game.services.ClientSnapshotService;
import game.services.GameService;
import game.services.RemotePointService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class GameMechanic {
    private static final @NotNull Logger LOGGER = LoggerFactory.getLogger(GameMechanic.class);
    private @NotNull ClientSnapshotService clientSnapshotService;
    private @NotNull RemotePointService remotePointService;
    private @NotNull GameService gameService;
    private @NotNull ResourseFactory resourseFactory;

    private int NUMBER_PLAYERS;

    private @NotNull Set<Player> players = new HashSet<>();

    private @NotNull ConcurrentLinkedQueue<Player> waiters = new ConcurrentLinkedQueue<>();

    private @NotNull ConcurrentLinkedQueue<Runnable> tasks = new ConcurrentLinkedQueue<>();

    public GameMechanic(@NotNull ClientSnapshotService clientSnapshotService,
                        @NotNull RemotePointService remotePointService,
                        @NotNull GameService gameService,
                        @NotNull ResourseFactory resourseFactory) {
        this.clientSnapshotService = clientSnapshotService;
        this.remotePointService = remotePointService;
        this.gameService = gameService;
        this.resourseFactory = resourseFactory;
    }

    @PostConstruct
    private void setup() {
        final Deathmatch settings = resourseFactory.getFromFile("game/deathmatch.json", Deathmatch.class);
        NUMBER_PLAYERS = settings.getPlayers();
        gameService.setGameTime(settings.getGameTime());
    }

    public void addSnap(String user, ClientSnap clientSnap) {
        LOGGER.info("add snapshot {}", clientSnap);
        tasks.add(() -> clientSnapshotService.pushClientSnap(user, clientSnap));
    }

    public void addPlayer(Player player) {
        if (!players.contains(player)) {
            LOGGER.info("add waiter {}", player);
            waiters.add(player);
        }
    }

    public void runTasks() {
        while (!tasks.isEmpty()) {
            final Runnable nextTask = tasks.poll();
            if (nextTask != null) {
                try {
                    nextTask.run();
                } catch (RuntimeException ex) {
                    LOGGER.error("Can't handle game task", ex);
                }
            }
        }
    }

    public void gameStep(int serverTime) {
        runTasks();

        tryStartGames();

        final Set<GameSession> sessions = gameService.getGameSessions();
        for (GameSession session : sessions) {
            gameService.processGameForSession(session, serverTime);
        }
    }

    public void tryStartGames() {
        final List<Player> newSet = new ArrayList<>();
        while (!waiters.isEmpty()) {
            final Player candidate = waiters.poll();
            if (!remotePointService.isConnected(candidate.getUser())) {
                continue;
            }
            newSet.add(candidate);
            if (newSet.size() == NUMBER_PLAYERS) {
                gameService.startGame(newSet);
                players.addAll(newSet);
                newSet.clear();
            }
        }
        if (!newSet.isEmpty()) {
            waiters.addAll(newSet);
        }
    }


}

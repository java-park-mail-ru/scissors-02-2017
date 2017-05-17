package game.gamemechanic;

import game.mechanic.GameMechanic;
import game.mechanic.MechanicExecutor;
import game.mechanic.GameSession;
import game.objects.Coords;
import game.objects.Player;
import game.services.GameService;
import game.services.RemotePointService;
import game.snapshots.ClientSnap;
import org.eclipse.jetty.websocket.server.WebSocketServerFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@RunWith(SpringRunner.class)
public class GameMechanicTest {
    @SuppressWarnings("unused")
    @MockBean
    private RemotePointService remotePointService;

    @Autowired
    private GameMechanic gameMechanic;

    @Autowired
    private GameService gameService;

    @SuppressWarnings("unused")
    @MockBean
    private MechanicExecutor mechanicExecutor;

    @SuppressWarnings("unused")
    @MockBean
    private WebSocketServerFactory defaultHandshakeHandler;

    private Player player1 = new Player();
    private Player player2 = new Player();




    @Before
    public void setUp() {
        player1.setUser("player1");
        player2.setUser("player2");

    }

    @Test
    public void startGame() {
        gameMechanic.addPlayer(player1);
        gameMechanic.addPlayer(player2);
        when(remotePointService.isConnected(anyString())).thenReturn(true);
        gameMechanic.gameStep();

        final Set<GameSession> sessions = gameService.getGameSessions();
        assertFalse("сессия не добавлена", sessions.isEmpty());
    }

    @Test
    public void move() {
        startGame();

        gameMechanic.addSnap("player1", createMoveSnap());
        gameMechanic.gameStep();

        final Set<GameSession> sessions = gameService.getGameSessions();
        final Iterator<GameSession> iterator = sessions.iterator();
        final GameSession gameSession = iterator.next();

        final Set<Player> players = gameSession.getPlayers();
        for(Player player: players){
            if(player.equals(player1)){
                assertEquals(new Coords(100, 100), player.getPresentPosition());
            }
        }
    }

    private ClientSnap createMoveSnap(){
        final ClientSnap snap1 = new ClientSnap();
        snap1.setUser("player1");
        snap1.setDirection(0);
        snap1.setIsFiring(false);
        return snap1;
    }

    private ClientSnap createFiringSnap(){
        final ClientSnap snap1 = new ClientSnap();
        snap1.setUser("player1");
        snap1.setDirection(0);
        snap1.setIsFiring(true);
        snap1.setWeapon("pistol");
        return snap1;
    }
}

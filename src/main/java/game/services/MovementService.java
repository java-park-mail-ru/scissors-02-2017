package game.services;


import game.mechanic.GameSession;
import game.objects.Bullet;
import game.objects.Coords;
import game.objects.Player;
import org.springframework.stereotype.Service;


import java.util.HashSet;
import java.util.Set;

@Service
public class MovementService {

    private GameSession gameSession;

    private Set<Player> players = new HashSet<>();

    public void setGameSession(GameSession gameSession) {
        this.gameSession = gameSession;
    }

    public void addObject(Player player) {
        players.add(player);
    }

    public void move(int serverTime) {
        for (Player player : players) {
            int x = player.getPresentPosition().getX();
            int y = player.getPresentPosition().getY();
            for (Coords shift : player.getDesirableShift()) {
                x = x + shift.getX();
                y = y + shift.getY();
            }
            player.setPresentPosition(new Coords(x, y));
        }

        final Set<Bullet> bullets = gameSession.getBullets();
        for (Bullet bullet : bullets) {
            final double angle = bullet.getDirection();
            final int x = bullet.getPresentPosition().getX() + (int) (bullet.getSpeed() * Math.cos(angle))*serverTime;
            final int y = bullet.getPresentPosition().getY() + (int) (bullet.getSpeed() * Math.sin(angle))*serverTime;

            bullet.setPresentPosition(new Coords(x, y));

        }
    }

    public void clear() {
        gameSession = null;
        players.clear();
    }
}

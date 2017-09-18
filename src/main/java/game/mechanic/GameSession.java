package game.mechanic;

import game.objects.Player;
import game.objects.Bullet;

import java.util.*;

public class GameSession {

    private long time = System.currentTimeMillis();

    private Set<Player> players = new HashSet<>();

    private Map<Bullet, Player> bullets = new HashMap<>();
    private Map<Long, Bullet> idBullets = new HashMap<>();

    public void addPlayers(List<Player> users) {
        players.addAll(users);
    }

    public void addBullet(Player player, Bullet bul) {
        this.idBullets.putIfAbsent(bul.getId(), bul);
        this.bullets.putIfAbsent(bul, player);
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public boolean isGameOver(long gameTime) {
        return System.currentTimeMillis() - time > gameTime;
    }

    public Bullet getBullet(long id) {
        return this.idBullets.get(id);
    }

    public Set<Bullet> getBullets() {
        return bullets.keySet();
    }

    public Player getPlayer(Bullet bullet) {
        return bullets.get(bullet);
    }

    public void stopGameFor(Player player) {
        players.remove(player);
    }
}

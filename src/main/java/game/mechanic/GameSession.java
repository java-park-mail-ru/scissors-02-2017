package game.mechanic;

import game.objects.Player;
import game.objects.Bullet;

import java.util.*;

public class GameSession {
    //нужно наверное вынести отсюда
    private static final long GAME_TIME = 180000;

    private long time = System.currentTimeMillis();

    private Set<Player> players = new HashSet<>();

    private Map<Bullet, Player> bullets = new HashMap<>();
    private Map<Long, Bullet> idBullets = new HashMap<>();

    public void addPlayers(List<Player> users) {
        players.addAll(users);
    }

    public void addBullet(Player player,Bullet bul){
        this.idBullets.putIfAbsent(bul.getId(),bul);
        this.bullets.putIfAbsent(bul, player);
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public boolean isGameOver() {
        return System.currentTimeMillis() - time > GAME_TIME;
    }

    public Bullet getBullet(long id){
        return idBullets.get(id);
    }


}

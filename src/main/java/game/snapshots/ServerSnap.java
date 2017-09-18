package game.snapshots;


import game.objects.Bullet;
import game.objects.Player;

import java.util.List;
import java.util.Set;

public class ServerSnap {

    private Set<Player> players;
    private Set<Bullet> bullets;

    public void setBullets(Set<Bullet> bullets) {
        this.bullets = bullets;
    }

    public Set<Bullet> getBullets() {
        return bullets;
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }
}

package game.objects;


import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Player extends GameObject {
    private static final @NotNull Logger LOGGER = LoggerFactory.getLogger(Player.class);
    private String user;
    private int score;
    private int health;
    private boolean isFiring;

    public Player() {
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public String getUser() {
        return user;
    }

    public int getScore() {
        return score;
    }

    public int getHealth() {
        return health;
    }

    public boolean isFiring() {
        return isFiring;
    }

    public void setFiring(boolean firing) {
        isFiring = firing;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Player other = (Player) obj;
        if (!user.equals(other.getUser())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode(){
        return user.hashCode();
    }
}

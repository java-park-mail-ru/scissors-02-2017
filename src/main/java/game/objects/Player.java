package game.objects;


import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

public class Player extends GameObject {
    private String user;
    private long score;
    private int health;
    private boolean isFiring;
    private short speed;
    @JsonIgnore
    private List<Coords> desirableShift = new ArrayList<>();

    public Player() {
    }

    public List<Coords> getDesirableShift() {
        return desirableShift;
    }

    public void addDesirableShift(int dx, int dy) {
        this.desirableShift.add(new Coords(dx, dy));
    }

    public void clearShifts() {
        this.desirableShift.clear();
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public String getUser() {
        return user;
    }

    public long getScore() {
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

    public short getSpeed() {
        return speed;
    }

    public void setSpeed(short speed) {
        this.speed = speed;
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
    public int hashCode() {
        return user.hashCode();
    }
}

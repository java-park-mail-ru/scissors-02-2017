package game.snapshots;


import game.objects.Bullet;

import java.util.ArrayList;
import java.util.List;

public class ClientSnap {
    private String user;
    private int x;
    private int y;
    private double direction;
    private boolean isFiring;
    private String weapon;
    private long frameTime;
    private List<Bullet> bullets=new ArrayList<>();

    public ClientSnap(){}

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getUser() {
        return user;
    }

    public boolean isFiring() {
        return isFiring;
    }

    public void setFiring(boolean firing) {
        isFiring = firing;
    }

    public double getDirection() {
        return direction;
    }

    public void setDirection(double direction) {
        this.direction = direction;
    }

    public void setWeapon(String weapon) {
        this.weapon = weapon;
    }

    public String getWeapon() {
        return weapon;
    }

    public void setFrameTime(long frameTime) {
        this.frameTime = frameTime;
    }

    public long getFrameTime() {
        return frameTime;
    }

    public List<Bullet> getBullets() {
        return bullets;
    }

    public void setBullets(List<Bullet> bullets) {
        this.bullets = bullets;
    }
}

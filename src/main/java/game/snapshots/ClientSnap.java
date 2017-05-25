package game.snapshots;


import game.objects.Way;

public class ClientSnap {
    private String user;
    private Way way;
    private double direction;
    private boolean isFiring;
    private String weapon;
    private int frameTime;

    public ClientSnap() {
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public boolean isFiring() {
        return isFiring;
    }

    public void setIsFiring(boolean firing) {
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

    public void setFrameTime(int frameTime) {
        this.frameTime = frameTime;
    }

    public int getFrameTime() {
        return frameTime;
    }

    public void setWay(Way way) {
        this.way = way;
    }

    public Way getWay() {
        return way;
    }
}

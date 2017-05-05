package game.objects;


import java.util.concurrent.atomic.AtomicLong;

public class Bullet extends GameObject {
    private static final AtomicLong ATOMIC_ID = new AtomicLong(0);
    private long id;
    private String type;
    private short damage;
    private short speed;

    public Bullet() {
        id = ATOMIC_ID.getAndIncrement();
    }

    public long getId() {
        return id;
    }

    public int getSpeed() {
        return speed;
    }

    public int getDamage() {
        return damage;
    }

    public void setSpeed(short speed) {
        this.speed = speed;
    }

    public void setDamage(short damage) {
        this.damage = damage;
    }

    public void setType(String type) {
        this.type = type;
    }
}

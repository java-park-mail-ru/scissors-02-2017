package game.objects;


import com.fasterxml.jackson.annotation.JsonIgnore;
import game.resourses.ResourseBullet;

import java.util.concurrent.atomic.AtomicLong;

public class Bullet extends GameObject {
    private static final AtomicLong ATOMIC_ID = new AtomicLong(0);
    private long id;
    private String type;
    @JsonIgnore
    private short damage;
    @JsonIgnore
    private short speed;

    public Bullet() {
        id = ATOMIC_ID.getAndIncrement();
    }

    public long getId() {
        return id;
    }

    public short getSpeed() {
        return speed;
    }

    public short getDamage() {
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

    public String getType() {
        return type;
    }

}

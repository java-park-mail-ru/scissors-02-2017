package game.resourses;

public class ResourseBullet {
    private String type;
    private short damage;
    private short speed;

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

    public short getDamage() {
        return damage;
    }

    public short getSpeed() {
        return speed;
    }
}

package game.objects;



public class Player extends GameObject {
    private String user;
    private long score;
    private int health;
    private boolean isFiring;

    public Player() {
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

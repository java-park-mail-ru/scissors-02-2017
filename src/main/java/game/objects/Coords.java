package game.objects;


public class Coords {
    private int x;
    private  int y;

    public Coords(int x, int y){
        this.x=x;
        this.y=y;
    }
    public Coords(){}

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Coords other = (Coords) obj;
        if (x!=other.getX()) {
            return false;
        }
        if (y!=other.getY()) {
            return false;
        }
        return true;
    }
}

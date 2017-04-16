package game.models;


public class Player {
    private int x;
    private  int y;
    private String name;
    private int score;

    Player(int x, int y, String name, int score){
        this.x=x;
        this.y=y;
        this.name=name;
        this.score=score;
    }
}

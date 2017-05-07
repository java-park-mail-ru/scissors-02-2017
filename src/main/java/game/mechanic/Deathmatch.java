package game.mechanic;


public class Deathmatch {
    private int players;
    private long gameTime;

    public void setPlayers(int players) {
        this.players = players;
    }

    public int getPlayers() {
        return players;
    }

    public long getGameTime() {
        return gameTime;
    }

    public void setGameTime(long gameTime) {
        this.gameTime = gameTime;
    }
}

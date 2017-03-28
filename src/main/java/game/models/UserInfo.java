package game.models;

public class UserInfo {
    private String login;
    private long score;

    public UserInfo() {
    }

    public UserInfo(String login, long score) {
        this.login = login;
        this.score = score;
    }

    public String getLogin() {
        return login;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}

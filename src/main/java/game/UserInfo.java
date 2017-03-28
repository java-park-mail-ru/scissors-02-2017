package game;

public class UserInfo {
    private final String login;
    private final long score;

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
}

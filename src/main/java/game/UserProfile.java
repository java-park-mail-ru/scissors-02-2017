package game;

public class UserProfile {
    private final String login;
    private final String email;
    private String password;
    private final long id;
    private long score;


    public UserProfile(String login, String email, String password, long id) {
        this.login = login;
        this.email = email;
        this.password = password;
        this.id = id;
        this.score = 0;
    }

    public String getPassword() {
        return this.password;
    }

    public String getEmail() {
        return this.email;
    }

    public String getLogin() {
        return this.login;
    }

    public long getId() {
        return this.id;
    }

    public long getScore() {
        return this.score;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public UserInfo toInfo() {
        return new UserInfo(login, score);
    }

}

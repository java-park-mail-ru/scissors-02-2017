package game.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserAuth {
    private String login;
    private int score;
    private String password;
    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public int getScore() {
        return score;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

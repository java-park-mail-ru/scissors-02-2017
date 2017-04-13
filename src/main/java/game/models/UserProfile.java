package game.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserProfile {
    private String login;
    private String email;
    private String password;
    private long score;

    public UserProfile() {
    }

    @JsonCreator
    public UserProfile(@JsonProperty("login") String login,
                       @JsonProperty("email") String email,
                       @JsonProperty("password") String password) {
        this.login = login;
        this.email = email;
        this.password = password;
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

   public long getScore() {
        return this.score;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLogin(String login) {
        this.login = login;
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final UserProfile other = (UserProfile) obj;
        if (!login.equals(other.getLogin()))
            return false;
        if (score != other.getScore())
            return false;
        if(password!=null&&!password.equals(other.getPassword()))
            return false;
        return true;
    }

}

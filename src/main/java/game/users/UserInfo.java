package game.users;

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final UserInfo other = (UserInfo) obj;
        if (!login.equals(other.getLogin()))
            return false;
        if (score != other.getScore())
            return false;
        return true;
    }
}

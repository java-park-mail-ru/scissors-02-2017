package game;

public class UserProfile {
    private String email;
    private String login;
    private String password;
    private int id;
    private int score;

    public UserProfile(String email, String login, String password, int id){
        this.email = email;
        this.login = login;
        this.password = password;
        this.id = id;
        this.score = 0;
    }

    public String getPassword(){
        return this.password;
    }
    public String getEmail(){
        return this.email;
    }
    public String getLogin(){
        return this.login;
    }
    public int getId(){
        return this.id;
    }
    public int getScore(){
        return this.score;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public void setId(int id){
        this.id = id;
    }
    public void setScore(int score){
        this.score += score;
    }
    public String getDataJson(){
        return "{\"login\" :" + '"' + login + "\", \"email\" :" + '"' + email + "\"}";
    }
}

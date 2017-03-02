package game;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.json.JSONObject;

public class UserProfile {
    private final String login;
    private final String email;
    private String password;
    private final long id;
    private long score;
    private JSONObject json = new JSONObject();

    @JsonCreator
    public UserProfile(@JsonProperty("login") String login,
                       @JsonProperty("email")  String email,
                       @JsonProperty("password")  String password,
                       long id) {
        this.login = login;
        this.email = email;
        this.password = password;
        this.id = id;
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
    public long getId(){
        return this.id;
    }
    public long getScore(){
        return this.score;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public void setScore(long score){
        this.score = score;
    }

    public String getDataJson(){
        json.put("login", login);
        json.put("email", email);
        return json.toString();
    }

}

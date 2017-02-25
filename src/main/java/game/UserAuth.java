package game;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserAuth {
    private String login;
    private String email;
    private String password;

    @JsonCreator
    public UserAuth(@JsonProperty("login") String login, @JsonProperty("email") String email, @JsonProperty("password") String password){
        this.login = login;
        this.email = email;
        this.password = password;
    }

    public String getLogin(){
        return login;
    }
    public String getEmail(){
        return email;
    }
    public String getPassword(){
        return password;
    }

}

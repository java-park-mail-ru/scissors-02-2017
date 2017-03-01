package src;


public class UserProfile {
    private final String login;
    private final String email;
    private String password;
    private final long id;



    public UserProfile(String login, String email, String password, long id) {
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

    public void setPassword(String password){
         this.password = password;
    }
    public String getDataJson(){
        return "{\"login\" :" + '"' + login + "\", \"email\" :" + '"' + email + "\"}";
    }

}

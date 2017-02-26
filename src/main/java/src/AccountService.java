package src;

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;


public class AccountService {
    private Map<String, UserProfile> users = new HashMap<>();
    private Map<String, String>  sessions = new HashMap<>();

    private static final AtomicLong ID_GENERATOR = new AtomicLong(0);

    public boolean addUser(String login, String email, String password){
        if(checkUser(login)){
            return false;
        }

        final UserProfile user = new UserProfile(login, email, password,ID_GENERATOR.getAndIncrement());
        users.put(login,user);
        return true;
    }

    public boolean checkUser(String login){
        return users.containsKey(login);
    }
    public UserProfile getUser(String login){
        return users.get(login);
    }
    public void changeUserData(String login, UserAuth newData){
        final UserProfile user = users.get(login);
        final String newEmail = newData.getEmail();
        final String newPassword = newData.getPassword();
        if(!StringUtils.isEmpty(newEmail)){
            user.setEmail(newEmail);
        }
        if(!StringUtils.isEmpty(newPassword)){
            user.setPassword(newPassword);
        }
    }

    public UserProfile getUser(HttpSession httpSession){
        final String login = (String)httpSession.getAttribute("login");
        //final String login = sessions.get(sessionId);
        return users.get(login);
    }
    public void addSession(HttpSession httpSession, String login){
        httpSession.setAttribute("login", login);
        //final String sessionId = httpSession.getId();
        //sessions.put(sessionId,login);
    }
    public boolean checkSession(HttpSession httpSession){
        if(httpSession.getAttribute("login")==null){
            return false;
        }
        return true;
    }
    public void removeSession(HttpSession httpSession){
        httpSession.setAttribute("login", null);
        //final String sessionId = httpSession.getId();
        //sessions.remove(sessionId);
    }

}

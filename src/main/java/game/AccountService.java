package game;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Service
public class AccountService {
    private int countUser = 0;
    private Map<String, UserProfile> users = new HashMap<>();

    public boolean addUser(String login, String email, String password){
        if(checkUser(login)){
            return false;
        }
        final UserProfile user = new UserProfile(login, email, password, countUser);
        users.put(login, user);
        countUser++;
        return true;
    }
    public boolean checkUser(String login){
        return users.containsKey(login);
    }
    public UserProfile getUser(String login){
        return users.get(login);
    }
    public void changeUserData(String login, UserAuth newData){
        UserProfile user = users.get(login);
        String newEmail = newData.getEmail();
        String newPassword = newData.getPassword();
        if(!StringUtils.isEmpty(newEmail)){
            user.setEmail(newEmail);
        }
        if(!StringUtils.isEmpty(newPassword)){
            user.setPassword(newPassword);
        }
    }
    public UserProfile getUser(HttpSession httpSession){
        final String login = (String)httpSession.getAttribute("login");
        return users.get(login);
    }
    public void addSession(HttpSession httpSession, String login){
        httpSession.setAttribute("login", login);
    }
    public boolean checkSession(HttpSession httpSession){
        if(httpSession.getAttribute("login")==null){
            return false;
        }
        return true;
    }
    public void removeSession(HttpSession httpSession){
        httpSession.setAttribute("login", null);
    }
}

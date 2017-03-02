package game;

import org.springframework.util.StringUtils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;


public class AccountService {
    private ConcurrentHashMap<String, UserProfile> users = new ConcurrentHashMap<>();

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

    public void changePassword(String login, UserAuth newData){
        final UserProfile user = users.get(login);
        final String newPassword = newData.getPassword();
        if(!StringUtils.isEmpty(newPassword)){
            user.setPassword(newPassword);
        }
    }
}
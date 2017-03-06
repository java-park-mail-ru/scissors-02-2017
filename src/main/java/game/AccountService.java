package game;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class AccountService {
    private ConcurrentHashMap<String, UserProfile> users = new ConcurrentHashMap<>();

    private static final AtomicLong ID_GENERATOR = new AtomicLong(0);

    public UserProfile putIfAbsent(UserAuth body) {
        final String login = body.getLogin();
        final String email = body.getEmail();
        final String password = body.getPassword();
        if (!users.containsKey(login)) {
            final UserProfile user = new UserProfile(login, email, password, ID_GENERATOR.getAndIncrement());
            users.put(login, user);
            return user;
        }
        return null;
    }
    public UserProfile getUser(String login) {
        return users.get(login);
    }

    public void changePassword(String login, UserAuth newData) {
        final UserProfile user = users.get(login);
        final String newPassword = newData.getPassword();
        if (!StringUtils.isEmpty(newPassword)) {
            user.setPassword(newPassword);
        }
    }
}
package game;

import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class AccountService {
    private ConcurrentHashMap<String, UserProfile> users = new ConcurrentHashMap<>();

    private static final AtomicLong ID_GENERATOR = new AtomicLong(0);

    @Nullable
    public UserProfile addUser(UserAuth body) {
        final String login = body.getLogin();
        final String email = body.getEmail();
        final String password = body.getPassword();
        final UserProfile user = new UserProfile(login, email, password, ID_GENERATOR.getAndIncrement());
        if (users.putIfAbsent(login, user) != null) {
            return null;
        }
        return user;
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

    public ArrayList<UserInfo> getRating() {
        final ArrayList<UserProfile> allUsers = new ArrayList<>();
        allUsers.addAll(users.values());
        final ArrayList<UserInfo> rating = new ArrayList<>();
        for (int i = 0; i < allUsers.size() && i < 10; i++) {
            rating.add(allUsers.get(i).toInfo());
        }
        return rating;
    }
}
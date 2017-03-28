package game.services;


import game.models.UserAuth;
import game.models.UserInfo;
import game.models.UserProfile;
import org.jetbrains.annotations.Nullable;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class AccountService {

    private final JdbcTemplate template;

    public AccountService(JdbcTemplate tem) {
        this.template = tem;
    }

    private ConcurrentHashMap<String, UserProfile> users = new ConcurrentHashMap<>();
    private static final AtomicLong ID_GENERATOR = new AtomicLong(0);


    @Nullable
    public UserInfo addUser(UserProfile body) {
        final String login = body.getLogin();
        final String email = body.getEmail();
        final String password = body.getPassword();
        try {
            return template.queryForObject(
                    "insert into users (login, email, password) values(?,?,?) returning login,score",
                    new Object[]{login, email, password},
                    AccountService::userInfo);
        } catch (DuplicateKeyException ex) {
            return null;
        }
    }

    @Nullable
    public UserProfile getUser(String login) {
        try {
            return template.queryForObject(
                    "select login,score,password from users where lower(login)=lower(?)",
                    new Object[]{login},
                    AccountService::userAuth);
        } catch (DataAccessException ex) {
            return null;
        }

    }

    public boolean changePassword(String login, UserAuth newData) {
        final String newPassword = newData.getPassword();
        if (!StringUtils.isEmpty(newPassword)) {
            try {
                template.update(
                        "update users set password = ? where lower(login)=lower(?)",
                        new Object[]{newPassword, login});
                return true;
            } catch (DataAccessException ex) {
                return false;
            }
        }
        return false;
    }

    public List<UserInfo> getRating() {
        try {
            return template.query("select * from users order by score desc limit 10",
                    AccountService::userInfo);
        } catch (DataAccessException ex) {
            return new ArrayList<UserInfo>();
        }
    }
    public static UserInfo userInfo(ResultSet rs, int rowNum) throws SQLException {
        final UserInfo user = new UserInfo();
        user.setLogin(rs.getString("login"));
        user.setScore(rs.getInt("score"));
        return user;
    }
    public static UserProfile userAuth(ResultSet rs, int rowNum) throws SQLException {
        final UserProfile user = new UserProfile();
        user.setLogin(rs.getString("login"));
        user.setScore(rs.getInt("score"));
        user.setPassword(rs.getString("password"));
        return user;
    }
    public void clear(){
        template.update("truncate table users");
    }


}
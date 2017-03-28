package game.services;


import game.models.UserInfo;
import game.models.UserProfile;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AccountService {

    private final JdbcTemplate template;

    public AccountService(JdbcTemplate tem) {
        this.template = tem;
    }

    @Nullable
    public UserInfo addUser(UserProfile body) {
        final String login = body.getLogin();
        final String email = body.getEmail();
        final String password = body.getPassword();
        try {
            return template.queryForObject(
                    "INSERT INTO users (login, email, password) VALUES(?,?,?) RETURNING login,score",
                    new Object[]{login, email, passwordEncoder().encode(password)},
                    AccountService::userInfo);
        } catch (DuplicateKeyException ex) {
            return null;
        }
    }

    @Nullable
    public UserInfo getUser(String login) {
        try {
            return template.queryForObject(
                    "SELECT login,score FROM users WHERE lower(login)=lower(?)",
                    new Object[]{login},
                    AccountService::userInfo);
        } catch (DataAccessException ex) {
            return null;
        }
    }

    @Nullable
    public UserInfo auth(UserProfile body) {
        final String login = body.getLogin();
        final String password = body.getPassword();
        try {
            final UserProfile user = template.queryForObject(
                    "SELECT login,score,password FROM users WHERE lower(login)=lower(?)",
                    new Object[]{login},
                    AccountService::userAuth);
            if (passwordEncoder().matches(password, user.getPassword())) {
                return user.toInfo();
            }
            return null;
        } catch (DataAccessException ex) {
            return null;
        }
    }

    public void changePassword(String login, UserProfile newData) {
        final String newPassword = newData.getPassword();
        if (!StringUtils.isEmpty(newPassword)) {
            template.update(
                    "UPDATE users SET password = ? WHERE lower(login)=lower(?)",
                    new Object[]{newPassword, login});
        }
    }

    public List<UserInfo> getRating() {
        try {
            return template.query("SELECT * FROM users ORDER BY score DESC LIMIT 10",
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

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public void clear() {
        template.update("TRUNCATE TABLE users");
    }


}
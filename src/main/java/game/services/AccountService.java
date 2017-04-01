package game.services;


import game.models.UserInfo;
import game.models.UserProfile;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AccountService {
    @Autowired
    PasswordEncoder passwordEncoder;

    private final JdbcTemplate template;

    static final Logger LOG = LoggerFactory.getLogger(AccountService.class);

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
                    AccountService::userInfo, login, email, passwordEncoder.encode(password));
        } catch (DuplicateKeyException ex) {
            LOG.info("User {} already exists …", login);
            return null;
        } catch (DataAccessException ex) {
            LOG.warn("DB Exception: ", ex);
            return null;
        }
    }

    @Nullable
    public UserInfo getUser(String login) {
        try {
            return template.queryForObject(
                    "SELECT login, score FROM users WHERE lower(login)=lower(?)",
                    AccountService::userInfo, login);
        } catch (EmptyResultDataAccessException ex) {
            LOG.info("User {} not found", login);
            return null;
        } catch (DataAccessException ex) {
            LOG.warn("DB Exception: ", ex);
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
                    AccountService::userAuth, login);
            if (passwordEncoder.matches(password, user.getPassword())) {
                return user.toInfo();
            }
            return null;
        } catch (EmptyResultDataAccessException ex) {
            LOG.info("User {} not found", login);
            return null;
        } catch (DataAccessException ex) {
            LOG.warn("DB Exception: ", ex);
            return null;
        }
    }

    public void changePassword(String login, UserProfile newData) {
        final String newPassword = newData.getPassword();
        try {
            if (!StringUtils.isEmpty(newPassword)) {
                template.update(
                        "UPDATE users SET password = ? WHERE lower(login)=lower(?)",
                        newPassword, login);
            }
        } catch (DataAccessException ex) {
            LOG.warn("DB Exception: ", ex);
        }
    }

    public List<UserInfo> getRating() {
        try {
            return template.query("SELECT * FROM users ORDER BY score DESC LIMIT 10",
                    AccountService::userInfo);
        } catch (EmptyResultDataAccessException ex) {
            LOG.warn("DB returned empty rating ");
            return new ArrayList<UserInfo>();
        } catch (DataAccessException ex) {
            LOG.warn("DB Exception: ", ex);
            return null;
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


    public void clear() {
        template.update("TRUNCATE TABLE users");
        LOG.warn("Table USERS was cleared");
    }


}
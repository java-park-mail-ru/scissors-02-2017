package game.controllers;


import game.response.ResponseError;
import game.models.UserInfo;
import game.models.UserProfile;
import game.services.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;


@CrossOrigin(maxAge = 84600)
@RestController
public class  UserController {

    private final AccountService accountService;
    private static final String KEY = "login";
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);


    public UserController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping(path = "/api/signup")
    public ResponseEntity<?> signup(@RequestBody UserProfile body, HttpSession httpSession) {
        final String login = body.getLogin();
        final String email = body.getEmail();
        final String password = body.getPassword();

        if (StringUtils.isEmpty(login)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseError.EMPTY_LOGIN);
        }
        if (StringUtils.isEmpty(password)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseError.EMPTY_PASSWORD);
        }
        if (StringUtils.isEmpty(email)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseError.EMPTY_EMAIL);
        }

        final UserInfo newUser = accountService.addUser(body);

        if (newUser != null) {
            LOG.info("add user: {}", login);
            httpSession.setAttribute(KEY, login);
            return ResponseEntity.status(HttpStatus.OK).body(newUser);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseError.ERROR_LOGIN);
        }
    }

    @PostMapping(path = "/api/session")
    public ResponseEntity<?> login(@RequestBody UserProfile body, HttpSession httpSession) {
        final String login = body.getLogin();
        final String password = body.getPassword();

        if (StringUtils.isEmpty(login)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseError.EMPTY_LOGIN);
        }
        if (StringUtils.isEmpty(password)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseError.EMPTY_PASSWORD);
        }

        final UserInfo user = accountService.auth(body);

        if (user != null) {
            LOG.info("user {} login", login);
            httpSession.setAttribute(KEY,login);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }
        LOG.info("user {} tried to login. Incorrect login/password", login);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseError.ERROR_SIGNUP);
}

    @DeleteMapping(path = "/api/session")
    public ResponseEntity<?> logout(HttpSession httpSession) {
        if (httpSession.getAttribute(KEY) != null) {
            LOG.info("user {} logout", httpSession.getAttribute(KEY));
            httpSession.setAttribute(KEY, null);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseError.ERROR_SESSION);
        }
    }

    @GetMapping(path = "/api/session")
    public ResponseEntity<?> userOfCurrentSession(HttpSession httpSession) {
        if (httpSession.getAttribute(KEY) == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseError.ERROR_AUTH);
        }

        final String login = (String) httpSession.getAttribute(KEY);
        final UserInfo user = accountService.getUser(login);

        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @GetMapping(path = "/api/user/{login}")
    public ResponseEntity<?> getUserData(@PathVariable(value = "login") String login, HttpSession httpSession) {
        if (httpSession.getAttribute(KEY) == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseError.ERROR_AUTH);
        }

        final UserInfo user = accountService.getUser(login);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseError.ERROR_USER);
        }
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PostMapping(path = "/api/user/{login}")
    public ResponseEntity<?> changePassword(@PathVariable(value = "login") String login,
                                            @RequestBody UserProfile body, HttpSession httpSession) {
        final String attrib = (String) httpSession.getAttribute(KEY);

        if (attrib == null || !attrib.equals(login)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseError.ERROR_AUTH);
        }

        accountService.changePassword(login,body);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping(path = "/api/rating")
    public ResponseEntity<?> rating() {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.getRating());
    }

}

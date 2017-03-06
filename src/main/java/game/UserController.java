package game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@CrossOrigin
@RestController
public class UserController {
    private final AccountService accountService;
    private static final String KEY = "login";

    @Autowired
    public UserController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping(path = "/api/signup")
    public ResponseEntity<?> signup(@RequestBody UserAuth body, HttpSession httpSession) {
        final String login = body.getLogin();
        final String email = body.getEmail();
        final String password = body.getPassword();

        if (StringUtils.isEmpty(login)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Error.EMPTY_LOGIN);
        }
        if (StringUtils.isEmpty(password)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(Error.EMPTY_PASSWORD));
        }
        if (StringUtils.isEmpty(email)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(Error.EMPTY_EMAIL));
        }
        final UserProfile newUser = accountService.putIfAbsent(body);
        if (newUser != null) {
            httpSession.setAttribute(KEY, login);
            return ResponseEntity.status(HttpStatus.OK).body(newUser.toInfo());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(Error.ERROR_LOGIN));
        }
    }

    @PostMapping(path = "/api/session")
    public ResponseEntity<?> login(@RequestBody UserAuth body, HttpSession httpSession) {
        final String login = body.getLogin();
        final String password = body.getPassword();
        if (StringUtils.isEmpty(login)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(Error.EMPTY_LOGIN));
        }
        if (StringUtils.isEmpty(password)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(Error.EMPTY_PASSWORD));
        }
        final UserProfile user = accountService.getUser(login);
        if (user != null) {
            if (user.getPassword().equals(password)) {
                httpSession.setAttribute(KEY, login);
                return ResponseEntity.status(HttpStatus.OK).body(user.toInfo());
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(Error.ERROR_SIGNUP));
    }

    @DeleteMapping(path = "/api/session")
    public ResponseEntity<?> logout(HttpSession httpSession) {
        if (httpSession.getAttribute(KEY) != null) {
            httpSession.setAttribute(KEY, null);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(Error.ERROR_SESSION));
        }
    }

    @GetMapping(path = "/api/session")
    public ResponseEntity<?> userOfCurrentSession(HttpSession httpSession) {
        if (httpSession.getAttribute(KEY) == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(Error.ERROR_AUTH));
        }
        final String login = (String) httpSession.getAttribute(KEY);
        final UserProfile user = accountService.getUser(login);
        return ResponseEntity.status(HttpStatus.OK).body(user.toInfo());
    }

    @GetMapping(path = "/api/user/{login}")
    public ResponseEntity<?> getUserData(@PathVariable(value = "login") String login, HttpSession httpSession) {
        if (httpSession.getAttribute(KEY) == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Error(Error.ERROR_AUTH));
        }
        final UserProfile user = accountService.getUser(login);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error(Error.ERROR_USER));
        }
        return ResponseEntity.status(HttpStatus.OK).body(user.toInfo());
    }

    @PostMapping(path = "/api/user/{login}")
    public ResponseEntity<?> changePassword(@PathVariable(value = "login") String login, @RequestBody UserAuth body, HttpSession httpSession) {
        final String attrib = (String) httpSession.getAttribute(KEY);
        if (attrib == null || attrib != login) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Error(Error.ERROR_AUTH));
        }
        accountService.changePassword(login, body);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

}

package game;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
public class UserController {

    @NotNull
    private final AccountService accountService;

    public UserController(@NotNull AccountService accountService){
        this.accountService = accountService;
    }

    @RequestMapping(path = "/", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponseEntity<String> registration(@RequestBody UserAuth body, HttpSession httpSession){
        final String login = body.getLogin();
        final String email = body.getEmail();
        final String password = body.getPassword();

        if(StringUtils.isEmpty(login)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\" : \"login is empty\"}");
        }
        if(StringUtils.isEmpty(password)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\" : \"password is empty\"}");
        }
        if(StringUtils.isEmpty(email)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\" : \"email is empty\"}");
        }

        if(accountService.addUser(login, email, password)){
            accountService.addSession(httpSession, login);
            return ResponseEntity.status(HttpStatus.OK).body("{\"login\" : "+ '"' +login+"\"}");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\" : "+ '"' +login+"\"is already exists}");
    }

    @RequestMapping(path = "/api/session", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponseEntity<String> login(@RequestBody UserAuth body, HttpSession httpSession){
        final String login = body.getLogin();
        final String password = body.getPassword();
        if(StringUtils.isEmpty(login) || StringUtils.isEmpty(password)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\" : \"login or password is empty\"}");
        }
        if(accountService.checkUser(login)){
            if(accountService.getUser(login).getPassword().equals(password)){
                accountService.addSession(httpSession, login);
                return ResponseEntity.status(HttpStatus.OK).body("{\"login\" : "+ '"' +login+"\"}");
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\" : \"wrong login or password\"}");
    }

    @RequestMapping(path="/api/session", method = RequestMethod.DELETE, produces = "application/json", consumes = "application/json")
    public ResponseEntity<String> logout(HttpSession httpSession) {
        if(accountService.checkSession(httpSession)){
            accountService.removeSession(httpSession);
            return ResponseEntity.status(HttpStatus.OK).body("{}");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\" : \"session is not found\"}");
    }

    @RequestMapping(path="/api/session", method = RequestMethod.GET, produces = "application/json", consumes = "application/json")
    public ResponseEntity<String> userOfCurrentSession(HttpSession httpSession){
        if(!accountService.checkSession(httpSession)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\" : \"session is not found\"}");
        }
        final UserProfile user = accountService.getUser(httpSession);
        return ResponseEntity.status(HttpStatus.OK).body("{\"login\" : "+ '"' +user.getLogin()+"\"}");
    }

    @RequestMapping(path="/api/user/{login}", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponseEntity<String> changeUserData(@PathVariable(value = "login") String login, @RequestBody UserAuth body, HttpSession httpSession){
        if(!accountService.checkUser(login)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\" : \"user is not exists\"}");
        }
        if(!accountService.checkSession(httpSession)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"error\" : \"user is not authorized\"}");
        }
        accountService.changeUserData(login, body);
        return ResponseEntity.status(HttpStatus.OK).body("{}");
    }

    @RequestMapping(path="/api/user/{login}", method = RequestMethod.GET, produces = "application/json", consumes = "application/json")
    public ResponseEntity<String> getUserData (@PathVariable(value = "login") String login, HttpSession httpSession){
        if(!accountService.checkUser(login)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\" : \"user is not exists\"}");
        }
        if(!accountService.checkSession(httpSession)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"error\" : \"user is not authorized\"}");
        }
        final UserProfile user = accountService.getUser(login);
        return ResponseEntity.status(HttpStatus.OK).body(user.getDataJson());
    }
}

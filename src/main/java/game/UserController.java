package game;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class UserController {
    private AccountService accountService = new AccountService();
    private static final String KEY = "login";

    @PostMapping(path = "/api/signup")
    public ResponseEntity<String> signup(@RequestBody UserAuth body, HttpSession httpSession){
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
            httpSession.setAttribute("login", login);
            /*ObjectMapper objectMapper = new ObjectMapper();
            String resp = objectMapper.writeValueAsString(body);
            return ResponseEntity.status(HttpStatus.OK).body(objectMapper.writeValueAsString(body));*/
            return ResponseEntity.status(HttpStatus.OK).body("{\"login\" : "+ '"' +login+"\"}");
        } else {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\" : "+ '"' +login+" is already exists\"}");}
    }

    @PostMapping(path = "/api/session")
    public ResponseEntity<String> login(@RequestBody UserAuth body, HttpSession httpSession){
        final String login = body.getLogin();
        final String password = body.getPassword();
        if(StringUtils.isEmpty(login) || StringUtils.isEmpty(password)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\" : \"login or password is empty\"}");
        }
        if(accountService.checkUser(login)){
            if(accountService.getUser(login).getPassword().equals(password)){
                httpSession.setAttribute("login", login);
                return ResponseEntity.status(HttpStatus.OK).body("{\"login\" : "+ '"' +login+"\"}");
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\" : \"wrong login or password\"}");
    }

    @DeleteMapping(path="/api/session")
    public ResponseEntity<String> logout(HttpSession httpSession) {
        if(httpSession.getAttribute("login")!=null){
            httpSession.setAttribute("login", null);
            return ResponseEntity.status(HttpStatus.OK).body("{}");
        } else {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\" : \"session is not found\"}");}
    }

    @GetMapping(path="/api/session")
    public ResponseEntity<String> userOfCurrentSession(HttpSession httpSession){
        if(httpSession.getAttribute("login")==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\" : \"session is not found\"}");
        }
        final String login = (String)httpSession.getAttribute(KEY);
        final UserProfile user = accountService.getUser(login);
        return ResponseEntity.status(HttpStatus.OK).body("{\"login\" : "+ '"' +user.getLogin()+"\"}");
    }

    @GetMapping(path="/api/user/{login}")
    public ResponseEntity<String> getUserData (@PathVariable(value = "login") String login, HttpSession httpSession){
        if(!accountService.checkUser(login)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\" : \"user is not exists\"}");
        }
        if(httpSession.getAttribute("login")==null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"error\" : \"user is not authorized\"}");
        }

        final UserProfile user = accountService.getUser(login);
        return ResponseEntity.status(HttpStatus.OK).body(user.getDataJson());
    }
    @PostMapping(path="/api/user/{login}")
    public ResponseEntity<String> changePassword(@PathVariable(value = "login") String login, @RequestBody UserAuth body, HttpSession httpSession){
        if(!accountService.checkUser(login)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\" : \"user does not exists\"}");
        }
        if(httpSession.getAttribute("login")==null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"error\" : \"user is not authorized\"}");
        }
        accountService.changePassword(login, body);
        return ResponseEntity.status(HttpStatus.OK).body("{}");
    }

}

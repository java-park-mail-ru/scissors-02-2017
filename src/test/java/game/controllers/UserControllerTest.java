package game.controllers;


import game.users.UserInfo;
import game.users.UserProfile;
import game.services.AccountService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;


import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;


@SpringBootTest(webEnvironment = RANDOM_PORT)
@RunWith(SpringRunner.class)
public class UserControllerTest {
    @SuppressWarnings("unused")
    @MockBean
    private AccountService accountService;

    @Autowired
    private TestRestTemplate restTemplate;

    final HttpHeaders httpHeader = new HttpHeaders();


    private String bodySignUp = "{\"login\":\"test\", \"email\":\"test\", \"password\":\"test\"}";
    private String bodyLogin = "{\"login\":\"test\",  \"password\":\"test\"}";
    private String password = "{\"password\":\"test1\"}";

    private UserProfile testUserProfile = new UserProfile("test", "test", "test");
    private UserInfo testUserInfo = new UserInfo("test", 0);

    @PostConstruct
    public void initAfterStartup() {
        httpHeader.setContentType(MediaType.APPLICATION_JSON);

    }


    public List<String> signup() {
        given(accountService.addUser(testUserProfile)).willReturn(testUserInfo);

        final HttpEntity req = new HttpEntity(bodySignUp, httpHeader);

        final ResponseEntity<?> res = restTemplate.exchange("/api/signup", HttpMethod.POST, req, UserInfo.class);

        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertEquals(res.getBody(), testUserInfo);

        final List<String> cooks = res.getHeaders().get("Set-Cookie");
        assertNotNull(cooks);
        assertFalse(cooks.isEmpty());
        return cooks;
    }

    @Test
    public void singup200() {
        signup();
    }

    @Test
    public void signup400() {
        given(accountService.addUser(testUserProfile)).willReturn(null);

        final HttpEntity req = new HttpEntity(bodySignUp, httpHeader);

        final ResponseEntity<?> res = restTemplate.exchange("/api/signup", HttpMethod.POST, req, UserInfo.class);

        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
    }

    @Test
    public void logout200() {
        final List<String> cooks = signup();
        httpHeader.put(HttpHeaders.COOKIE, cooks);
        final HttpEntity req = new HttpEntity(httpHeader);
        final ResponseEntity<?> res = restTemplate.exchange("/api/session", HttpMethod.DELETE, req, Object.class);
        assertEquals(HttpStatus.OK, res.getStatusCode());
    }

    @Test
    public void logout400() {
        final HttpEntity req = new HttpEntity(httpHeader);
        final ResponseEntity<?> res = restTemplate.exchange("/api/session", HttpMethod.DELETE, req, Object.class);
        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());

    }

    @Test
    public void login200() {
        given(accountService.auth(testUserProfile)).willReturn(testUserInfo);

        final HttpEntity req = new HttpEntity(bodyLogin, httpHeader);

        final ResponseEntity<?> res = restTemplate.exchange("/api/session", HttpMethod.POST, req, UserInfo.class);

        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertEquals(res.getBody(), testUserInfo);
    }

    @Test
    public void login400() {
        given(accountService.auth(testUserProfile)).willReturn(null);

        final HttpEntity req = new HttpEntity(bodyLogin, httpHeader);

        final ResponseEntity<?> res = restTemplate.exchange("/api/session", HttpMethod.POST, req, UserInfo.class);

        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());

    }

    @Test
    public void userOfCurrentSession200() {
        given(accountService.getUser("test")).willReturn(testUserInfo);

        final List<String> cooks = signup();
        httpHeader.put(HttpHeaders.COOKIE, cooks);
        final HttpEntity req = new HttpEntity(httpHeader);

        final ResponseEntity<?> res = restTemplate.exchange("/api/session", HttpMethod.GET, req, UserInfo.class);

        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertEquals(res.getBody(), testUserInfo);
    }

    @Test
    public void userOfCurrentSession400() {
        final HttpEntity req = new HttpEntity(httpHeader);

        final ResponseEntity<?> res = restTemplate.exchange("/api/session", HttpMethod.GET, req, UserInfo.class);

        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());


    }

    @Test
    public void getUser200() {
        given(accountService.getUser("test")).willReturn(testUserInfo);

        final List<String> cooks = signup();
        httpHeader.put(HttpHeaders.COOKIE, cooks);
        final HttpEntity req = new HttpEntity(httpHeader);

        final ResponseEntity<?> res = restTemplate.exchange("/api/user/test", HttpMethod.GET, req, UserInfo.class);

        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertEquals(res.getBody(), testUserInfo);
    }

    @Test
    public void getUser403() {
        final HttpEntity req = new HttpEntity(httpHeader);

        final ResponseEntity<?> res = restTemplate.exchange("/api/user/test", HttpMethod.GET, req, UserInfo.class);

        assertEquals(HttpStatus.FORBIDDEN, res.getStatusCode());
    }

    @Test
    public void changePassword200() {
        final List<String> cooks = signup();
        httpHeader.put(HttpHeaders.COOKIE, cooks);
        final HttpEntity req = new HttpEntity(password, httpHeader);

        final ResponseEntity<?> res = restTemplate.exchange("/api/user/test", HttpMethod.POST, req, UserInfo.class);

        assertEquals(HttpStatus.OK, res.getStatusCode());

    }

    @Test
    public void changePassword403_noSession() {
        final HttpEntity req = new HttpEntity(password, httpHeader);

        final ResponseEntity<?> res = restTemplate.exchange("/api/user/test", HttpMethod.POST, req, UserInfo.class);

        assertEquals(HttpStatus.FORBIDDEN, res.getStatusCode());
    }

    @Test
    public void changePassword403() {
        final List<String> cooks = signup();
        httpHeader.put(HttpHeaders.COOKIE, cooks);
        final HttpEntity req = new HttpEntity(password, httpHeader);

        final ResponseEntity<?> res = restTemplate.exchange("/api/user/test1", HttpMethod.POST, req, UserInfo.class);

        assertEquals(HttpStatus.FORBIDDEN, res.getStatusCode());

    }
}
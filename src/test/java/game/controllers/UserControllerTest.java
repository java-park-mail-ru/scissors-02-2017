package game.controllers;

import game.services.AccountService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
/* TODO : проверять сессии*/

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc(print = MockMvcPrint.NONE)
public class UserControllerTest {
    private static final String KEY = "login";
    @Autowired
    private AccountService accountService;

    @Autowired
    private MockMvc mockMvc;

    private String bodySignUp = "{\"login\":\"test\", \"email\":\"test\", \"password\":\"test\"}";
    private String bodyLogin = "{\"login\":\"test\",  \"password\":\"test\"}";
    private String password = "{\"password\":\"test1\"}";

    @Before
    public void start() {
        accountService.clear();
    }

    @Test
    public void signup200() throws Exception {

        mockMvc
                .perform(
                        post("/api/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(bodySignUp))
                .andExpect(status().isOk())
                .andExpect(jsonPath("login").value("test"))
                .andExpect(jsonPath("score").value("0"));
    }

    @Test
    public void signup400() throws Exception {
        signup200();
        mockMvc
                .perform(
                        post("/api/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(bodySignUp))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("error").value("login is already exists"));
    }

    @Test
    public void logout200() throws Exception {
        signup200();
        mockMvc
                .perform(
                        delete("/api/session")
                                .sessionAttr(KEY, "test"))
                .andExpect(status().isOk());
    }

    @Test
    public void logout400() throws Exception {
        signup200();
        mockMvc
                .perform(
                        delete("/api/session"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void login200() throws Exception {
        signup200();
        mockMvc
                .perform(
                        post("/api/session")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(bodyLogin))
                .andExpect(status().isOk())
                .andExpect(jsonPath("login").value("test"))
                .andExpect(jsonPath("score").value("0"));
    }

    @Test
    public void login400() throws Exception {
        mockMvc
                .perform(
                        post("/api/session")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(bodyLogin))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void userOfCurrentSession200() throws Exception {
        signup200();
        mockMvc
                .perform(
                        get("/api/session")
                                .sessionAttr(KEY, "test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("login").value("test"))
                .andExpect(jsonPath("score").value("0"));
    }

    @Test
    public void userOfCurrentSession400() throws Exception {
        mockMvc
                .perform(
                        get("/api/session"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getUser200() throws Exception {
        signup200();
        mockMvc
                .perform(
                        get("/api/user/test")
                                .sessionAttr(KEY, "test"))
                .andExpect(jsonPath("login").value("test"))
                .andExpect(jsonPath("score").value("0"))
                .andExpect(status().isOk());
    }

    @Test
    public void getUser403() throws Exception {
        signup200();
        mockMvc
                .perform(
                        get("/api/user/test"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("error").value("user is not auth"));
    }

    @Test
    public void changePassword200() throws Exception {
        signup200();

        mockMvc
                .perform(
                        post("/api/user/test")
                                .sessionAttr(KEY, "test")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(password))
                .andExpect(status().isOk());
    }

    @Test
    public void changePassword403_noSession() throws Exception {
        signup200();
        mockMvc
                .perform(
                        post("/api/user/test")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(password))
                .andExpect(status().isForbidden());
    }

    @Test
    public void changePassword403() throws Exception {
        signup200();
        mockMvc
                .perform(
                        post("/api/user/test")
                                .sessionAttr(KEY, "another")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(password))
                .andExpect(status().isForbidden());
    }

    @Test
    public void rating_empty() throws Exception {
        mockMvc
                .perform(
                        get("/api/rating"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    public void rating() throws Exception {
        mockMvc
                .perform(
                        post("/api/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(bodySignUp));
        final String bodySignUp2 = "{\"login\":\"test2\", \"email\":\"test2\", \"password\":\"test2\"}";
        mockMvc
                .perform(
                        post("/api/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(bodySignUp2));
        mockMvc
                .perform(
                        get("/api/rating"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].login").value("test"))
                .andExpect(jsonPath("$[1].login").value("test2"))
                .andExpect(jsonPath("$[0].score").value("0"))
                .andExpect(jsonPath("$[1].score").value("0"));
    }

}
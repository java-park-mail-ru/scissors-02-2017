package game.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import game.models.UserInfo;
import game.models.UserProfile;
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


@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc(print = MockMvcPrint.NONE)
public class UserControllerTest {
    private static final String KEY = "login";
    @Autowired
    private AccountService accountService;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void start() {
        accountService.clear();
    }

    @Test
    public void signup200() throws Exception {
        final UserProfile user = new UserProfile("test", "test", "test");
        mockMvc
                .perform(
                        post("/api/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("login").value("test"))
                .andExpect(jsonPath("score").value("0"));
    }

    @Test
    public void signup400() throws Exception {
        signup200();
        final UserProfile user = new UserProfile("test", "test", "test");
        mockMvc
                .perform(
                        post("/api/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(user)))
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
        final UserProfile user = new UserProfile("test", "", "test");
        mockMvc
                .perform(
                        post("/api/session")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("login").value("test"));
    }

    @Test
    public void login400() throws Exception {
        final UserProfile user = new UserProfile("test", "", "test");
        mockMvc
                .perform(
                        post("/api/session")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(user)))
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
        final UserProfile user = new UserProfile("", "", "test1");
        mockMvc
                .perform(
                        post("/api/user/test")
                                .sessionAttr(KEY, "test")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(user)))
                .andExpect(status().isOk());
    }

    @Test
    public void changePassword403_noSession() throws Exception {
        signup200();
        final UserProfile user = new UserProfile("", "", "test1");
        mockMvc
                .perform(
                        post("/api/user/test")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(user)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void changePassword403() throws Exception {
        signup200();
        final UserProfile user = new UserProfile("", "", "test1");
        mockMvc
                .perform(
                        post("/api/user/test")
                                .sessionAttr(KEY, "another")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(user)))
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
        final UserProfile user1 = new UserProfile("test1", "test1", "test");
        final UserProfile user2 = new UserProfile("test2", "test2", "test");
        mockMvc
                .perform(
                        post("/api/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(user1)));
        mockMvc
                .perform(
                        post("/api/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(user2)));
        mockMvc
                .perform(
                        get("/api/rating"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].login").value("test1"))
                .andExpect(jsonPath("$[1].login").value("test2"))
                .andExpect(jsonPath("$[0].score").value("0"))
                .andExpect(jsonPath("$[1].score").value("0"));
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
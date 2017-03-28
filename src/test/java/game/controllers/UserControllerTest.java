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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc(print = MockMvcPrint.NONE)
public class UserControllerTest {
    @Autowired
    private AccountService acountService;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void start() {
        acountService.clear();
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
                                .sessionAttr("login", "test")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("error").value("login is already exists"));
    }
    @Test
    public void login200() throws Exception {

    }


    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
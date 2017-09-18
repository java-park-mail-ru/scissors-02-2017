package game.services;


import game.users.UserInfo;
import game.users.UserProfile;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@RunWith(SpringRunner.class)
@Transactional
public class AccountServiceTest {
    @Autowired
    private AccountService service;

    private UserProfile userProfileTest = new UserProfile("test", "test", "test");
    private UserInfo userInfoTest = new UserInfo("test", 0);

    @Test
    public void addUser() {
        assertEquals(userInfoTest, service.addUser(userProfileTest));
    }

    @Test
    public void addUserConflict() {
        service.addUser(userProfileTest);
        assertNull(service.addUser(userProfileTest));
    }

    @Test
    public void getUser() {
        addUser();
        assertEquals(userInfoTest, service.getUser("test"));
    }

    @Test
    public void getUser_null() {
        assertNull(service.getUser("test"));
    }

    @Test
    public void getRating() {
        addUser();
        final List<UserInfo> expect = new ArrayList<>();
        expect.add(userInfoTest);
        assertArrayEquals(expect.toArray(), service.getRating().toArray());
    }

    @Test
    public void getRatingEmpty() {
        assertTrue(service.getRating().isEmpty());
    }

    @Test
    public void authSuccess() {
        addUser();
        final UserProfile userAuth = new UserProfile("test", "", "test");
        assertEquals(userInfoTest, service.auth(userAuth));
    }

    @Test
    public void authNoUser() {
        final UserProfile userAuth = new UserProfile("test", "", "test");
        assertNull(service.auth(userAuth));
    }

    @Test
    public void authWrongPassword(){
        addUser();
        final UserProfile userAuth = new UserProfile("test", "", "another");
        assertNull(service.auth(userAuth));
    }

}

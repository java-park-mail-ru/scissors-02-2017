package game.services;


import game.models.UserInfo;
import game.models.UserProfile;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AccountServiceTest {
    @Autowired
    private AccountService service;

    @Before
    public void start() {
        service.clear();
    }

    @Test
    public void addUser() {
        final UserProfile user = new UserProfile("test", "test", "test");
        assertEquals(new UserInfo("test", 0), service.addUser(user));
    }

    @Test
    public void addUser_null() {
        final UserProfile user = new UserProfile("test", "test", "test");
        service.addUser(user);
        assertNull(service.addUser(user));
    }

    @Test
    public void getUser() {
        addUser();
        final UserInfo expect = new UserInfo("test", 0);
        assertEquals(expect, service.getUser("test"));
    }

    @Test
    public void getUser_null() {
        assertNull(service.getUser("test"));
    }

    @Test
    public void getRating() {
        addUser();
        final UserInfo user = new UserInfo("test", 0);
        final List<UserInfo> expect = new ArrayList<>();
        expect.add(user);
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
        final UserInfo expect = new UserInfo("test", 0);
        assertEquals(expect, service.auth(userAuth));
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

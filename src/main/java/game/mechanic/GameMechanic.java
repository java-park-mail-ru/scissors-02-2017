package game.mechanic;


import game.models.UserInfo;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class GameMechanic {
    @NotNull
    private static final Logger LOGGER = LoggerFactory.getLogger(GameMechanic.class);
    @NotNull
    private Set<UserInfo> playingUsers = new HashSet<>();

    @NotNull
    private ConcurrentLinkedQueue<UserInfo> waiters = new ConcurrentLinkedQueue<>();

    public void addPlayer(UserInfo user){
        if(playingUsers.contains(user)){
            return;
        }
        LOGGER.info("add waiter {}", user);
        waiters.add(user);
    }
    public void gameStep(){
        LOGGER.info("step game");

    }


}

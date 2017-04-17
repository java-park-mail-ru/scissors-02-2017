package game.mechanic;


import game.models.ClientSnap;
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
    private static final @NotNull Logger LOGGER = LoggerFactory.getLogger(GameMechanic.class);

    private @NotNull Set<UserInfo> playingUsers = new HashSet<>();

    private @NotNull ConcurrentLinkedQueue<UserInfo> waiters = new ConcurrentLinkedQueue<>();

    public void addSnap(ClientSnap clientSnap) {

    }

    public void addPlayer(UserInfo user) {
        if (playingUsers.contains(user)) {
            return;
        }
        LOGGER.info("add waiter {}", user);
        waiters.add(user);
    }

    public void gameStep() {
        LOGGER.info("step game");


    }


}

package game.transport.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import game.mechanic.GameMechanic;
import game.objects.Player;
import game.resourses.ResourseFactory;
import game.services.RemotePointService;
import game.transport.websocket.GameMessageHandlerContainer;
import game.transport.websocket.Message;
import game.users.UserInfo;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Component
public class JoinGameHandler extends MessageHandler {
    private @NotNull GameMechanic gameMechanics;
    private @NotNull GameMessageHandlerContainer messageHandlerContainer;
    private @NotNull RemotePointService remotePointService;
    private @NotNull ObjectMapper objectMapper = new ObjectMapper();
    private @NotNull ResourseFactory resourseFactory;
    private static final @NotNull Logger LOGGER = LoggerFactory.getLogger(JoinGameHandler.class);


    public JoinGameHandler(@NotNull GameMechanic gameMechanics,
                           @NotNull GameMessageHandlerContainer messageHandlerContainer,
                           @NotNull RemotePointService remotePointService,
                           @NotNull ResourseFactory resourseFactory) {
        this.gameMechanics = gameMechanics;
        this.messageHandlerContainer = messageHandlerContainer;
        this.remotePointService = remotePointService;
        this.resourseFactory = resourseFactory;
    }

    @PostConstruct
    private void init() {
        messageHandlerContainer.registerHandler("JoinGame", this);
    }

    @Override
    public void handle(@NotNull String user, @NotNull Message message, @NotNull WebSocketSession session) {
        remotePointService.addUser(user, session);
        try {
            final UserInfo userInfo = objectMapper.readValue(message.getContent(), UserInfo.class);
            final Player player = resourseFactory.get("game/player.json", Player.class);
            player.setUser(userInfo.getLogin());
            player.setScore(userInfo.getScore());
            gameMechanics.addPlayer(player);
        } catch (IOException ex) {
            LOGGER.error("wrong json format: ", ex);
        }
    }
}

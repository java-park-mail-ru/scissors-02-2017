package game.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import game.models.UserInfo;
import game.services.AccountService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.naming.AuthenticationException;
import java.io.IOException;

public class GameSocketHandler extends TextWebSocketHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameSocketHandler.class);
    private static final String KEY = "login";
    @NotNull
    private AccountService accountService;

    @NotNull
    private GameMessageHandlerContainer gameMessageHandlerContainer;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public GameSocketHandler(@NotNull AccountService accountService, @NotNull GameMessageHandlerContainer gameMessageHandlerContainer){
        this.accountService=accountService;
        this.gameMessageHandlerContainer=gameMessageHandlerContainer;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession){
        LOGGER.info("connect");

    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws AuthenticationException {
        final String login =  (String)session.getAttributes().get(KEY);
        final UserInfo user = accountService.getUser(login);
        if (login == null || user== null) {
            throw new AuthenticationException("Only authenticated users allowed to play a game");
        }
        handleMessage(user, message);
    }


    private void handleMessage(UserInfo user, TextMessage text) {

        final Message message;
        try {
            message = objectMapper.readValue(text.getPayload(), Message.class);
        } catch (IOException ex) {
            LOGGER.error("wrong json format at ping response", ex);
            return;
        }
        try {
            gameMessageHandlerContainer.handle(message, user);
        } catch (Exception e) {
            LOGGER.error("Can't handle message of type " + message.getType() + " with content: " + message.getContent(), e);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {
        LOGGER.warn("Websocket transport problem", throwable);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
         }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}

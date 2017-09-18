package game.transport.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import game.services.RemotePointService;
import game.users.UserInfo;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

@Component
public class GameSocketHandler extends TextWebSocketHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameSocketHandler.class);
    private static final String KEY = "login";
    private @NotNull GameMessageHandlerContainer gameMessageHandlerContainer;
    private @NotNull RemotePointService remotePointService;

    private final ObjectMapper objectMapper = new ObjectMapper();


    public GameSocketHandler(@NotNull GameMessageHandlerContainer gameMessageHandlerContainer,
                             @NotNull RemotePointService remotePointService) {
        this.gameMessageHandlerContainer = gameMessageHandlerContainer;
        this.remotePointService = remotePointService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        LOGGER.info("connect");
    }

    @Override
    /*protected void handleTextMessage(WebSocketSession session, TextMessage message) throws AuthenticationException {
        final String login =  (String)session.getAttributes().get(KEY);
        final UserInfo user = accountService.getUser(login);
        if (login == null || user== null) {
            throw new AuthenticationException("Only authenticated users allowed to play a game");
        }
        handleMessage(user, message);
    }*/
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        final Message mes;
        try {
            mes = objectMapper.readValue(message.getPayload(), Message.class);
            final UserInfo userInfo = objectMapper.readValue(mes.getContent(), UserInfo.class);
            final String user = userInfo.getLogin();
            handleMessage(user, message, session);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void handleMessage(String user, TextMessage text, WebSocketSession session) {
        final Message message;
        try {
            message = objectMapper.readValue(text.getPayload(), Message.class);
        } catch (IOException ex) {
            LOGGER.error("wrong json format", ex);
            return;
        }
        try {
            gameMessageHandlerContainer.handle(user, message, session);
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
        //final String user = (String) webSocketSession.getAttributes().get(KEY);
        final String user = "test";
        if (user == null) {
            return;
        }
        remotePointService.removeUser(user);
        LOGGER.info("connection close");
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}

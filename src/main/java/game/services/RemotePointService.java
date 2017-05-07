package game.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import game.transport.websocket.Message;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RemotePointService {

    private @NotNull ObjectMapper objectMapper = new ObjectMapper();
    private static final @NotNull Logger LOGGER = LoggerFactory.getLogger(RemotePointService.class);

    private ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    public void addUser(String user, WebSocketSession webSocketSession) {
        LOGGER.info("add session {}", user);
        sessions.putIfAbsent(user, webSocketSession);
    }

    public void sendMessage(Message message, String forUser) {
        final WebSocketSession session = sessions.get(forUser);
        try {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        } catch (IOException e) {
            LOGGER.error("json format error ", e);
        }
    }

    public boolean isConnected(@NotNull String user) {
        return sessions.containsKey(user) && sessions.get(user).isOpen();
    }

    public void disconnect(String player) {
        final WebSocketSession webSocketSession = sessions.get(player);
        if (webSocketSession != null && webSocketSession.isOpen()) {
            try {
                webSocketSession.close(CloseStatus.SERVER_ERROR);
            } catch (IOException ignore) {
            }
        }
    }

    public void removeUser(String user) {
        sessions.remove(user);
    }

}

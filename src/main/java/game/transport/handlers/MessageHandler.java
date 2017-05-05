package game.transport.handlers;


import game.transport.websocket.Message;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.socket.WebSocketSession;

public abstract class MessageHandler {
    public abstract void handle(@NotNull String user, @NotNull Message message, @NotNull WebSocketSession session);
}

package game.transport.websocket;


import game.transport.handlers.MessageHandler;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;

@Service
public class  GameMessageHandlerContainer {

    private static final @NotNull Logger LOGGER = LoggerFactory.getLogger(GameMessageHandlerContainer.class);

    final Map<String, MessageHandler> handlerMap = new HashMap<>();

    public void handle(@NotNull String user, @NotNull Message message, @NotNull WebSocketSession session)  {
        final MessageHandler messageHandler = handlerMap.get(message.getType());
        if (messageHandler == null) {
            LOGGER.warn("no handler for message of {} type", message.getType());
            return;
        }
        messageHandler.handle(user, message, session);
        LOGGER.info("message handled: type =[" + message.getType() + "], content=[" + message.getContent() + ']');
    }

    public <T> void registerHandler(@NotNull String type, MessageHandler handler) {
        handlerMap.put(type, handler);
    }
}
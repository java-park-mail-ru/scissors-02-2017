package game.websocket;


import game.handlers.MessageHandler;
import game.models.UserInfo;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class  GameMessageHandlerContainer {

    @SuppressWarnings("ConstantConditions")
    @NotNull
    private static final Logger LOGGER = LoggerFactory.getLogger(GameMessageHandlerContainer.class);
    final Map<String, MessageHandler> handlerMap = new HashMap<>();


    public void handle(@NotNull Message message, @NotNull UserInfo forUser)  {
        final MessageHandler messageHandler = handlerMap.get(message.getType());
        if (messageHandler == null) {
            LOGGER.warn("no handler for message of {} type", message.getType());
            return;
        }
        messageHandler.handle(message.getContent(), forUser);
        LOGGER.debug("message handled: type =[" + message.getType() + "], content=[" + message.getContent() + ']');
    }

    public <T> void registerHandler(@NotNull String type, MessageHandler handler) {
        handlerMap.put(type, handler);
    }
}
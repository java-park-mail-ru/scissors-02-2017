package game.transport.handlers;


import com.fasterxml.jackson.databind.ObjectMapper;
import game.mechanic.GameMechanic;
import game.snapshots.ClientSnap;
import game.transport.websocket.GameMessageHandlerContainer;
import game.transport.websocket.Message;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Component
public class ClientSnapHandler extends MessageHandler {
    private @NotNull GameMechanic gameMechanic;
    private @NotNull GameMessageHandlerContainer messageHandlerContainer;
    private @NotNull ObjectMapper objectMapper = new ObjectMapper();


    private static final @NotNull Logger LOGGER = LoggerFactory.getLogger(ClientSnapHandler.class);

    public ClientSnapHandler(@NotNull GameMechanic gameMechanic, @NotNull GameMessageHandlerContainer messageHandlerContainer){
        this.gameMechanic=gameMechanic;
        this.messageHandlerContainer=messageHandlerContainer;
    }
    @Override
    public void handle(@NotNull String user, @NotNull Message message, @NotNull WebSocketSession session) {
        try{
            final ClientSnap snap = objectMapper.readValue(message.getContent(), ClientSnap.class);
            gameMechanic.addSnap(user,snap);
        }catch(IOException e){
            LOGGER.error("json format error ", e);
        }
 }
    @PostConstruct
    private void init() {
        messageHandlerContainer.registerHandler("ClientSnap", this);
    }

}

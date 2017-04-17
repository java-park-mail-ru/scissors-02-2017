package game.handlers;


import com.fasterxml.jackson.databind.ObjectMapper;
import game.mechanic.GameMechanic;
import game.models.ClientSnap;
import game.models.UserInfo;
import game.websocket.GameMessageHandlerContainer;
import game.websocket.Message;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Component
public class ClientSnapHandler extends MessageHandler {
    @NotNull
    private GameMechanic gameMechanic;
    @NotNull
    private GameMessageHandlerContainer messageHandlerContainer;

    public ClientSnapHandler(@NotNull GameMechanic gameMechanic, @NotNull GameMessageHandlerContainer messageHandlerContainer){
        this.gameMechanic=gameMechanic;
        this.messageHandlerContainer=messageHandlerContainer;
    }
    @Override
    public void handle(@NotNull Message message, @NotNull UserInfo user) {
        try{
            final ClientSnap snap = new ObjectMapper().readValue(message.getContent(), ClientSnap.class);
            gameMechanic.addSnap(snap);
        }catch(IOException ex){

            return;
        }

    }
    @PostConstruct
    private void init() {
        messageHandlerContainer.registerHandler("ClientSnap", this);
    }

}

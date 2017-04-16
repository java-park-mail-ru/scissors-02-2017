package game.handlers;

import game.mechanic.GameMechanic;
import game.models.UserInfo;
import game.websocket.GameMessageHandlerContainer;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class JoinGameHandler extends MessageHandler {
    @NotNull
    private GameMechanic gameMechanics;
    @NotNull
    private GameMessageHandlerContainer messageHandlerContainer;

    public JoinGameHandler(@NotNull GameMechanic gameMechanics, @NotNull GameMessageHandlerContainer messageHandlerContainer) {
        this.gameMechanics = gameMechanics;
        this.messageHandlerContainer = messageHandlerContainer;
    }

    @PostConstruct
    private void init() {
        messageHandlerContainer.registerHandler("JoinGame", this);
    }

    @Override
    public void handle(@NotNull String message, @NotNull UserInfo forUser) {
        gameMechanics.addPlayer(forUser);
    }
}

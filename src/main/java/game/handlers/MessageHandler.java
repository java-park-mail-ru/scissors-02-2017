package game.handlers;


import game.models.UserInfo;
import game.websocket.Message;
import org.jetbrains.annotations.NotNull;

public abstract class MessageHandler {
    public abstract void handle(@NotNull Message message,@NotNull UserInfo user);
}

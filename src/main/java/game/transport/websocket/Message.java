package game.transport.websocket;


import org.jetbrains.annotations.NotNull;

public class Message {
    private @NotNull String type;
    private @NotNull String content;

    public @NotNull String getType() {
        return type;
    }
    public @NotNull String getContent() {
        return content;
    }

    public Message() {
    }

    public Message(@NotNull String type, @NotNull String content) {
        this.type = type;
        this.content = content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setType(String type) {
        this.type = type;
    }
}

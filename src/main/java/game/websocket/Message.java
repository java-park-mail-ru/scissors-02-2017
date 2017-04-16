package game.websocket;


import org.jetbrains.annotations.NotNull;

public class Message {
    @NotNull
    private String type;
    @NotNull
    private String content;

    @NotNull
    public String getType() {
        return type;
    }
    @NotNull
    public String getContent() {
        return content;
    }

    public Message() {
    }

    public Message(@NotNull String type, @NotNull String content) {
        this.type = type;
        this.content = content;
    }

}

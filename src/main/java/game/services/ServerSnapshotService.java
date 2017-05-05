package game.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import game.mechanic.GameSession;
import game.objects.Player;
import game.transport.websocket.Message;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ServerSnapshotService {
    private @NotNull ObjectMapper objectMapper = new ObjectMapper();
    private static final @NotNull Logger LOGGER = LoggerFactory.getLogger(ServerSnapshotService.class);

    public Message createSnapshot(GameSession gameSession) {
        final Message message = new Message();
        message.setType("ServerSnap");
        final Set<Player> players = gameSession.getPlayers();
        try {
            message.setContent(objectMapper.writeValueAsString(players));
        } catch (JsonProcessingException e) {
            LOGGER.error("json format error: ", e);
        }
        return message;
    }

    public Message gameIsOver(GameSession gameSession) {
        final Message message = new Message();
        message.setType("End");
        final Set<Player> players = gameSession.getPlayers();
        try {
            message.setContent(objectMapper.writeValueAsString(players));
        } catch (JsonProcessingException e) {
            LOGGER.error("json format error: ", e);
        }
        return message;

    }
}

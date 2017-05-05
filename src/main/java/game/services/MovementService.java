package game.services;


import game.mechanic.GameSession;
import game.objects.GameObject;
import org.springframework.stereotype.Service;


import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
public class MovementService {

    private GameSession gameSession;

    private Set<GameObject> objectsToMove = new HashSet<>();

    public void setGameSession(GameSession gameSession){
        this.gameSession = gameSession;
    }

    public void addObject(GameObject gameObject) {
        objectsToMove.add(gameObject);
    }

    public void move() {
        for (GameObject gameObject : objectsToMove) {
            if (Objects.equals(gameObject.getClass().getName(), "game.objects.Player")) {
                gameObject.setPresentPosition(gameObject.getDesirablePosition());
            } else if (Objects.equals(gameObject.getClass().getName(), "game.objects.bullets.Bullet")) {
                gameObject.setPresentPosition(gameObject.getDesirablePosition());
            }
        }

    }
    public void clear() {
        objectsToMove.clear();
    }
}

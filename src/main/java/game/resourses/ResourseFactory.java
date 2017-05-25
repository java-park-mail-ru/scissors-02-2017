package game.resourses;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import game.objects.Bullet;
import game.objects.GameObject;
import game.objects.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ResourseFactory {
    private final ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourseFactory.class);

    private Map<String, Object> resourses = new ConcurrentHashMap<>();

    @PostConstruct
    private void addResourses() {
        resourses.put("game/weapon/pistol.json", this.getFromFile("game/weapon/pistol.json", ResourseBullet.class));
        resourses.put("game/weapon/gun.json", this.getFromFile("game/weapon/gun.json", ResourseBullet.class));
        resourses.put("game/weapon/plasma.json", this.getFromFile("game/weapon/plasma.json", ResourseBullet.class));
        resourses.put("game/weapon/sniper.json", this.getFromFile("game/weapon/sniper.json", ResourseBullet.class));
        resourses.put("game/player.json", this.getFromFile("game/player.json", Player.class));
    }


    public ResourseFactory() {
    }

    public <T> T getFromFile(String path, Class<T> clazz) {
        URL resourceDescriptor;
        try {
            resourceDescriptor = Resources.getResource(path);
        } catch (IllegalArgumentException ex) {
            LOGGER.error("Unable to find resource " + path, ex);
            return null;
        }

        T resource;
        try {
            resource = objectMapper.readValue(resourceDescriptor, clazz);
        } catch (IOException e) {
            LOGGER.error("Failed constructing resource object " + path + " of type " + clazz.getName(), e);
            return null;
        }
        return resource;
    }

    public <T> T get(String path, Class<T> clazz) {
        Object object = resourses.get(path);
        if (object == null) {
            object = getFromFile(path, clazz);
            LOGGER.info("{}", object);
            if (object == null) {
                return null;
            } else {
                resourses.put(path, object);
                return (T) object;
            }
        }
        switch (object.getClass().getSimpleName()) {
            case ("Bullet"): {
                final ResourseBullet pattern = (ResourseBullet) object;
                final Bullet bul = new Bullet();
                bul.setSpeed(pattern.getSpeed());
                bul.setDamage(pattern.getDamage());
                bul.setType(pattern.getType());
                return (T) bul;
            }
            case ("Player"):{
                final Player pattern = (Player) object;
                final Player pl = new Player();
                pl.setHealth(pattern.getHealth());
                pl.setSpeed(pattern.getSpeed());
                return (T) pl;
            }

            default:
                return (T)object;
        }

    }

}

package game.mechanic;

import game.objects.Bullet;
import game.objects.GameObject;
import game.resourses.ResourseFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
public class Resourse {
    private @NotNull ResourseFactory factory;
    private Map<String, GameObject> resourses = new HashMap<>();

    public Resourse(@NotNull ResourseFactory factory) {
        this.factory = factory;
    }

    @PostConstruct
    private void addResourses() {
        resourses.put("pistol", factory.get("game/weapon/pistol.json", Bullet.class));
        resourses.put("gun", factory.get("game/weapon/gun.json", Bullet.class));
        resourses.put("plasma", factory.get("game/weapon/plasma.json", Bullet.class));
        resourses.put("sniper", factory.get("game/weapon/sniper.json", Bullet.class));
    }

    public @Nullable GameObject getObject(String res) {
        final GameObject object = resourses.get(res);
        if (object == null) {
            return null;
        }
        switch (object.getClass().getSimpleName()) {
            case ("Bullet"): {
                final Bullet pattern = (Bullet) object;
                final Bullet bul = new Bullet();
                bul.setSpeed(pattern.getSpeed());
                bul.setDamage(pattern.getDamage());
                bul.setType(pattern.getType());
                return bul;
            }
            default:
                return null;
        }

    }
}
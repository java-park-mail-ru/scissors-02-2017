package game.mechanic;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Clock;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Service
public class MechanicExecutor implements Runnable {
    private static final int STEP_TIME = 10000;

    private final @NotNull GameMechanic gameMechanic;

    private Executor tickExecutor = Executors.newSingleThreadExecutor();

    @Autowired
    public MechanicExecutor(@NotNull GameMechanic gameMechanic) {
        this.gameMechanic = gameMechanic;
    }

    private @NotNull Clock clock = Clock.systemDefaultZone();

    @PostConstruct
    public void initAfterStartup() {
        tickExecutor.execute(this);
    }

    @Override
    public void run() {
        int lastFrameMillis = STEP_TIME;
        while (true) {
            final int before = (int) clock.millis();
            gameMechanic.gameStep(lastFrameMillis);
            try {
                Thread.sleep(STEP_TIME);
            } catch (InterruptedException e) {
                return;
            }
            if (Thread.currentThread().isInterrupted()) {
                return;
            }

            final int afterSleep = (int) clock.millis();
            lastFrameMillis = afterSleep - before;
        }

    }
}

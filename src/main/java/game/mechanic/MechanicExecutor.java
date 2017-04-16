package game.mechanic;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Service
public class MechanicExecutor implements Runnable{
    private static final Logger LOGGER = LoggerFactory.getLogger(MechanicExecutor.class);
    private static final long STEP_TIME = 50;

    @NotNull
    private final GameMechanic gameMechanic;

    private Executor tickExecutor = Executors.newSingleThreadExecutor();

    @Autowired
    public MechanicExecutor(@NotNull GameMechanic gameMechanic) {
        this.gameMechanic = gameMechanic;
    }

    @PostConstruct
    public void initAfterStartup() {
        LOGGER.info("start");
        tickExecutor.execute(this);
    }

    @Override
    public void run() {
        while(true){
            gameMechanic.gameStep();

            try {
                Thread.sleep(STEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (Thread.currentThread().isInterrupted()) {
                return;
            }

        }

    }
}

package game.services;

import game.snapshots.ClientSnap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ClientSnapshotService {
    private final ConcurrentHashMap<String, List<ClientSnap>> clientSnaps = new ConcurrentHashMap<>();

    public void pushClientSnap(@NotNull String user, @NotNull ClientSnap snap) {
        this.clientSnaps.putIfAbsent(user, new ArrayList<>());
        clientSnaps.get(user).add(snap);
    }

    public List<ClientSnap> getClientSnaps(String user) {
        return clientSnaps.get(user);
    }

    public @Nullable ClientSnap getLastClientSnaps(String user) {
        final List<ClientSnap> snaps = clientSnaps.get(user);
            if (snaps==null){
                return null;
            }
            return snaps.get(snaps.size() - 1);
    }

    public void clear(){
        clientSnaps.clear();
    }


}

package it.polimi.ingsw.distributed;

import java.util.List;

public interface Server {
    void register(Client client);
    void update(Client client, Integer column);
    void update(Client client, String nickname);
    void update(Client client, List<int[]> coords);
}
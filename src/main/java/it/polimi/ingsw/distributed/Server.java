package it.polimi.ingsw.distributed;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Server extends Remote {
    void startGame() throws RemoteException;
    void register(Client client, String nickname) throws RemoteException;
    void update(Client client, Integer column) throws RemoteException;
    void update(Client client, String nickname) throws RemoteException;
    void update(Client client, List<int[]> coords) throws RemoteException;
}
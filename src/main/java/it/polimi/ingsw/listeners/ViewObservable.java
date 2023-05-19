package it.polimi.ingsw.listeners;

import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.distributed.Server;

import java.rmi.RemoteException;
import java.util.List;

public interface ViewObservable {
    void addListener(Server o);
    void notifyObservers(Client o, List<int[]> arg1, Integer arg2) throws RemoteException;
}

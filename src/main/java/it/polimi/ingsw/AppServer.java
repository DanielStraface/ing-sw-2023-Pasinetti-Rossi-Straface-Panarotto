package it.polimi.ingsw;

import it.polimi.ingsw.distributed.Server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AppServer extends Remote {
    public Server connect() throws RemoteException;
    public Server connect(String newGame) throws RemoteException;
}

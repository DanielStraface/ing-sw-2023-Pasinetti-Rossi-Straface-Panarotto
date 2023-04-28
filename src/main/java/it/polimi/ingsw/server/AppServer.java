package it.polimi.ingsw.server;

import it.polimi.ingsw.distributed.Server;
import it.polimi.ingsw.exceptions.TooManyMatchesException;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AppServer extends Remote {
    Server connect() throws RemoteException;
    Server connect(String newGame) throws RemoteException, TooManyMatchesException;
}

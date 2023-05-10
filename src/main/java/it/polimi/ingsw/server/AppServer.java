package it.polimi.ingsw.server;

import it.polimi.ingsw.distributed.Server;
import it.polimi.ingsw.exceptions.NotSupportedMatchesException;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AppServer extends Remote {
    enum typeOfMatch {
        existingGame, newTwoPlayersGame, newThreePlayersGame, newFourPlayersGame
    }
    Server connect() throws RemoteException;

    Server connect(typeOfMatch type) throws RemoteException, NotSupportedMatchesException;

    boolean log(String nickname) throws RemoteException;
    void removeLoggedUser(String nickname) throws RemoteException;
}

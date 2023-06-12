package it.polimi.ingsw.server;

import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.distributed.Server;
import it.polimi.ingsw.exceptions.NotSupportedMatchesException;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AppServer extends Remote {
    long CLIENT_TIMEOUT = 6000;

    /**
     * Enumeration for the type of match:
     *         - an unfinished match
     *         - a new two players game
     *         - a new three players game
     *         - a new four players game
     *
     */
    enum typeOfMatch {
        existingGame, newTwoPlayersGame, newThreePlayersGame, newFourPlayersGame
    }

    /**
     * Connects a client to a server
     * @param type typeOfMatch enum
     * @return the Server connected to
     * @throws RemoteException if the execution of a remote method call goes wrong
     * @throws NotSupportedMatchesException if there is an unsupported number of matches
     */
    Server connect(typeOfMatch type) throws RemoteException, NotSupportedMatchesException;

    /**
     * Prints a login message and adds the nicknames to the logged users String Set
     * @param nickname the joined player's nickname String
     * @return true boolean if the nickname isn't already present
     * @throws RemoteException
     */
    boolean log(String nickname, boolean isRMI) throws RemoteException;

    /**
     * Removes a logged nickname from the logged users String Set
     * @param nickname the player's nickname String to be removed
     * @throws RemoteException if the execution of a remote method call goes wrong
     */
    void removeLoggedUser(String nickname) throws RemoteException;

    /**
     * Send ping method called from client in order to verify disconnection
     */
    boolean heartbeat(String client) throws RemoteException;
    /**
     * The corresponding method of heartbeat called from client in order to stop the disconnection verification
     */
    boolean heartbeatStop(String client) throws RemoteException;
}

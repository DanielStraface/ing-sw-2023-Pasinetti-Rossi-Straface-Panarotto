package it.polimi.ingsw.server;

import it.polimi.ingsw.distributed.Server;
import it.polimi.ingsw.exceptions.NotSupportedMatchesException;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The AppServer interface is the representation of the Server computer that manages all the matches.
 * It contains methods in order to log and connect client, create and monitor matches.
 * It also contains methods that control the client connection status.
 */
public interface AppServer extends Remote {
    long CLIENT_TIMEOUT = 12000;

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
     * @param isRMI flag used if the client is connected via RMI
     * @return true boolean if the nickname isn't already present
     * @throws RemoteException if the execution of a remote method call goes wrong
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
     * @param client the nickname of the specific user client
     * @return true
     * @throws RemoteException if the execution of a remote method call goes wrong
     */
    boolean heartbeat(String client) throws RemoteException;
    /**
     * The corresponding method of heartbeat called from client in order to stop the disconnection verification
     * @param client the nickname of the specific user client
     * @return true
     * @throws RemoteException if the execution of a remote method call goes wrong
     */
    boolean heartbeatStop(String client) throws RemoteException;
}

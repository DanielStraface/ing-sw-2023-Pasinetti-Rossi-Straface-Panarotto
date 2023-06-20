package it.polimi.ingsw.distributed;

import it.polimi.ingsw.modelview.GameView;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * The AppClientRMI class represents a specific type of AppClient class used for the RMI connection type.
 * It contains a heartbeat method to monitor the client activity connection status.
 * See AppClient class documentation for more information.
 */
public interface Client extends Remote {

    enum QuitState {NORMAL, EMPTY_BAG, QUIT};

    /**
     * Update method passing a GameView
     * @param game GameView
     * @throws RemoteException if the execution of method goes wrong
     */
    void update(GameView game) throws RemoteException;

    /**
     * Update method passing a message String
     * @param msg String
     * @throws RemoteException if the execution of method goes wrong
     */
    void update(String msg) throws RemoteException;

    /**
     * Update method passing a clientID (int)
     * @param clientID int
     * @throws RemoteException if the execution of method goes wrong
     */
    void update(int clientID) throws RemoteException;

    /**
     * Update method passing a disconnection notification List (List<Object>)
     * @param notificationList List<Object>
     * @throws RemoteException if the execution of method goes wrong
     */
    void update(List<Object> notificationList) throws RemoteException;

    /**
     * Get method for a player's nickname
     * @return nickname String
     * @throws RemoteException if the execution of method goes wrong
     */
    String getNickname() throws RemoteException;

    /**
     * Get method for a clientID (int)
     * @return clientID int
     * @throws RemoteException if the execution of method goes wrong
     */
    int getClientID() throws RemoteException;
}
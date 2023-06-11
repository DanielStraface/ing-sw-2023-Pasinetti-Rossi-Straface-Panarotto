package it.polimi.ingsw.distributed;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;


public interface Server extends Remote {

    /**
     * Makes a player join a new match lobby or an unfinished match
     * @throws RemoteException
     */
    void startGame() throws RemoteException;

    /**
     * Registers a client with his nickname, adds the client to the controller's client List and as model listener
     * @param client client to register
     * @param nickname player's nickname
     * @throws RemoteException
     */
    void register(Client client, String nickname) throws RemoteException;

    /**
     * Update method with all the player's choices (coordinates, column) to update the controller and the matchLog
     * @param client the choosing player
     * @param coords coordinates chosen
     * @param column the shelf column chosen
     * @throws RemoteException
     */
    void update(Client client, List<int[]> coords, Integer column) throws RemoteException;

    /**
     * Update method with a String List containing all notifications
     * @param notificationList the String List with notification messages
     * @throws RemoteException if the execution of a remote method call goes wrong
     */
    void update(List<String> notificationList) throws RemoteException;
}
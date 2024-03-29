package it.polimi.ingsw.listeners;

import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.distributed.Server;

import java.rmi.RemoteException;
import java.util.List;

/**
 * ViewSubject is an interface implemented in all UI classes, it contains notification update
 * methods about choices made by a player and disconnections
 */
public interface ViewSubject {
    /**
     * Method to add a client as a listener
     * @param o client to be added as listener
     */
    void addListener(Server o);

    /**
     * Invokes methods that set the "changed" flag to true and notifies observers about the client
     * and its tiles and the shelf's column choices
     * @param o the client making the choices
     * @param arg1 the item's coordinates chosen
     * @param arg2 the shelf's column choice
     * @throws RemoteException if the execution of a remote method call goes wrong
     */
    void notifyObservers(Client o, List<int[]> arg1, Integer arg2) throws RemoteException;

    /**
     * notifies about disconnection
     * @param notificationList list of possible disconnection
     * @throws RemoteException if another client has disconnected
     */
    void notifyDisconnection(List<String> notificationList) throws RemoteException;
}

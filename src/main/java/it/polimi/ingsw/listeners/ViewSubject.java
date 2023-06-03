package it.polimi.ingsw.listeners;

import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.distributed.Server;

import java.rmi.RemoteException;
import java.util.List;

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
     * @throws RemoteException
     */
    void notifyObservers(Client o, List<int[]> arg1, Integer arg2) throws RemoteException;

    void notifyDisconnection(List<Object> notificationList) throws RemoteException;
}

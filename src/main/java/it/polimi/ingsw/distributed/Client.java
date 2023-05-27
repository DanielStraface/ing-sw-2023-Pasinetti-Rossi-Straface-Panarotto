package it.polimi.ingsw.distributed;

import it.polimi.ingsw.modelview.GameView;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Client extends Remote {

    /**
     * Update method passing a GameView
     * @param game GameView
     * @throws RemoteException
     */
    void update(GameView game) throws RemoteException;

    /**
     * Update method passing a message String
     * @param msg String
     * @throws RemoteException
     */
    void update(String msg) throws RemoteException;

    /**
     * Update method passing a clientID (int)
     * @param clientID int
     * @throws RemoteException
     */
    void update(int clientID) throws RemoteException;

    /**
     * Get method for a player's nickname
     * @return nickname String
     * @throws RemoteException
     */
    String getNickname() throws RemoteException;

    /**
     * Get method for a clientID (int)
     * @return clientID int
     * @throws RemoteException
     */
    int getClientID() throws RemoteException;
}
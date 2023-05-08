package it.polimi.ingsw.distributed;

import it.polimi.ingsw.modelview.GameView;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Client extends Remote {
    //void update(GameBoardView gb) throws RemoteException;
    void update(GameView game) throws RemoteException;
    //void update(Item[][] gameGrid) throws RemoteException;
    //void update(ShelfView shelf) throws RemoteException;
    //void update(Integer column) throws RemoteException;
    void update(String msg) throws RemoteException;
    void update(int clientID) throws RemoteException;
    String getNickname() throws RemoteException;
    int getClientID() throws RemoteException;
}
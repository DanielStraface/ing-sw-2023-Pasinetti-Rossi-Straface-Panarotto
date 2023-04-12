package it.polimi.ingsw.distributed;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.modelview.GameBoardView;
import it.polimi.ingsw.modelview.GameView;
import it.polimi.ingsw.modelview.ShelfView;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Client extends Remote {
    void update(GameBoardView gb) throws RemoteException;
    void update(GameView game) throws RemoteException;
    void update(Item[][] gameGrid) throws RemoteException;
    void update(ShelfView shelf) throws RemoteException;
    void update(Integer column) throws RemoteException;
}
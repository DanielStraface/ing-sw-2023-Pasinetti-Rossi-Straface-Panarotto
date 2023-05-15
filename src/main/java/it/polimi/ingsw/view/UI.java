package it.polimi.ingsw.view;

import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.listeners.ViewSubject;
import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.modelview.GameBoardView;
import it.polimi.ingsw.modelview.GameView;
import it.polimi.ingsw.modelview.PlayerView;
import it.polimi.ingsw.modelview.ShelfView;

import java.rmi.RemoteException;

public abstract class UI extends ViewSubject {
    public abstract void update(GameBoardView gb);

    public abstract void update(GameView game);

    public abstract void update(Item[][] gameGrid);

    public abstract void update(ShelfView shelf);
    public abstract void update(String msg);
    public abstract void run(GameView gameView) throws RemoteException;
    public abstract void displayInfo(GameView gameView, PlayerView playerView);
    public abstract void setReferenceClient(Client client);
}

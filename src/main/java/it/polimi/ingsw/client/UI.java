package it.polimi.ingsw.client;

import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.listeners.ViewSubject;
import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.modelview.GameBoardView;
import it.polimi.ingsw.modelview.GameView;
import it.polimi.ingsw.modelview.PlayerView;
import it.polimi.ingsw.modelview.ShelfView;

import java.rmi.RemoteException;

public interface UI extends ViewSubject {
    int MAX_NICKNAME_LEN = 15;
    static boolean nicknameController(String input){
        if(input.length() > MAX_NICKNAME_LEN | input.contains("%") || input.contains("!") || input.contains("?") || input.contains("=") ||
                input.contains("(") || input.contains(")") || input.contains("'") ||
                input.contains("/") || input.contains("£") || input.contains("$") || input.contains("€"))
            return false;
        else return true;
    }
    void update(GameBoardView gb);

    void update(GameView game);

    void update(Item[][] gameGrid);

    void update(ShelfView shelf);
    void update(String msg);
    void run(GameView gameView) throws RemoteException;
    void displayInfo(GameView gameView, PlayerView playerView);
    void setReferenceClient(Client client);
    void gameOverPointTokenHandler(GameView game, String playerNickname);
}

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
                input.contains("/") || input.contains("£") || input.contains("$") || input.contains("€") ||
                input.contains("disconnected") || input.contains("searching") || input.contains("TWO") ||
                input.contains("THREE") || input.contains("FOUR") || input.contains("unfinished") ||
                input.contains("is playing") || input.contains("BYE") || input.contains("Joining a lobby...") ||
                input.contains("Correct"))
            return false;
        else return true;
    }

    /**
     * updated method
     * @param gb GameBoardView
     */
    void update(GameBoardView gb);

    /**
     * update method
     * @param game GameView
     */
    void update(GameView game);

    /**
     * update method
     * @param gameGrid item[][]
     */
    void update(Item[][] gameGrid);

    /**
     * update method
     * @param shelf shelfView
     */
    void update(ShelfView shelf);

    /**
     * update method
     * @param msg String
     */
    void update(String msg);

    /**
     * runs the game with the provided GameView
     * @param gameView gameView
     * @throws RemoteException if the execution of a remote method call goes wrong
     */
    void run(GameView gameView) throws RemoteException;

    /**
     * displays game information based onb the provided gameView and playerView
     * @param gameView gameView
     * @param playerView playerView
     */
    void displayInfo(GameView gameView, PlayerView playerView);

    /**
     * set reference to the Client object
     * @param client the client to be set as the reference
     */
    void setReferenceClient(Client client);

    /**
     * handles the game over point when a player fills the shelf
     * @param game
     * @param playerNickname
     */
    void gameOverPointTokenHandler(GameView game, String playerNickname);
}

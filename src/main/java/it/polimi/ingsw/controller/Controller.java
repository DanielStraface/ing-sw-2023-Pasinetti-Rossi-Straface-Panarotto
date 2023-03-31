package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;

import java.util.Random;

public class Controller {
    /* ATTRIBUES SECTION */
    private final Game game;
    private final TurnHandler turnHandler;

    /* METHOD SECTION */

    /* -- constructor --*/
    public Controller(Game game){ //must be edited by View
        this.game = game;
        turnHandler = new TurnHandler(game);
    }

    /* -- logic methods --*/
    /**
     * chooseFirstPlayer method decides the first player of the match
     */
    private void chooseFirstPlayer(){
        //extract a random number between zero and numberOfPlayers
        Random random = new Random();
        int n = random.nextInt(game.getPlayers().size());
        //set isFirstPlayer = true for that player
        game.getPlayers().get(n).setIsFirstPlayer();
        game.setCurrentPlayer(game.getPlayers().get(n));
    }

    /**
     * gameActions method launch the turnHandler operations.
     */
    public void gameActions() throws Exception{
        //Add set nickname and clientID by view notifications
        //Must be modified, true may not appear
        while(true){
            turnHandler.manageTurn();
        }
    }

    /**
     * getGame method return the game reference in controller. It is synchronized due to view interactions,
     * TurnChecker and PlayerAction operations
     * @return this.game
     */
    public synchronized Game getGame(){
        return this.game;
    }
}

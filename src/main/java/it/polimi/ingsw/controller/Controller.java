package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.InvalidNumberOfItemsException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.view.TextualUI;

import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

public class Controller implements Observer {
    /* ATTRIBUES SECTION */
    private final Game game;
    private final TextualUI view;
    private final TurnHandler turnHandler;

    /* METHOD SECTION */

    /* -- constructor --*/
    public Controller(Game game, TextualUI view){ //must be edited by View
        this.game = game;
        game.setCurrentPlayer(game.getPlayers().get(0));
        turnHandler = new TurnHandler(game);
        this.view = view;
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

    @Override
    public void update(Observable o, Object arg) {
        if(o != view){
            System.err.println("Discarding notification from " + o);
        }
        if(arg instanceof String){
            String nickname = (String) arg;
            game.getCurrentPlayer().setNicknameAndClientID(nickname, 0);
        } else if(arg instanceof List<?>){
            List<int[]> list = (List<int[]>) arg;
            try {
                game.getCurrentPlayer().pickItems(list, game.getGameboard().getGameGrid(), game.getValidGrid());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if(arg instanceof Integer){
            Integer integer = (Integer) arg;
            int column = integer.intValue();
            try {
                game.getCurrentPlayer().putItemInShelf(column, game.getCurrentPlayer().getSelectItems());
            } catch (InvalidNumberOfItemsException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

package it.polimi.ingsw.controller;

import it.polimi.ingsw.listeners.ViewListener;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.view.TextualUI;

import java.io.*;
import java.util.List;
import java.util.Random;

public class Controller implements ViewListener {
    /* ATTRIBUTES SECTION */
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
    public void chooseFirstPlayer(){
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

    /** saveGame method saves the state of the game in a file*/
    public void saveGame(Game game, String fileName) {
        try{
            FileOutputStream fileOutputStream=new FileOutputStream(fileName);
            ObjectOutputStream objectOutputStream=new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(game);
            objectOutputStream.close();
            fileOutputStream.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    /** loadGame method loads a saved game from a file*/
    public static Game loadGame(String fileName) {
        try {
            FileInputStream fileInputStream = new FileInputStream(fileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Game game = (Game)objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();

            return game;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
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
    public void update(TextualUI o, Integer column) {
        int col = column.intValue();
        try {
            game.getCurrentPlayer().putItemInShelf(col);
            saveGame(getGame(),"savedGame.ser");
            this.turnHandler.manageTurn();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void update(TextualUI o, String nickname){
        game.getCurrentPlayer().setNicknameAndClientID(nickname, 0);
    }

    @Override
    public void update(TextualUI o, List<int[]> coords) {
        if( o != this.view){
            System.err.println("Discarding notification from " + o);
        } else {
            try {
                game.getCurrentPlayer().pickItems(coords, game.getGameboard().getGameGrid(), game.getValidGrid());
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }
}

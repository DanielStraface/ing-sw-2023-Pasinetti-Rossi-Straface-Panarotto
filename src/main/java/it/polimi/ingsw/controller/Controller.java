package it.polimi.ingsw.controller;

import it.polimi.ingsw.AppServerImpl;
import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.exceptions.NoSavingPointException;
import it.polimi.ingsw.exceptions.SaveFileNotFoundException;
import it.polimi.ingsw.model.Game;

import java.io.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class represent the top of the controller section (the CONTROLLER in MVC). It has got three references:
 * one to the model (attribute game), one to the view (attribute view) and one to the turnHandler, that defines the
 * correct action, explicit by the player or implicit such as various check, for a specific player turn.
 * @see TurnHandler
 * @method chooseFirstPlayer(), saveGame(), loadGame(), update(UI, Integer), update(UI, String), update(UI, List<int[]>)
 * @author Matteo Panarotto
 */
public class Controller {
    /* ATTRIBUTES SECTION */
    private final String savedFileName;
    private final Game game;
    //private final Client view;
    private final List<Client> views = new ArrayList<>();
    private final TurnHandler turnHandler;

    /* METHODS SECTION */

    /* -- constructor --*/
    public Controller(Game game, Client view, Integer numberOfController) throws RemoteException, NoSavingPointException {
        this.game = game;
        if(numberOfController == null){
            System.out.println("The numberOfController is null");
            this.savedFileName = "NOT_SAVE_POINT";
            throw new NoSavingPointException("There is no saving point file name reference for this game!");
        }
        this.savedFileName = "match" + numberOfController + ".ser";
        //game.setCurrentPlayer(game.getPlayers().get(0));
        turnHandler = new TurnHandler(game);
        this.views.add(view);
        //this.view = view;
    }

    /* -- logic methods --*/
    public void addClientView(Client view){
        this.views.add(view);
    }
    /**
     * chooseFirstPlayer method decides the first player of the match
     * @author Matteo Panarotto
     */
    public void chooseFirstPlayer() throws RemoteException{
        //extract a random number between zero and numberOfPlayers
        Random random = new Random();
        int n = random.nextInt(game.getPlayers().size());
        //set isFirstPlayer = true for that player
        game.getPlayers().get(n).setIsFirstPlayer();
        game.setCurrentPlayer(game.getPlayers().get(n));
    }

    /**
     * saveGame method saves the state of the game in a file
     * @param game - the current model of the match
     * @param fileName - the name of the saving file
     * @author Christian Pasinetti
     */
    public void saveGame(Game game, String fileName) {
        try{
            FileOutputStream fileOutputStream=new FileOutputStream(fileName);
            ObjectOutputStream objectOutputStream=new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(game);
            objectOutputStream.flush();
            objectOutputStream.reset();
            objectOutputStream.close();
            fileOutputStream.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * loadGame method loads a saved game from a file
     * @param fileName - the name of the file in which the game was saved
     * @return the game instance that represent the model stored in the fileName
     * @author Christian Pasinetti
     */
    public Game loadGame(String fileName) throws SaveFileNotFoundException {
        try {
            FileInputStream fileInputStream = new FileInputStream(fileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Game game = (Game)objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
            return game;
        } catch (IOException e) {
            throw new SaveFileNotFoundException(fileName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void continuePreviousMatch(){
        try {
            this.game.setCurrentPlayer(this.game.getCurrentPlayer());
        } catch (RemoteException e) {
            System.err.println("Unable to continue this match");
        }
    }

    /* get methods */
    /**
     * getGame method return the game reference in controller. It is synchronized due to view interactions,
     * TurnChecker and PlayerAction operations
     * @return this.game
     * @author Matteo Panarotto
     */
    public synchronized Game getGame(){
        return this.game;
    }
    public List<Client> getViews(){return this.views;}
    public boolean getGameOver(){return this.turnHandler.getGameOver();}

    /* update methods */
    /**
     * This method is a custom implementation of the observer-observable pattern. In particular, it is the update that
     * manage the column choice of the player's shelf.
     * @param o - the UI that notify this event
     * @param column - the chosen column by the player
     * @author Matteo Panarotto
     */
    public void update(Client o, Integer column) {
        if( !this.views.contains(o) ){
            System.err.println("Discarding notification from " + o);
        } else {
            int col = column.intValue();
            try {
                saveGame(getGame(),this.savedFileName);
                game.getCurrentPlayer().putItemInShelf(col);
                this.turnHandler.manageTurn(o);
            } catch (Exception e) {
                System.err.println(e.getMessage());
                System.err.println("Skipping this selection, the turn passes");
            }
        }
    }

    /**
     * This method is a custom implementation of the observer-observable pattern. In particular, it is the update that
     * manage the nickname of the player.
     * @param o - the UI that notify this event
     * @param nickname - the nickname chosen by the player
     * @author Matteo Panarotto
     */
    public void update(Client o, String nickname){
        game.getCurrentPlayer().setNicknameAndClientID(nickname, 0);
        try {
            this.chooseFirstPlayer();
        } catch (RemoteException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * This method is a custom implementation of the observer-observable pattern. In particular, it is the update that
     * manage the item selection choice by the player.
     * @param o - the UI that notify this event
     * @param coords - the list of coordinates of the item selected by the player
     * @author Matteo Panarotto
     */
    public void update(Client o, List<int[]> coords) throws RemoteException{
        if( !this.views.contains(o) ){
            System.err.println("Discarding notification from " + o);
        } else {
            try {
                game.getCurrentPlayer().pickItems(coords, game.getGameboard().getGameGrid(), game.getValidGrid());
            } catch (Exception e) {
                System.err.println(e.getMessage());
                System.err.println("Skipping this selection, repeat the turn");
                game.setCurrentPlayer(game.getCurrentPlayer());
            }
        }
    }
}

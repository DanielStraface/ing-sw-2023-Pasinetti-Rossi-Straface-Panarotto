package it.polimi.ingsw.controller;

import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.distributed.ClientImpl;
import it.polimi.ingsw.distributed.socket.middleware.ClientSkeleton;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.modelview.GameView;

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
    private int matchID;
    private final Game game;
    private final List<Client> views = new ArrayList<>();
    private final TurnHandler turnHandler;
    private static final String SelectionError = "Try again, invalid selection due to: ";
    private boolean toRepeatTheTurnFlag = false;

    /* METHODS SECTION */

    /* -- constructor --*/
    public Controller(Game game, Client view) throws RemoteException {
        this.game = game;
        turnHandler = new TurnHandler(game);
        this.views.add(view);
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

    public void setMatchID(int matchID) {this.matchID = matchID;}
    private void setToRepeatTheTurn(boolean value) {this.toRepeatTheTurnFlag = value;}

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
    private int getMatchID() {return this.matchID;}
    private boolean getToRepeatTheTurn(){return this.toRepeatTheTurnFlag;}

    /* update methods */
    /**
     * This method is a custom implementation of the observer-observable pattern. In particular, it is the update that
     * manage the column choice of the player's shelf.
     * @param o - the UI that notify this event
     * @param column - the chosen column by the player
     * @author Matteo Panarotto
     */
    public void update(Client o, Integer column) throws RemoteException {
        boolean fromValidClient = false;
        for(Client c : this.views){
            if(c.getClientID() == o.getClientID())
                fromValidClient = true;
        }
        if( !fromValidClient ){
            System.err.println("Discarding notification from client with " + o.getClientID() + " clientID number in" +
                    " update(column)");
        } else {
            int col = column.intValue();
            try {
                if(getToRepeatTheTurn()) {
                    setToRepeatTheTurn(false);
                    return;
                }
                game.getCurrentPlayer().putItemInShelf(col);
                for(Client c : this.views)
                    if(c.getClientID() == o.getClientID())
                        if(c instanceof ClientSkeleton){
                            this.turnHandler.manageTurn(c);
                        } else {
                            this.turnHandler.manageTurn(o);
                        }
                saveGame(getGame(),"savedGame.ser");
            } catch (Exception e) {
                System.err.println("Macthes#" + this.getMatchID() + ": " + e.getMessage() +
                        "\nSkipping this selection, repeat the column selection of "
                        + game.getCurrentPlayer().getNickname());
                this.views.stream()
                        .filter(client -> {
                            try {
                                return client.getClientID() == this.game.getCurrentPlayer().getClientID();
                            } catch (RemoteException ex) {
                                throw new RuntimeException(ex);
                            }
                        })
                        .findAny()
                        .get()
                        .update("WRONG_COL");
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
        boolean fromValidClient = false;
        for(Client c : this.views){
            if(c.getClientID() == o.getClientID())
                fromValidClient = true;
        }
        if(!fromValidClient){
            System.err.println("Discarding notification from client with " + o.getClientID() + " clientID number" +
                    " in update(coords)");
        } else {
            try {
                game.getCurrentPlayer().pickItems(coords, game.getGameboard().getGameGrid(), game.getValidGrid());
            } catch (Exception e) {
                System.err.println("Macthes#" + this.getMatchID() + ": " + e.getMessage() +
                        "\nSkipping this selection, repeat the turn of " + game.getCurrentPlayer().getNickname());
                for(Client c : this.views){
                    if(c.getClientID() == o.getClientID()){
                        if(c instanceof ClientSkeleton){
                            setToRepeatTheTurn(true);
                            c.update(SelectionError + e.getMessage());
                        } else {
                            o.update(SelectionError + e.getMessage());
                        }
                    }
                }
                game.setCurrentPlayer(game.getCurrentPlayer());
                for(Client c : views)
                    if(c.getClientID() == game.getCurrentPlayer().getClientID())
                        c.update(new GameView(this.game), game.getCurrentPlayer().getClientID());
            }
        }
    }
}

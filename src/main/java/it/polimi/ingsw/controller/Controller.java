package it.polimi.ingsw.controller;

import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.exceptions.FullColumnException;
import it.polimi.ingsw.exceptions.InvalidSelectionException;
import it.polimi.ingsw.exceptions.InvalidStateException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.modelview.ShelfView;

import java.io.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    private final List<Client> clients = new ArrayList<>();
    private final TurnHandler turnHandler;
    private static final String SelectionError = "Try again, invalid selection due to: ";

    /* METHODS SECTION */

    /* -- constructor --*/
    public Controller(Game game) {
        this.game = game;
        turnHandler = new TurnHandler(game);
    }

    /* -- logic methods --*/
    public void addClient(Client view){
        this.clients.add(view);
    }
    /**
     * chooseFirstPlayer method decides the first player of the match
     * @author Matteo Panarotto
     */
    public void chooseFirstPlayer() throws RemoteException{
        //extract a random number between zero and numberOfPlayers
        Random random = new Random();
        int n = random.nextInt(game.getPlayersNumber());
        Player p = game.getPlayers().stream()
                        .filter(pl -> pl.getClientID() == n*10)
                                .findFirst()
                                        .get();
        p.setIsFirstPlayer();
        String msg;
        for(Client client : this.getClients()){
            if(client.getClientID() == p.getClientID()) msg = "You are the first player.";
            else msg = p.getNickname() + " is the first player. Wait your turn!";
            client.update(msg + " Enjoy!");
        }
        game.setCurrentPlayer(p);
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
    public List<Client> getClients(){return this.clients;}
    public boolean getGameOver(){return this.turnHandler.getGameOver();}
    public int getMatchID() {return this.matchID;}

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
        for(Client c : this.clients){
            if(c.getClientID() == o.getClientID())
                fromValidClient = true;
        }
        if( !fromValidClient ){
            System.err.println("Match#" + this.getMatchID() + ", discarding notification from client with "
                    + o.getClientID() + " clientID number in update(column)");
        } else {
            int col = column.intValue();
            try {
                    game.getCurrentPlayer().putItemInShelf(col);
                for(Client c : this.clients)
                    if(c.getClientID() == o.getClientID()){
                        this.turnHandler.manageTurn(c);
                    }
                saveGame(getGame(),"savedGame.ser");
            } catch (FullColumnException e) {
                System.err.println("Match#" + this.getMatchID() + ": " + e.getMessage() +
                        "\nSkipping this selection, repeat the column selection of "
                        + game.getCurrentPlayer().getNickname());
                this.clients.stream()
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
            } catch (RemoteException e) {
                System.err.println("Match#" + this.getMatchID() +
                        "Error while try to notify the client: " + e.getMessage());
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
        for(Client c : this.clients){
            if(c.getClientID() == o.getClientID())
                fromValidClient = true;
        }
        if(!fromValidClient){
            System.err.println("Match#" + this.getMatchID() + " discarding notification from client with "
                    + o.getClientID() + " clientID number in update(coords)");
        } else {
            try {
                game.getCurrentPlayer().pickItems(coords, game.getGameboard().getGameGrid(), game.getValidGrid());
            } catch (InvalidSelectionException e) {
                coords.clear();
                System.err.println("Match#" + this.getMatchID() + ": " + e.getMessage() +
                        "\nSkipping this selection, repeat the turn of " + game.getCurrentPlayer().getNickname());
                throw new RemoteException(SelectionError + e.getMessage());
            } catch (InvalidStateException e) {
                System.err.println("Match#" + this.getMatchID() + "Something went wrong during pickItems execution: "
                        + e.getMessage());
                System.out.println("Match#" + this.getMatchID() + "Ignoring this problem");
            } catch (RemoteException e) {
                System.err.println("Match#" + this.getMatchID() + " error occurred during notification of observers: "
                        + e.getMessage() + ". The controller tries...");
                /*try{
                    o.update(new ShelfView(this.game.getCurrentPlayer().getMyShelf().getShelfGrid()));
                } catch (RemoteException ex) {
                    System.err.println("Cannot handling the problem, the error occurred again!" +
                            "\nIgnoring this for now...");
                } finally {
                    System.out.println("A remote error occurred during " + this.game.getCurrentPlayer().getNickname() +
                            " turn while calling pickItems");
                }*/
            }
        }
    }

    public void update(Client o, List<int[]> coords, Integer column) throws RemoteException {
        boolean fromValidClient = false;
        for(Client c : this.clients){
            if(c.getClientID() == o.getClientID())
                fromValidClient = true;
        }
        if(!fromValidClient){
            System.err.println("Match#" + this.getMatchID() + " discarding notification from client with "
                    + o.getClientID() + " clientID number in update");
        } else {
            try{
                game.getCurrentPlayer().pickItems(coords, game.getGameboard().getGameGrid(), game.getValidGrid());
                game.getCurrentPlayer().putItemInShelf(column);
                for(Client c : this.clients)
                    if(c.getClientID() == o.getClientID()){
                        this.turnHandler.manageTurn(c);
                    }
                saveGame(getGame(),"savedGame.ser");
            } catch (InvalidStateException e) {
                System.err.println("ERR1");
            } catch (InvalidSelectionException e) {
                System.err.println("ERR2");
            } catch (FullColumnException e) {
                System.err.println("ERR3");
            }
        }
    }
}

package it.polimi.ingsw.controller;

import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.exceptions.RMIClientDisconnectionException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

import java.io.*;
import java.net.SocketException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * This class represent the top of the controller section (the CONTROLLER in MVC). It has got three references:
 * one to the model (attribute game), one to the view (attribute view) and one to the turnHandler, that defines the
 * correct action, explicit by the player or implicit such as various check, for a specific player turn.
 * @see TurnHandler
 */
public class Controller {
    /* ATTRIBUTES SECTION */
    private int matchID;
    private Game game;
    private final List<Client> clients = new ArrayList<>();
    private TurnHandler turnHandler;

    /* METHODS SECTION */

    /* -- constructor --*/

    /**
     * Constructor method
     * @param game the game model
     */
    public Controller(Game game) {
        this.game = game;
        turnHandler = new TurnHandler(game);
    }

    /* -- logic methods --*/

    /**
     * Adds a client to the client List
     * @param view the client to be added
     */
    public void addClient(Client view){
        this.clients.add(view);
    }

    /**
     * chooseFirstPlayer method decides the first player of the match
     * @throws RemoteException if it is not possible to notify the clients
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
        String msg = null;
        StringBuilder message = new StringBuilder();
        message.append("The game will start in 5 seconds\nThe players' nickname are:\n%");
        for(Player player : game.getPlayers())
            message.append(player.getNickname()).append("!");
        for(Client client : this.getClients()){
            if(client.getClientID() == p.getClientID()) msg = "$You are the first player.";
            else msg = "$" + p.getNickname() + " is the first player.\nWait your turn!";
            client.update(message + msg + " Enjoy!");
        }
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            System.err.println("No sleep time occurred");
        }
        game.setCurrentPlayer(p);
    }

    /**
     * saveGame method saves the state of the game in a file
     * @param game - the current model of the match
     * @param fileName - the name of the saving file
     */
    public static void saveGame(Game game, String fileName) {
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
     * @throws FileNotFoundException if the file which name is fileName not exist
     */
    public static Game loadGame(String fileName) throws FileNotFoundException {
        try {
            FileInputStream fileInputStream = new FileInputStream(fileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Game game = (Game)objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
            return game;
        } catch (IOException e) {
            if(e instanceof FileNotFoundException) throw new FileNotFoundException();
            else System.err.println("IO error: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Instantiates a TurnHandler class by giving a game instance
     * @param game the Game instance given
     */
    public void substituteGameModel(Game game){
        this.game = game;
        this.turnHandler = new TurnHandler(game);
    }

    /**
     * Set method for matchID
     * @param matchID an int used for the matchID
     */
    public void setMatchID(int matchID) {this.matchID = matchID;}

    /**
     * Method used for testing only
     * @param turnHandler to set to the controller
     */
    public void setTurnHandler(TurnHandler turnHandler) { this.turnHandler = turnHandler; }

    /* get methods */
    /**
     * getGame method return the game reference in controller. It is synchronized due to view interactions,
     * TurnChecker and PlayerAction operations
     * @return this.game
     */
    public synchronized Game getGame(){
        return this.game;
    }

    /**
     * Get method for all clients
     * @return Client List
     */
    public List<Client> getClients(){return this.clients;}

    /**
     * Get method for GameOver boolean
     * @return GameOver boolean that indicates whether the game is on its last turn or not
     */
    public boolean getGameOver(){return this.turnHandler.getGameOver();}

    /**
     * Get method for MatchID
     * @return int -> MatchID
     */
    public int getMatchID() {return this.matchID;}

    /* update methods */

    /**
     * Update method for a client's coordinates and column choices
     * @param o Client
     * @param coords Coordinates chosen
     * @param column Column chosen
     * @throws RMIClientDisconnectionException if a client connected via RMI has disconnected
     */
    public void update(Client o, List<int[]> coords, Integer column) throws RMIClientDisconnectionException {
        boolean fromValidClient = false;
        try{
            for(Client c : this.clients){
                if(c.getClientID() == o.getClientID())
                    fromValidClient = true;
            }
            if(!fromValidClient){
                System.err.println("Match#" + this.getMatchID() + " discarding notification from client with "
                        + o.getClientID() + " clientID number in update");
            } else {
                game.getCurrentPlayer().pickItems(coords, game.getGameboard().getGameGrid(),
                        game.getGameboard().getValidGrid());
                game.getCurrentPlayer().putItemInShelf(column);
                for(Client c : this.clients)
                    if(c.getClientID() == o.getClientID())
                        this.turnHandler.manageTurn(this.getMatchID());
            }
        } catch (RemoteException e) {
            if(e.getCause() != null && e.getCause() instanceof SocketException){
                System.out.println("A client has disconnected during this turn...Force game termination");
                throw new RMIClientDisconnectionException();
            }
        }
    }
}

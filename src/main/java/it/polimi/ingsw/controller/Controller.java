package it.polimi.ingsw.controller;

import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

import java.io.*;
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
 * @method chooseFirstPlayer(), saveGame(), loadGame(), update(UI, Integer), update(UI, String), update(UI, List<int[]>)
 * @author Matteo Panarotto
 */
public class Controller {
    /* ATTRIBUTES SECTION */
    private int matchID;
    private Game game;
    private final List<Client> clients = new ArrayList<>();
    private TurnHandler turnHandler;

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
     * @author Christian Pasinetti
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
     * @author Christian Pasinetti
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

    public void substituteGameModel(Game game){
        this.game = game;
        this.turnHandler = new TurnHandler(game);
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
            game.getCurrentPlayer().pickItems(coords, game.getGameboard().getGameGrid(),
                    game.getGameboard().getValidGrid());
            game.getCurrentPlayer().putItemInShelf(column);
            for(Client c : this.clients)
                if(c.getClientID() == o.getClientID())
                    this.turnHandler.manageTurn(this.getMatchID(), c);
        }
    }
}

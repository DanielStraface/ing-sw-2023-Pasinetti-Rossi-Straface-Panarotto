package it.polimi.ingsw.controller;

import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.modelview.GameBoardView;
import it.polimi.ingsw.modelview.ShelfView;

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
    private Game game;
    private int[][] validGrid;
    private final List<Client> clients = new ArrayList<>();
    private TurnHandler turnHandler;
    private static final int INVALID = 0;
    private static final int PLAYABLE = 1;
    private static final int OCCUPIED = 2;
    private static final String SelectionError = "Try again, invalid selection due to: ";

    /* METHODS SECTION */

    /* -- constructor --*/
    public Controller(Game game) {
        this.game = game;
        this.validGrid=game.getGameboard().getValidGrid();
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
     * @author Cristian Pasinetti
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
     * @author Cristian Pasinetti
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

    private void noItemSelectedChecker(List<int[]> coords) throws InvalidSelectionException {
        for(int i=0;i<coords.size();i++){
            if(validGrid[coords.get(i)[0]][coords.get(i)[1]] == INVALID)
                throw new SelectionInvalidOrEmptySlotException("selected item in invalid slot");
            else if(validGrid[coords.get(i)[0]][coords.get(i)[1]] == PLAYABLE )
                throw new SelectionInvalidOrEmptySlotException("selected item in empty slot");
        }
    }
    private void freeSideChecker(List<int[]> coords) throws InvalidSelectionException {
        for (int i = 0; i < coords.size(); i++) {
            final int FIRST_ROW = 0;
            final int LAST_ROW = 8;
            final int FIRST_COLUMN = 0;
            final int LAST_COLUMN = 8;
            int row = coords.get(i)[0];
            int col = coords.get(i)[1];
            /* if items are on the edge of the game board they have always almost a free side  */
            if (col != FIRST_COLUMN && col != LAST_COLUMN && row != FIRST_ROW && row != LAST_ROW) {
                if (validGrid[row][col - 1] == OCCUPIED && validGrid[row][col + 1] == OCCUPIED &&
                        validGrid[row - 1][col] == OCCUPIED && validGrid[row + 1][col] == OCCUPIED)
                    throw new NoFreeSidesException("selected item with no free sides");
            }
        }
    }
    private void rowAndColChecker(List<int[]> coords) throws InvalidSelectionException {
        boolean sameX = true;
        boolean sameY = true;
        int XOfFirstCoordinate = coords.get(0)[0];
        int YOfFirstCoordinate = coords.get(0)[1];
        /* For-cycle to analyse values of coordinates: first bond: Items from the same row or column*/
        for (int i = 1; i < coords.size(); i++) {
            /*If all coordinates haven't the same x: items will not be picked from the same row */
            if (coords.get(i)[0] != XOfFirstCoordinate) {
                sameX = false;
            }
            /* If all coordinates haven't the same y: items will not be picked from the same column */
            if (coords.get(i)[1] != YOfFirstCoordinate) {
                sameY = false;
            }
        }
        /* If all coordinates haven't the same x or the same y: items can not be picked from the game board */
        if (!sameX && !sameY) {
            throw new NotSameRowOrColException("no same rows or cols selection");
        }
        boolean consecutiveX = true;
        boolean consecutiveY = true;
        if (sameX) {
            int minY = coords.get(0)[1];
            int maxY = coords.get(0)[1];

            for (int i = 1; i < coords.size(); i++) {
                if (coords.get(i)[1] < minY) {
                    minY = coords.get(i)[1];
                }
            }
            for (int i = 1; i < coords.size(); i++) {
                if (coords.get(i)[1] > maxY) {
                    maxY = coords.get(i)[1];
                }
            }
            if (coords.size() == 3) {
                if (maxY - minY != 2) {
                    consecutiveY = false;
                }
            }
            if (coords.size() == 2) {
                if (maxY - minY != 1) {
                    consecutiveY = false;
                }
            }
            if (!consecutiveY) {
                throw new NoConsecutiveSelectionException("no consecutive items selection");
            }
        }

        if (sameY) {
            int minX = coords.get(0)[0];
            int maxX = coords.get(0)[0];

            for (int i = 1; i < coords.size(); i++) {
                if (coords.get(i)[0] < minX) {
                    minX = coords.get(i)[0];
                }
            }
            for (int i = 1; i < coords.size(); i++) {
                if (coords.get(i)[0] > maxX) {
                    maxX = coords.get(i)[0];
                }
            }
            if (coords.size() == 3) {
                if (maxX - minX != 2) {
                    consecutiveX = false;
                }
            }
            if (coords.size() == 2) {
                if (maxX - minX != 1) {
                    consecutiveX = false;
                }
            }
            if (!consecutiveX) {
                throw new NoConsecutiveSelectionException("no consecutive item selection");
            }
        }
    }
    private void columnCheck(int columnSelected,int numOfItems) throws FullColumnException {
        int[] lastRows = game.getCurrentPlayer().getMyShelf().getLastRow();
        if(numOfItems > lastRows[columnSelected]+1){
            throw new FullColumnException();
        }
    }
    public void selectionChecker(List<int[]> coords, int col) throws InvalidSelectionException, FullColumnException {
        noItemSelectedChecker(coords);
        freeSideChecker(coords);
        rowAndColChecker(coords);
        columnCheck(col,coords.size());
    }

    /* update methods */

    public void update(Client o,List<int[]> coords, Integer column) throws RemoteException{
        boolean fromValidClient = false;
        for(Client c : this.clients){
            if(c.getClientID() == o.getClientID())
                fromValidClient = true;
        }
        if(!fromValidClient){
            System.err.println("Match#" + this.getMatchID() + " discarding notification from client with "
                    + o.getClientID() + " clientID number in update");
        } else {
                try {
                    selectionChecker(coords, column);
                } catch (InvalidSelectionException | FullColumnException e) {
                    throw new RuntimeException(e);
                }
            game.getCurrentPlayer().pickItems(coords, game.getGameboard().getGameGrid(),
                    game.getGameboard().getValidGrid());
            game.getCurrentPlayer().putItemInShelf(column);
            for(Client c : this.clients)
                if(c.getClientID() == o.getClientID())
                    this.turnHandler.manageTurn(this.getMatchID(), c);
        }
    }
}

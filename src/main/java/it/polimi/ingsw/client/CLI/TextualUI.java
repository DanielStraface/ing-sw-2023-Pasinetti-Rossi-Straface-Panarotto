package it.polimi.ingsw.client.CLI;

import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.distributed.Server;
import it.polimi.ingsw.distributed.socket.middleware.ServerStub;
import it.polimi.ingsw.exceptions.FullColumnException;
import it.polimi.ingsw.exceptions.InvalidSelectionException;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.modelview.GameBoardView;
import it.polimi.ingsw.modelview.GameView;
import it.polimi.ingsw.modelview.PlayerView;
import it.polimi.ingsw.modelview.ShelfView;
import it.polimi.ingsw.client.CLI.commands.Command;
import it.polimi.ingsw.client.CLI.commands.SelectColumnCommand;
import it.polimi.ingsw.client.CLI.commands.SelectItemsCommand;
import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.server.AppServer;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.*;

public class TextualUI implements UI, Serializable {

    private transient boolean changed = false;
    private transient final Vector<Server> observers = new Vector<>();

    /**
     * A method that invokes another method to continue reading choices from the client (from connection to match choices)
     * @return a list of Integers containing the connection and match choices made by the client
     */
    public static List<Integer> setupConnectionByUser(){
        return AppClient.mainMenu();
    }

    /**
     * A method that saves the nickname chosen by the client (while making sure that the one entered is valid)
     * @return the nickname String chosen by the client
     */
    public static String askNickname(){
        String input;
        System.out.print("\nInsert your nickname >>");
        Scanner scanner = new Scanner(System.in);
        input = scanner.nextLine();
        while(!UI.nicknameController(input)) {
            System.out.print("""
                    
                    The max nickname length must be 20 chars.
                    This chars are not allowed !£$%&/()=?' , please try again
                    >>""");
            input = scanner.nextLine();
        }
        return input;
    }

    private transient List<int[]> coords;
    private transient List<Integer> columnReference;
    private transient Client refClient;
    private transient List<Command> gameActionMenu;

    /**
     * Constructor method for TextualUI
     */
    public TextualUI(){
        this.coords = new ArrayList<>();
        this.columnReference = new ArrayList<>();
        this.gameActionMenu = Arrays.asList(new SelectItemsCommand(this.coords),
                new SelectColumnCommand(this.columnReference));
    }

    /**
     * Method for the start of a turn
     * @param gameView to get and display the GameBoard and to check if the tiles and Shelf column selected are valid
     * @throws RemoteException
     */
    public void run(GameView gameView) throws RemoteException{
        this.displayNewTurn(gameView);
        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.println("Hey " + this.refClient.getNickname() + ", is your turn!");
        while(true){
            try{
                gameActionOnGameboard(gameView.getGameBoard());
                gameActionOnShelf(gameView.getCurrentPlayer().getMyShelf());
                setChangedAndNotifyListener();
                break;
            } catch (FullColumnException e) {
                System.out.println("Wrong column selection: " + e.getMessage());
            } catch (RemoteException e) {
                System.err.println("Remote error occurred,\n" + e.getMessage());
                break;
            }
        }
    }

    /**
     * Method to display a player's personal objective card
     * @param player PlayerView to get the player's personal objective card
     */
    private void displayPersonalObjCard(PlayerView player) {
        System.out.println("\nYour personal objective card:");
        for(int i=0;i<player.getMyPersonalOBjCard().getCardGrid().length;i++){
            System.out.print(i+1 + " ");
            for(int j=0;j<player.getMyPersonalOBjCard().getCardGrid()[i].length;j++){
                if(player.getMyPersonalOBjCard().getCardGrid()[i][j].getCategoryType()==Category.CAT){
                    System.out.print("C ");
                }
                if(player.getMyPersonalOBjCard().getCardGrid()[i][j].getCategoryType()==Category.PLANT){
                    System.out.print("P ");
                }
                if(player.getMyPersonalOBjCard().getCardGrid()[i][j].getCategoryType()==Category.FRAME){
                    System.out.print("F ");
                }
                if(player.getMyPersonalOBjCard().getCardGrid()[i][j].getCategoryType()==Category.BOOK){
                    System.out.print("B ");
                }
                if(player.getMyPersonalOBjCard().getCardGrid()[i][j].getCategoryType()==Category.TROPHY){
                    System.out.print("T ");
                }
                if(player.getMyPersonalOBjCard().getCardGrid()[i][j].getCategoryType()==Category.GAME){
                    System.out.print("G ");
                }
                if(player.getMyPersonalOBjCard().getCardGrid()[i][j].getCategoryType()==null){
                    System.out.print("- ");
                }
            }
            System.out.println();
        }
        System.out.print("  ");
        for(int j=0;j<player.getMyPersonalOBjCard().getCardGrid().length-1;j++) System.out.print(j+1 + " ");
        System.out.print("\n\n");
    }

    /**
     * Method to display both common objective cards
     * @param game GameView to get the common objective cards
     */
    private void displayCommonObjCard(GameView game) {
        System.out.println("The first common obj card is " + game.getCommonObjCard().get(0).getType() +
                "\nCard description: " + game.getCommonObjCard().get(0).getDescription());
        System.out.println("The second common obj card is " + game.getCommonObjCard().get(1).getType() +
                "\nCard description: " + game.getCommonObjCard().get(1).getDescription());
    }

    /**
     * Method to display a player's shelf
     * @param shelf ShelfView to get the shelf's grid
     */
    private void displayShelf(ShelfView shelf) {
        System.out.println("Your shelf: ");
        for(int i=0; i<6; i++){
            System.out.print(i+1 + " ");
            for(int j=0; j<5; j++){
                if(shelf.getShelfGrid()[i][j].getCategoryType()==Category.CAT){
                    System.out.print("C ");
                }
                if(shelf.getShelfGrid()[i][j].getCategoryType()==Category.PLANT){
                    System.out.print("P ");
                }
                if(shelf.getShelfGrid()[i][j].getCategoryType()==Category.FRAME){
                    System.out.print("F ");
                }
                if(shelf.getShelfGrid()[i][j].getCategoryType()==Category.BOOK){
                    System.out.print("B ");
                }
                if(shelf.getShelfGrid()[i][j].getCategoryType()==Category.TROPHY){
                    System.out.print("T ");
                }
                if(shelf.getShelfGrid()[i][j].getCategoryType()==Category.GAME){
                    System.out.print("G ");
                }
                if(shelf.getShelfGrid()[i][j].getCategoryType()==null){
                    System.out.print("  ");
                }
            }
            System.out.print("\n");
        }
        System.out.print("  ");
        for(int j=0;j<shelf.getShelfGrid()[0].length;j++) System.out.print(j+1 + " ");
        System.out.print("\n\n");
    }

    /**
     * Method to display the GameBoard
     * @param gameGrid an Item matrix so it can be displayed
     */
    private void displayGameBoard(Item[][] gameGrid){
        System.out.println("The gameboard is ");
        for(int i=0;i<gameGrid.length;i++){
            System.out.print(i + " ");
            for(int j=0;j<gameGrid[i].length;j++){
                if(gameGrid[i][j].getCategoryType() == Category.CAT){
                    System.out.print("C ");
                }
                if(gameGrid[i][j].getCategoryType() == Category.GAME){
                    System.out.print("G ");
                }
                if(gameGrid[i][j].getCategoryType() == Category.PLANT){
                    System.out.print("P ");
                }
                if(gameGrid[i][j].getCategoryType() == Category.FRAME){
                    System.out.print("F ");
                }
                if(gameGrid[i][j].getCategoryType() == Category.TROPHY){
                    System.out.print("T ");
                }
                if(gameGrid[i][j].getCategoryType() == Category.BOOK){
                    System.out.print("B ");
                }
                if(gameGrid[i][j].getCategoryType() == null){
                    System.out.print("  ");
                }
            }
            System.out.print("\n");
        }
        System.out.print("  ");
        for(int j=0;j<gameGrid.length;j++) System.out.print(j + " ");
        System.out.print("\n\n");
    }

    /**
     * All the player's information that is displayed before the same player's turn
     * @param game GameView to get all the players' information
     */
    private void displayNewTurn(GameView game){
        System.out.println("=================================================================================");
        System.out.println("Your points: " + game.getCurrentPlayer().getScore());
        displayCommonObjCard(game);
        displayPersonalObjCard(game.getCurrentPlayer());
        displayGameBoard(game.getGameBoard().getGameGrid());
        displayShelf(game.getCurrentPlayer().getMyShelf());
    }

    /**
     * The info displayed at the end of a player's turn
     * @param game GameView to get the common objective cards
     * @param ply PlayerView to get the players' information
     */
    public void displayInfo(GameView game, PlayerView ply){
        System.out.println("=================================================================================");
        System.out.println("Your points: " + ply.getScore());
        displayCommonObjCard(game);
        displayPersonalObjCard(ply);
    }

    /**
     * Method that gets the tiles chosen by the player and checks if the choice is valid
     * @param gb GameBoardView to get the GameBoard's grid
     */
    public void gameActionOnGameboard(GameBoardView gb) {
        SelectItemsCommand sic = (SelectItemsCommand) this.gameActionMenu.get(0);
        sic.setGameBoardView(gb);
        while(true){
            try{
                this.coords.clear();
                sic.execute();
                break;
            } catch (InvalidSelectionException e) {
                System.out.println("Wrong item selection: " + e.getMessage());
            }
        }
        //setChangedAndNotifyListener(this.coords);
    }

    /**
     * Invokes all methods to check if the shelf's column choice is valid
     * @param sh ShelfView to modify the shelf's grid
     * @throws RemoteException
     * @throws FullColumnException
     */
    public void gameActionOnShelf(ShelfView sh) throws RemoteException, FullColumnException {
        SelectColumnCommand scc = (SelectColumnCommand) this.gameActionMenu.get(1);
        SelectItemsCommand sic = (SelectItemsCommand) this.gameActionMenu.get(0);
        scc.setShelfView(sh);
        scc.setMaxNumOfItems(sic.getNumOfItems());
        scc.execute();
    }

    /**
     * Invokes a method to display the GameBoard
     * @param gb GameBoardView
     */
    public void update(GameBoardView gb) {displayGameBoard(gb.getGameGrid());}

    /**
     * Invokes a method to display the new turn's info
     * @param game GameView
     */
    public void update(GameView game) {
        displayNewTurn(game);
    }

    /**
     * Invokes a method to display the GameBoard
     * @param gameGrid Item matrix
     */
    public void update(Item[][] gameGrid) {
        displayGameBoard(gameGrid);
    }

    /**
     * Invokes a method to display the player's shelf
     * @param shelf ShelfView
     */
    public void update(ShelfView shelf) {
        displayShelf(shelf);
    }

    /**
     * Invokes a method to display a given message
     * @param msg String given
     */
    public void update(String msg) {System.out.println(msg);}

    /**
     * Sets a reference flag to a client
     * @param client Client to be referenced
     */
    public void setReferenceClient(Client client){
        this.refClient = client;
    }

    /**
     * Invokes methods that set the "changed" flag to true and notifies observers about the client
     * and its tiles and the shelf's column choices
     * @throws RemoteException
     */
    private void setChangedAndNotifyListener() throws RemoteException{
        setChanged();
        notifyObservers(this.refClient, this.coords, this.columnReference.remove(0));
    }

    /**
     * Method that adds a Server to a Server Vector
     * @param o Server to be added
     */
    @Override
    public void addListener(Server o) {
        if (o == null)
            throw new NullPointerException();
        if (!observers.contains(o)) {
            observers.addElement(o);
        }
    }

    /**
     * Notifies observers about the client and its tiles and the shelf's column choices
     * @param o Client that made the choices
     * @param arg1 Tiles' coordinates chosen
     * @param arg2 Column chosen
     * @throws RemoteException
     */
    @Override
    public void notifyObservers(Client o, List<int[]> arg1, Integer arg2) throws RemoteException {
        Object[] arrLocal;
        synchronized (this){
            if (!changed)
                return;
            arrLocal = observers.toArray();
            clearChanged();
        }
        for (int i = arrLocal.length-1; i>=0; i--){
            Server vl = (Server) arrLocal[i];
            vl.update(o, arg1, arg2);
        }
    }

    /**
     * Sets the "changed" flag to true
     */
    public void setChanged() {
        changed = true;
    }

    /**
     * Sets the "changed" flag to false
     */
    public void clearChanged() {
        changed = false;
    }
}
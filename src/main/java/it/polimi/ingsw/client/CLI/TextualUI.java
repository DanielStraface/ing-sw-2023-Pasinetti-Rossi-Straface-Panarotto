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
import javafx.application.Platform;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;

public class TextualUI implements UI, Serializable {
    private final transient Scanner scanner = new Scanner(System.in);

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
                    
                    The max nickname length must be 15 chars.
                    This chars are not allowed !£$%&/()=?' , please try again
                    >>""");
            input = scanner.nextLine();
        }
        return input;
    }

    private transient List<int[]> coords;
    private transient List<Integer> columnReference;
    private transient Client refClient;
    private final transient List<Command> gameActionMenu;

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
     * @throws RemoteException if the client (throw getNickname) or the server (throw displayTurnMenu) are unreachable
     */
    public void run(GameView gameView) throws RemoteException{
        this.displayNewTurn(gameView);
        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.println("Hey " + this.refClient.getNickname() + ", is your turn!");
        this.displayTurnMenu(gameView);
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
        System.out.println("The shelf is: ");
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
     * Method for display the turn menu (play, display shelf and quit decisions)
     * @param gameView to get and display the player's shelf
     * @throws RemoteException if the client is unreachable
     */
    private void displayTurnMenu(GameView gameView) throws RemoteException {
        String firstDecision;
        List<String> admitDecision = Arrays.asList("play", "shelf", "quit");
        while(true){
            System.out.print("""
               Do you want to play or to display the other players' shelf?
               Please, type
               'play' if you want to start your turn,
               'shelf' if you want to see the other player's shelf,
               'quit' if you want to close MyShelfie
               >>""");
            firstDecision = scanner.nextLine();
            while(!admitDecision.contains(firstDecision)){
                System.out.print("""
                    Wrong decision, please try again: >>""");
                firstDecision = scanner.nextLine();
            }
            if(firstDecision.equals(admitDecision.get(1)))
                for(PlayerView playerView : gameView.getPlayers()){
                    if(!playerView.getNickname().equals(this.refClient.getNickname())){
                        System.out.println("Player " + playerView.getNickname());
                        displayShelf(playerView.getMyShelf());
                    }
                }
            else if(firstDecision.equals(admitDecision.get(2))){
                System.out.println("Quit from MyShelfie...");
                List<String> notificationList = Collections.singletonList(this.refClient.getNickname());
                setChanged();
                notifyDisconnection(notificationList);
                System.exit(-5);
            } else break;
        }
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
            } catch (FullColumnException ignored) {
            }
        }
    }

    /**
     * Invokes all methods to check if the shelf's column choice is valid
     * @param sh ShelfView to modify the shelf's grid
     * @throws RemoteException due to the modification of the shelf
     * @throws FullColumnException when the shelf column is full
     */
    public void gameActionOnShelf(ShelfView sh) throws RemoteException, FullColumnException {
        SelectColumnCommand scc = (SelectColumnCommand) this.gameActionMenu.get(1);
        SelectItemsCommand sic = (SelectItemsCommand) this.gameActionMenu.get(0);
        scc.setShelfView(sh);
        scc.setMaxNumOfItems(sic.getNumOfItems());
        try {
            scc.execute();
        } catch (InvalidSelectionException ignored) {
        }
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
    public void update(String msg) {
        if(msg.contains("%") && msg.contains("$")) {
            int startPlayersName = msg.indexOf("%");
            int endPlayersName = msg.indexOf("$");
            String substring = msg.substring(startPlayersName, endPlayersName);
            int thePlayersPos = msg.indexOf("The players'");
            String finalMsg1 = msg.substring(0, thePlayersPos) +
                    msg.substring(endPlayersName + 1);
            String[] playerNickname = new String[4];
            StringBuilder temp = new StringBuilder();
            temp.append(substring.substring(1));
            int counter = 0;
            while(substring.contains("!")){
                playerNickname[counter] = (temp.substring(0, temp.indexOf("!")));
                temp.delete(0, temp.indexOf("!") + 1);
                substring = temp.toString();
                counter++;
            }
            System.out.println("The player's nickname are");
            for(String name : playerNickname)
                if(name != null) System.out.println(name);
            System.out.println(finalMsg1);
        } else if (msg.contains("disconnected")) {
            System.out.println(msg);
            System.exit(-5);
        } else if (msg.contains("the bag is empty")) {
            System.out.println(msg + "\nThe game ends here...");
            System.exit(-6);
        } else System.out.println(msg);
    }

    /**
     * Sets a reference flag to a client
     * @param client Client to be referenced
     */
    public void setReferenceClient(Client client){
        this.refClient = client;
    }

    @Override
    public void gameOverPointTokenHandler(GameView game, String playerNickname) {
        this.update("Player " + playerNickname + " completely filled the shelf.\nThis is the last turn cycle");
    }

    /**
     * Invokes methods that set the "changed" flag to true and notifies observers about the client
     * and its tiles and the shelf's column choices
     * @throws RemoteException if the server is unreachable
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
     * @throws RemoteException if the server is unreachable
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

    @Override
    public void notifyDisconnection(List<String> notificationList) throws RemoteException {
        Object[] arrLocal;
        synchronized (this){
            if (!changed)
                return;
            arrLocal = observers.toArray();
            clearChanged();
        }
        for (int i = arrLocal.length-1; i>=0; i--){
            Server vl = (Server) arrLocal[i];
            vl.update(notificationList);
            System.out.println("QUO");
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
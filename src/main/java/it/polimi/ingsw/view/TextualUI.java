package it.polimi.ingsw.view;

import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.exceptions.InvalidSelectionException;
import it.polimi.ingsw.listeners.ViewSubject;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.modelview.GameBoardView;
import it.polimi.ingsw.modelview.GameView;
import it.polimi.ingsw.modelview.PlayerView;
import it.polimi.ingsw.modelview.ShelfView;
import it.polimi.ingsw.view.commands.Command;
import it.polimi.ingsw.view.commands.SelectColumnCommand;
import it.polimi.ingsw.view.commands.SelectItemsCommand;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.*;

public class TextualUI extends ViewSubject implements Serializable {
    private transient List<int[]> coords;
    private transient List<Integer> columnReference;
    private transient Client refClient;
    private transient List<Command> gameActionMenu;

    public TextualUI(){
        this.coords = new ArrayList<>();
        this.columnReference = new ArrayList<>();
        this.gameActionMenu = Arrays.asList(new SelectItemsCommand(this.coords),
                new SelectColumnCommand(this.columnReference));
    }

    public void run(GameView gameView) throws RemoteException{
        this.displayNewTurn(gameView);
        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.println("Hey " + this.refClient.getNickname() + ", is your turn!");
        try{
            gameActionOnGameboard();
            gameActionOnShelf();
            setChangedAndNotifyListener();
        } catch (InvalidSelectionException e) {
            System.out.println(e.getMessage());
        } catch (RemoteException e) {
            System.err.println("Remote error occurred,\n" + e.getMessage());
        }
    }

    private void displayPersonalObjCard(PlayerView player) {
        System.out.println("Your personal objective card:");
        for(int i=0;i<player.getMyPersonalOBjCard().getCardGrid().length;i++){
            for(int j=0;j<player.getMyPersonalOBjCard().getCardGrid()[i].length;j++){
                System.out.print(player.getMyPersonalOBjCard().getCardGrid()[i][j].getCategoryType() + " ");
            }
            System.out.println();
        }
        System.out.print("\n\n");
    }

    private void displayCommonObjCard(GameView game) {
        System.out.println("The first common obj card is " + game.getCommonObjCard().get(0).getType() +
                "\nCard description: " + game.getCommonObjCard().get(0).getDescription());
        System.out.println("The second common obj card is " + game.getCommonObjCard().get(1).getType() +
                "\nCard description: " + game.getCommonObjCard().get(1).getDescription());
    }

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

    private void displayNewTurn(GameView game){
        System.out.println("=================================================================================");
        System.out.println("Your points: " + game.getCurrentPlayer().getScore());
        displayCommonObjCard(game);
        displayPersonalObjCard(game.getCurrentPlayer());
        displayGameBoard(game.getGameBoard().getGameGrid());
        displayShelf(game.getCurrentPlayer().getMyShelf());
    }

    public void gameActionOnGameboard() throws RemoteException, InvalidSelectionException {
        this.coords.clear();
        this.gameActionMenu.get(0).execute();
        //setChangedAndNotifyListener(this.coords);
    }

    public void gameActionOnShelf() throws RemoteException {
        this.gameActionMenu.get(1).execute();
        //setChangedAndNotifyListener(this.columnReference.remove(0));
    }

    public void update(GameBoardView gb) {displayGameBoard(gb.getGameGrid());}

    public void update(GameView game) {
        displayNewTurn(game);
    }

    public void update(Item[][] gameGrid) {
        displayGameBoard(gameGrid);
    }

    public void update(ShelfView shelf) {
        displayShelf(shelf);
    }

    public void update(Integer column) {
        System.out.println("Invalid column selection. Please choose another one!");
        try {
            gameActionOnShelf();
        } catch (RemoteException e) {
            System.err.println("Error occurred while re-asking the number of column in TextualUI: "  + e.getMessage());
        }
    }
    public void update(String msg) {
        System.out.println(msg);
    }

    public void setReferenceClient(Client client){
        this.refClient = client;
    }

    private void setChangedAndNotifyListener() throws RemoteException{
        setChanged();
        notifyObservers(this.refClient, this.coords, this.columnReference.remove(0));
    }

    private void setChangedAndNotifyListener(List<int[]> coords) throws RemoteException, InvalidSelectionException {
        setChanged();
        notifyObservers(this.refClient, coords);
    }
    private void setChangedAndNotifyListener(Integer column) throws RemoteException{
        setChanged();
        notifyObservers(this.refClient, column);
    }
}
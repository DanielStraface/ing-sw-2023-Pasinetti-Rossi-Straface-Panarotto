package it.polimi.ingsw.view;

import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.listeners.ViewSubject;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.modelview.GameBoardView;
import it.polimi.ingsw.modelview.GameView;
import it.polimi.ingsw.modelview.PlayerView;
import it.polimi.ingsw.modelview.ShelfView;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.*;

public class TextualUI extends ViewSubject implements Serializable {
    private transient List<int[]> coords;
    private transient int column;
    private transient Client refClient;

    public void run(Client client) throws RemoteException{
        this.refClient = client;
        System.out.println(client.getNickname());
        askAction();
        askColumn();
        setChangedAndNotifyListener(this.coords);
        setChangedAndNotifyListener(Integer.valueOf(this.column));
    }

    private void askAction() {
        coords = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        int maxNum = 0;
        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.println("Hey, is your turn");
        System.out.print("How many items do you want to pick up? >>");
        maxNum = scanner.nextInt();
        while(maxNum <=0 || maxNum > 3){
            System.out.print("\nInvalid number of items, please choose another number >>");
            maxNum = scanner.nextInt();
        }
        while(maxNum > 0){
            int[] coordsArray = new int[2];
            int coordsInput = 10;
            System.out.print("\nInsert the row value of the item: >>");
            coordsInput = scanner.nextInt();
            while(coordsInput < 0 || coordsInput > 9 ){
                System.out.print("\nInvalid row value, please choose a valid row >>");
                coordsInput = scanner.nextInt();
            }
            coordsArray[0] = coordsInput;
            coordsInput = 10;
            System.out.print("Insert the column value of the item: >>");
            coordsInput = scanner.nextInt();
            while(coordsInput < 0 || coordsInput > 9 ){
                System.out.print("\nInvalid column value, please choose a valid column >>");
                coordsInput = scanner.nextInt();
            }
            coordsArray[1] = coordsInput;
            if(coords.size() > 0 && coords.get(0)[0] == coordsArray[0] && coords.get(0)[1] == coordsArray[1]){
                System.out.println("Error: this item was already selected. Try another selection");
            } else {
                coords.add(coordsArray);
                maxNum--;
            }
        }
        askOrder();
    }

    private void askColumn() {
        Scanner scanner = new Scanner(System.in);
        int col = 0;
        while (col < 1 || col > 5) {
            System.out.print("\nIn what column do you want to insert your items (from 1 to 5) ? >>");
            col = scanner.nextInt();
        }
        this.column = col - 1;
        //setChangedAndNotifyListener(Integer.valueOf(this.column));
    }

    private void askOrder() {
        Scanner scanner = new Scanner(System.in);
        List<Integer> selectedItems = new ArrayList<Integer>();
        for(int i=0;i<this.coords.size();i++){
            selectedItems.add(i);
        }
        List<int[]> temp = new ArrayList<int[]>();
        int cardIndex;
        int numOfItems = coords.size();
        int counter = 1;
        while(numOfItems > 0){
            do{
                for(Integer i : selectedItems)
                    System.out.print(i+1 + " ");
                System.out.print("\nWhat item do you want to insert at position number " + counter + "? >>");
                cardIndex = scanner.nextInt();
            } while(!selectedItems.contains(cardIndex-1));
            temp.add(this.coords.remove(selectedItems.indexOf(cardIndex-1)));
            selectedItems.remove(selectedItems.indexOf(cardIndex-1));
            numOfItems--;
            counter++;
        }
        this.coords = temp;
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

    private void displayGameBoard(GameBoardView gameBoard) {
        System.out.println("The gameboard is ");
        for(int i=0;i<gameBoard.getGameGrid().length;i++){
            System.out.print(i + " ");
            for(int j=0;j<gameBoard.getGameGrid()[i].length;j++){
                if(gameBoard.getGameGrid()[i][j].getCategoryType() == Category.CAT){
                    System.out.print("C ");
                }
                if(gameBoard.getGameGrid()[i][j].getCategoryType() == Category.GAME){
                    System.out.print("G ");
                }
                if(gameBoard.getGameGrid()[i][j].getCategoryType() == Category.PLANT){
                    System.out.print("P ");
                }
                if(gameBoard.getGameGrid()[i][j].getCategoryType() == Category.FRAME){
                    System.out.print("F ");
                }
                if(gameBoard.getGameGrid()[i][j].getCategoryType() == Category.TROPHY){
                    System.out.print("T ");
                }
                if(gameBoard.getGameGrid()[i][j].getCategoryType() == Category.BOOK){
                    System.out.print("B ");
                }
                if(gameBoard.getGameGrid()[i][j].getCategoryType() == null){
                    System.out.print("  ");
                }
            }
            System.out.print("\n");
        }
        System.out.print("  ");
        for(int j=0;j<gameBoard.getGameGrid().length;j++) System.out.print(j + " ");
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

    /* UNUSED METHOD THAT RUNS AT THE START OF APLICATION*/

    private void displayNewTurn(GameView game){
        System.out.println("=================================================================================");
        System.out.println("Your points: " + game.getCurrentPlayer().getScore());
        displayCommonObjCard(game);
        displayPersonalObjCard(game.getCurrentPlayer());
        displayGameBoard(game.getGameBoard());
        displayShelf(game.getCurrentPlayer().getMyShelf());
    }

    public void update(GameBoardView gb) {
        displayGameBoard(gb);
    }

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
        System.out.println("Invalid column selection. Try again!");
        askColumn();
    }

    public void update(String commonObjCardReached) {System.out.println(commonObjCardReached);}

    private void setChangedAndNotifyListener(String nm) throws RemoteException {
        setChanged();
        notifyObservers(nm);
    }
    private void setChangedAndNotifyListener(List<int[]> coords) throws RemoteException{
        setChanged();
        notifyObservers(this.refClient, coords);
    }
    private void setChangedAndNotifyListener(Integer column) throws RemoteException{
        setChanged();
        notifyObservers(this.refClient, column);
    }
}
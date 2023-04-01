package it.polimi.ingsw.view;

import it.polimi.ingsw.model.*;

import java.util.*;

public class TextualUI extends Observable implements Observer, Runnable{
    private String name;
    private List<int[]> coords;
    private int column;

    @Override
    public void run() {
        askNickname();
        askAction();
    }

    private void askAction() {
        coords = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        int maxNum = 0;
        String input = null;
        System.out.println("How many items do you want to pick up?");
        while(maxNum <=0 || maxNum > 3){
            maxNum = scanner.nextInt();
            if(maxNum <=0 || maxNum > 3) System.out.println("Invalid number of items, please choose another number");
        }
        while(maxNum > 0){
            int[] coordsArray = new int[2];
            int coordsInput = 10;
            System.out.println("Insert the row value of the item: ");
            while(coordsInput < 0 || coordsInput > 9 ){
                coordsInput = scanner.nextInt();
            }
            coordsArray[0] = coordsInput;
            coordsInput = 10;
            System.out.println("Insert the column value of the item: ");
            while(coordsInput < 0 || coordsInput > 9 ){
                coordsInput = scanner.nextInt();
            }
            coordsArray[1] = coordsInput;
            coords.add(coordsArray);
            maxNum--;
        }
        askOrder();
        askColumn();
        setChanged();
        notifyObservers(coords);
        notifyObservers(Integer.valueOf(column));
    }

    private void askColumn() {
        Scanner scanner = new Scanner(System.in);
        int col = 0;
        while (col < 1 || col > 5) {
            System.out.println("\nIn what column do you want to insert your items (from 1 to 5) ?");
            col = scanner.nextInt();
        }
        this.column = col - 1;
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
                System.out.println("\nWhat item do you want to insert at position number " + counter + "?");
                cardIndex = scanner.nextInt();
            } while(!selectedItems.contains(cardIndex-1));
            temp.add(this.coords.remove(selectedItems.indexOf(cardIndex-1)));
            selectedItems.remove(selectedItems.indexOf(cardIndex-1));
            numOfItems--;
            counter++;
        }
        this.coords = temp;
    }

    private void askNickname() {
        String input = null;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Insert your nickname");
        while(input == null){
            input = scanner.nextLine();
        }
        setChanged();
        notifyObservers(name);
    }

    @Override
    public void update(Observable o, Object arg) {
        if(!(o instanceof Game)){
            System.err.println("Discarding notification from " + o);
        } else {
            if(arg instanceof GameBoard){
                GameBoard gameBoard = (GameBoard) arg;
                displayGameBoard(gameBoard);
            } else {
                System.err.println("Discarding notification from " + o + ": " + arg);
            }
        }
        if(!(o instanceof Player)){
            if(arg instanceof Item[][]){
                Item[][] gameGrid = (Item[][]) arg;
                displayGameBoard(gameGrid);
            } else if(arg instanceof Shelf){
                Shelf shelf = (Shelf) arg;
                displayShelf(shelf);
            } else {
                System.err.println("Discarding notification from " + o + ": " + arg);
            }
        }
    }

    private void displayShelf(Shelf shelf) {
        for(int i=0; i<6; i++){
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
                    System.out.print(" ");
                }
            }
            System.out.print("\n");
        }
        System.out.print("\n");
    }

    private void displayGameBoard(GameBoard gameBoard) {
        for(int i=0;i<gameBoard.getGameGrid().length;i++){
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
                    System.out.print(" ");
                }
            }
            System.out.print("\n");
        }
        System.out.print("\n");
    }

    private void displayGameBoard(Item[][] gameGrid){
        for(int i=0;i<gameGrid.length;i++){
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
                    System.out.print(" ");
                }
            }
            System.out.print("\n");
        }
        System.out.print("\n");
    }
}

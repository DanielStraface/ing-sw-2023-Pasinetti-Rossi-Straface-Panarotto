package it.polimi.ingsw.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Scanner;

public class TextualUI extends Observable implements Runnable{
    private String name;
    private List<int[]> coords;
    private int column;

    @Override
    public void run() {
        askNickname();
        askAction();
    }

    private void askAction() {
        coords = null;
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
        notifyObservers(column);
    }

    private void askColumn() {
        Scanner scanner = new Scanner(System.in);
        int col = 0;
        while (col < 1 || col > 5) {
            System.out.println("In what column do you want to insert your items ?");
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
                    System.out.print(i + " ");
                System.out.println("What item do you want to insert at position number ?" + counter);
                cardIndex = scanner.nextInt();
            } while(!selectedItems.contains(cardIndex));
            temp.add(this.coords.remove(selectedItems.indexOf(cardIndex)));
            selectedItems.remove(selectedItems.indexOf(cardIndex));
            numOfItems--;
        }
        this.coords = temp;
    }

    private void askNickname() {
        String input = null;
        Scanner scanner = new Scanner(System.in);
        while(input == null){
            input = scanner.nextLine();
        }
        setChanged();
        notifyObservers(name);
    }
}
package it.polimi.ingsw.view.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SelectOrderCommand implements Command{
    private final List<Integer> cardinalNumberOfSelectedItems;
    private final Scanner scanner;
    private List<int[]> coordsToOrder;
    private List<Integer> sortingOrder;

    public SelectOrderCommand(){
        this.scanner = new Scanner(System.in);
        this.cardinalNumberOfSelectedItems = new ArrayList<Integer>();
        this.sortingOrder = null;
    }

    public void itemToOrder(List<int[]> coords){
        this.coordsToOrder = coords;
    }

    public void askUser(){
        for(int i=0;i<this.coordsToOrder.size();i++){
            this.cardinalNumberOfSelectedItems.add(i);
        }
        List<Integer> sorting = new ArrayList<>();
        int cardIndex;
        int numOfItems = this.coordsToOrder.size();
        int counter = 1;
        while(numOfItems > 0){
            do{
                for(Integer i : this.cardinalNumberOfSelectedItems)
                    System.out.print(i+1 + " ");
                System.out.print("\nWhat item do you want to insert at position number " + counter + "? >>");
                cardIndex = scanner.nextInt();
            } while(!this.cardinalNumberOfSelectedItems.contains(cardIndex - 1));
            sorting.add(cardIndex - 1);
            this.cardinalNumberOfSelectedItems.remove(this.cardinalNumberOfSelectedItems.indexOf(cardIndex - 1));
            numOfItems--;
            counter++;
        }
        this.cardinalNumberOfSelectedItems.clear();
        this.sortingOrder = sorting;
    }

    @Override
    public void execute() {
        this.askUser();
        List<int[]> temp = new ArrayList<>();
        for(Integer i : this.sortingOrder)
            temp.add(this.coordsToOrder.get(i));
        this.coordsToOrder.clear();
        this.coordsToOrder.addAll(temp);
    }
}

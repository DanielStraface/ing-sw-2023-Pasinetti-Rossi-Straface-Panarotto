package it.polimi.ingsw.client.CLI.commands;

import it.polimi.ingsw.exceptions.FullColumnException;
import it.polimi.ingsw.exceptions.InvalidSelectionException;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SelectOrderCommand implements Command{
    private final List<Integer> cardinalNumberOfSelectedItems;
    private final Scanner scanner;
    private List<int[]> coordsToOrder;
    private List<Integer> sortingOrder;

    /**
     * Constructor method
     */
    public SelectOrderCommand(){
        this.scanner = new Scanner(System.in);
        this.cardinalNumberOfSelectedItems = new ArrayList<Integer>();
        this.sortingOrder = null;
    }

    /**
     * Sets the list of the tiles' coordinates
     * @param coords an int List of the tiles' coordinates chosen
     */
    public void itemToOrder(List<int[]> coords){
        this.coordsToOrder = coords;
    }

    /**
     * Prints to the player a question about the tiles' order for when they're put in the shelf and saves the choice
     * (all invalid numbers are not accepted)
     */
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

    /**
     * Invokes methods to ask the tiles' order, to check if the column selected isn't full
     * and to save the choice made in a List of int arrays
     * @throws InvalidSelectionException when the items selection from the game board is not correct
     * @throws  FullColumnException when the shelf column is full
     */
    @Override
    public void execute() throws InvalidSelectionException, FullColumnException {
        this.askUser();
        this.check();
    }

    /**
     * Checks if the column selected isn't full and saves the choice made in a List of int arrays
     * @throws InvalidSelectionException when the items selection from the game board is not correct
     * @throws FullColumnException when the shelf column is full
     */
    @Override
    public void check() throws InvalidSelectionException, FullColumnException {
        List<int[]> temp = new ArrayList<>();
        for(Integer i : this.sortingOrder)
            temp.add(this.coordsToOrder.get(i));
        this.coordsToOrder.clear();
        this.coordsToOrder.addAll(temp);
    }
}

package it.polimi.ingsw.client.CLI.commands;

import it.polimi.ingsw.exceptions.FullColumnException;
import it.polimi.ingsw.modelview.ShelfView;

import java.util.List;
import java.util.Scanner;

/**
 * The SelectColumnCommand class represents a command for selecting a column of the player's shelf
 * where the selected items will be placed
 */
public class SelectColumnCommand implements Command{
    private final Scanner scanner;
    private int col;
    private List<Integer> columnReference;
    private ShelfView shelfView;
    private int numOfItems;

    /**
     * Constructor method
     * @param param the list of Integer that passed the reference to the column variable used in order to make the
     *                  column choice
     */
    public SelectColumnCommand(List<Integer> param){
        this.scanner = new Scanner(System.in);
        this.columnReference = param;
    }

    /**
     * Prints to the player a question about the shelf's column where the tiles will be put in and saves the choice made
     */
    public void askUser(){
        System.out.print("\nIn what column do you want to insert your items (from 1 to 5) ? >>");
        this.col = this.scanner.nextInt();
        while (this.col < 1 || this.col > 5) {
            System.out.print("\nInvalid column number, please choose a valid column\n>>");
            this.col = this.scanner.nextInt();
        }
        this.col = this.col - 1;
    }

    /**
     * Checks if the shelf's column selected is valid
     * @param columnSelected the shelf's column selected
     * @throws FullColumnException when the shelf column is full
     */
    private void columnCheck(int columnSelected) throws FullColumnException {
        int[] lastRows = shelfView.getLastRow();
        if(numOfItems > lastRows[columnSelected]+1){
            throw new FullColumnException();
        }
    }

    /**
     * Set method for the ShelfView
     * @param shelf ShelfView
     */
    public void setShelfView(ShelfView shelf){
        this.shelfView= shelf;
    }

    /**
     * Set method for the numOfItems (number of tiles taken)
     * @param numOfItems int -> number of tiles taken
     */
    public void setMaxNumOfItems(int numOfItems){
        this.numOfItems=numOfItems;
    }

    /**
     * Set method for the column (choose column of the shelf)
     * @param col int -> number of chosen column
     */
    public void setColumn(int col){
        this.col = col - 1;
    }

    /**
     * Invokes all methods to ask the shelf's column, to check if it is valid and to save it in an Integer List
     * @throws FullColumnException when the shelf column is full
     */
    @Override
    public void execute() throws FullColumnException{
        this.askUser();
        this.check();
    }

    /**
     * Checks if it is valid and saves it in an Integer List
     * @throws FullColumnException when the shelf column is full
     */
    @Override
    public void check() throws FullColumnException {
        columnCheck(this.col);
        this.columnReference.add(this.col);
    }
}

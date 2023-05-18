package it.polimi.ingsw.view.CLI.commands;

import it.polimi.ingsw.exceptions.FullColumnException;
import it.polimi.ingsw.modelview.ShelfView;

import java.util.List;
import java.util.Scanner;

public class SelectColumnCommand implements Command{
    private final Scanner scanner;
    private int col;
    private List<Integer> columnReference;
    private ShelfView shelfView;
    private int numOfItems;

    public SelectColumnCommand(List<Integer> param){
        this.scanner = new Scanner(System.in);
        this.columnReference = param;
    }

    public void askUser(){
        System.out.print("\nIn what column do you want to insert your items (from 1 to 5) ? >>");
        this.col = this.scanner.nextInt();
        while (this.col < 1 || this.col > 5) {
            System.out.print("\nInvalid column number, please choose a valid column\n>>");
            this.col = this.scanner.nextInt();
        }
        this.col = this.col - 1;
    }

    private void columnCheck(int columnSelected) throws FullColumnException {
        int[] lastRows = shelfView.getLastRow();
        if(numOfItems > lastRows[columnSelected]+1){
            throw new FullColumnException();
        }
    }
    public void setShelfView(ShelfView shelf){
        this.shelfView= shelf;
    }
    public void setMaxNumOfItems(int numOfItems){
        this.numOfItems=numOfItems;
    }

    @Override
    public void execute() throws FullColumnException {
        this.askUser();
        columnCheck(this.col);
        this.columnReference.add(Integer.valueOf(this.col));
    }
}

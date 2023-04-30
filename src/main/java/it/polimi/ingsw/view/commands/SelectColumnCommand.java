package it.polimi.ingsw.view.commands;

import java.util.List;
import java.util.Scanner;

public class SelectColumnCommand implements Command{
    private Scanner scanner;
    private int col;
    private List<Integer> columnReference;

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

    @Override
    public void execute() {
        this.askUser();
        this.columnReference.add(Integer.valueOf(this.col));
    }
}

package it.polimi.ingsw.view.commands;

import java.util.List;
import java.util.Scanner;

public class SelectItemsCommand implements Command{
    private Scanner scanner;
    private List<int[]> coords;
    private SelectOrderCommand sortingCommand;

    public SelectItemsCommand(List<int[]> param){
        this.scanner = new Scanner(System.in);
        this.coords = param;
        this.sortingCommand = new SelectOrderCommand();
    }

    public void coordsToSelect(List<int[]> coords){
        this.coords = coords;
    }

    private void askUser(){
        int maxNum = 0;
        System.out.print("\nHow many items do you want to pick up? >>");
        maxNum = this.scanner.nextInt();
        while(maxNum <=0 || maxNum > 3){
            System.out.print("\nInvalid number of items, please choose another number >>");
            maxNum = this.scanner.nextInt();
        }
        while(maxNum > 0){
            int[] coordsArray = new int[2];
            int coordsInput = 10;
            System.out.print("\nInsert the row value of the item: >>");
            coordsInput = this.scanner.nextInt();
            while(coordsInput < 0 || coordsInput > 9 ){
                System.out.print("\nInvalid row value, please choose a valid row >>");
                coordsInput = this.scanner.nextInt();
            }
            coordsArray[0] = coordsInput;
            coordsInput = 10;
            System.out.print("Insert the column value of the item: >>");
            coordsInput = this.scanner.nextInt();
            while(coordsInput < 0 || coordsInput > 9 ){
                System.out.print("\nInvalid column value, please choose a valid column >>");
                coordsInput = this.scanner.nextInt();
            }
            coordsArray[1] = coordsInput;
            if(this.coords.size() > 0 && this.coords.get(0)[0] == coordsArray[0] && this.coords.get(0)[1] == coordsArray[1]){
                System.out.println("Error: this item was already selected. Try another selection");
            } else {
                this.coords.add(coordsArray);
                maxNum--;
            }
        }
    }

    @Override
    public void execute() {
        this.askUser();
        this.sortingCommand.itemToOrder(this.coords);
        this.sortingCommand.execute();
    }
}

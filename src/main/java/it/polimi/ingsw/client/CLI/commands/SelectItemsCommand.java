package it.polimi.ingsw.client.CLI.commands;

import it.polimi.ingsw.modelview.GameBoardView;

import it.polimi.ingsw.exceptions.*;

import java.util.List;
import java.util.Scanner;

public class SelectItemsCommand implements Command{
    private final Scanner scanner;
    private List<int[]> coords;
    private final SelectOrderCommand sortingCommand;
    private int maxNum;
    private int numOfPickItems;
    private GameBoardView gb;
    private static final int INVALID = 0;
    private static final int PLAYABLE = 1;
    private static final int OCCUPIED = 2;


    /**
     * Constructor method
     */
    public SelectItemsCommand(List<int[]> param){
        this.scanner = new Scanner(System.in);
        this.coords = param;
        this.sortingCommand = new SelectOrderCommand();
    }

    /**
     * Prints to the player a question about how many tiles have to be picked up and which ones to pick by using
     * their coordinates and saves the choices (all invalid numbers are not accepted)
     */
    private void askUser(){
        numOfPickItems = 0;
        System.out.print("\nHow many items do you want to pick up? >>");
        numOfPickItems = this.scanner.nextInt();
        while(numOfPickItems <=0 || numOfPickItems > 3){
            System.out.print("\nInvalid number of items, please choose another number >>");
            numOfPickItems = this.scanner.nextInt();
        }
        maxNum = numOfPickItems;
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

    /**
     * Checks if all the coordinates selected contain a tile
     * @param validGrid an int Matrix with all the GameBoard's coordinates' info
     * @throws InvalidSelectionException when the items selection from the game board is not correct
     */
    private void noItemSelectedChecker(int[][] validGrid) throws InvalidSelectionException {
        for(int i=0;i<coords.size();i++){
            if(validGrid[coords.get(i)[0]][coords.get(i)[1]] == INVALID)
                throw new SelectionInvalidOrEmptySlotException("selected item in invalid slot");
            else if(validGrid[coords.get(i)[0]][coords.get(i)[1]] == PLAYABLE )
                throw new SelectionInvalidOrEmptySlotException("selected item in empty slot");
        }
    }

    /**
     * Checks if all coordinates selected have at least one free side
     * @param validGrid an int Matrix with all the GameBoard's coordinates' info
     * @throws InvalidSelectionException when the items selection from the game board is not correct
     */
    private void freeSideChecker(int[][] validGrid) throws InvalidSelectionException {
        for (int i = 0; i < coords.size(); i++) {
            final int FIRST_ROW = 0;
            final int LAST_ROW = 8;
            final int FIRST_COLUMN = 0;
            final int LAST_COLUMN = 8;
            int row = coords.get(i)[0];
            int col = coords.get(i)[1];
            /* if items are on the edge of the game board they have always almost a free side  */
            if (col != FIRST_COLUMN && col != LAST_COLUMN && row != FIRST_ROW && row != LAST_ROW) {
                if (validGrid[row][col - 1] == OCCUPIED && validGrid[row][col + 1] == OCCUPIED &&
                        validGrid[row - 1][col] == OCCUPIED && validGrid[row + 1][col] == OCCUPIED)
                    throw new NoFreeSidesException("selected item with no free sides");
            }
        }
    }

    /**
     * Checks if all the coordinates chosen are from either the same row or column
     * @throws InvalidSelectionException when the items selection from the game board is not correct
     */
    private void rowAndColChecker() throws InvalidSelectionException {
        boolean sameX = true;
        boolean sameY = true;
        int XOfFirstCoordinate = coords.get(0)[0];
        int YOfFirstCoordinate = coords.get(0)[1];
        /* For-cycle to analyse values of coordinates: first bond: Items from the same row or column*/
        for (int i = 1; i < coords.size(); i++) {
            /*If all coordinates haven't the same x: items will not be picked from the same row */
            if (coords.get(i)[0] != XOfFirstCoordinate) {
                sameX = false;
            }
            /* If all coordinates haven't the same y: items will not be picked from the same column */
            if (coords.get(i)[1] != YOfFirstCoordinate) {
                sameY = false;
            }
        }
        /* If all coordinates haven't the same x or the same y: items can not be picked from the game board */
        if (!sameX && !sameY) {
            throw new NotSameRowOrColException("no same rows or cols selection");
        }
        boolean consecutiveX = true;
        boolean consecutiveY = true;
        if (sameX) {
            int minY = coords.get(0)[1];
            int maxY = coords.get(0)[1];

            for (int i = 1; i < coords.size(); i++) {
                if (coords.get(i)[1] < minY) {
                    minY = coords.get(i)[1];
                }
            }
            for (int i = 1; i < coords.size(); i++) {
                if (coords.get(i)[1] > maxY) {
                    maxY = coords.get(i)[1];
                }
            }
            if (coords.size() == 3) {
                if (maxY - minY != 2) {
                    consecutiveY = false;
                }
            }
            if (coords.size() == 2) {
                if (maxY - minY != 1) {
                    consecutiveY = false;
                }
            }
            if (!consecutiveY) {
                throw new NoConsecutiveSelectionException("no consecutive items selection");
            }
        }

        if (sameY) {
            int minX = coords.get(0)[0];
            int maxX = coords.get(0)[0];

            for (int i = 1; i < coords.size(); i++) {
                if (coords.get(i)[0] < minX) {
                    minX = coords.get(i)[0];
                }
            }
            for (int i = 1; i < coords.size(); i++) {
                if (coords.get(i)[0] > maxX) {
                    maxX = coords.get(i)[0];
                }
            }
            if (coords.size() == 3) {
                if (maxX - minX != 2) {
                    consecutiveX = false;
                }
            }
            if (coords.size() == 2) {
                if (maxX - minX != 1) {
                    consecutiveX = false;
                }
            }
            if (!consecutiveX) {
                throw new NoConsecutiveSelectionException("no consecutive item selection");
            }
        }
    }

    /**
     * Invokes all methods that check if the coordinates chosen are valid
     * @throws InvalidSelectionException when the items selection from the game board is not correct
     */
    public void selectionChecker()throws InvalidSelectionException{
        noItemSelectedChecker(gb.getValidGrid());
        freeSideChecker(gb.getValidGrid());
        rowAndColChecker();
    }

    /**
     * Set method for the GameBoardView
     * @param gb GameBoardView
     */
    public void setGameBoardView(GameBoardView gb){this.gb = gb;}

    /**
     * Get method for the number of tiles chosen
     * @return int -> number of tiles chosen
     */
    public int getNumOfItems(){ return numOfPickItems; }

    /**
     * Invokes methods to ask the number of tiles, their coordinates and their order for when
     * they're put in the shelf and to check if the choices made are valid
     *
     * @throws InvalidSelectionException when the items selection from the game board is not correct
     */
    @Override
    public void execute() throws InvalidSelectionException, FullColumnException {
        this.askUser();
        this.check();
        this.sortingCommand.itemToOrder(this.coords);
        this.sortingCommand.execute();
    }

    /**
     * Checks if the choices made are valid
     * @throws InvalidSelectionException when the items selection from the game board is not correct
     */
    @Override
    public void check() throws InvalidSelectionException {
        this.selectionChecker();
    }
}

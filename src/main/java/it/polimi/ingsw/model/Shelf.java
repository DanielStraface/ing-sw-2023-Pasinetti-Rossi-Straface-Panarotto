package it.polimi.ingsw.model;

import java.io.Serializable;

public class Shelf implements Serializable {

    private static final int SHELF_ROWS=6;
    private static final int SHELF_COLUMNS=5;
    private static final int ARRAY_LENGTH = 5;
    private static final int TOP_ROW=0;
    private Item[][] shelfGrid;
    private int[] lastRow;

    public Shelf() {
        this.shelfGrid = new Item[SHELF_ROWS][SHELF_COLUMNS];
        for (int row = 0; row < SHELF_ROWS; row++) {
            for (int col = 0; col < SHELF_COLUMNS; col++) {
                shelfGrid[row][col] = new Item(null);
            }
        }
        this.lastRow = new int[ARRAY_LENGTH];
        for(int i=0; i<ARRAY_LENGTH; i++){
            lastRow[i] = 5;
        }
    }

    /**
     * method that checks if the Shelf is full by checking if the first row is full
     * @return a true boolean is the Shelf is full
     */
    public boolean isFull() {
        for(int i=0;i<SHELF_COLUMNS;i++){
            if (this.getShelfGrid()[TOP_ROW][i].getCategoryType() == null) return false;
        }
        return true;
    }

    /* get methods */
    public Item[][] getShelfGrid(){
        return shelfGrid;
    }

    public int[] getLastRow(){ return lastRow; }

}






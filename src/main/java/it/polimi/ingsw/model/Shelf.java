package it.polimi.ingsw.model;

public class Shelf {

    private static final int SHELF_ROWS=6;
    private static final int SHELF_COLUMNS=5;
    private static final int TOP_ROW=0;
    private Item[][] shelfGrid;

    public Shelf() {
        /*this.shelfGrid = new Item[SHELF_ROWS][SHELF_COLUMNS];
        for (int row = 0; row < SHELF_ROWS; row++) {
            for (int col = 0; col < SHELF_COLUMNS; col++) {
                shelfGrid[row][col] = new Item(null);
            }
        }*/
        System.out.println("Hello");
    }

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

}






package it.polimi.ingsw.model;

public class Shelf {

    private static final int SHELF_ROWS=6;
    private static final int SHELF_COLUMNS=5;
    private Item[][] shelfGrid;

    public Shelf() {
        this.shelfGrid = new Item[SHELF_ROWS][SHELF_COLUMNS];
        for (int row = 0; row < SHELF_ROWS; row++) {
            for (int col = 0; col < SHELF_COLUMNS; col++) {
                shelfGrid[row][col] = new Item(null);
            }
        }
    }

        public boolean isFull() {
        for(int i=0;i<SHELF_ROWS;i++){
            for(int j=0;j<SHELF_COLUMNS;j++)
                if (this.getShelfGrid()[i][j].getCategoryType() == null) return false;
        }
        return true;
    }

    /* get methods */
    public Item[][] getShelfGrid(){
        return shelfGrid;
    }

}






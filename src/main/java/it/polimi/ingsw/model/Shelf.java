package it.polimi.ingsw.model;

public class Shelf {

    private static final int SHELF_ROWS=6;
    private static final int SHELF_COLUMS=5;
    private Item[][] shelfGrid = new Item[SHELF_ROWS][SHELF_COLUMS];

    public boolean isFull() {
        for(int i=0;i<SHELF_ROWS;i++){
            for(int j=0;j<SHELF_COLUMS;j++)
                if (this.getShelfGrid()[i][j].getCategoryType() == null) return false;
        }
        return true;
    }

    /* get methods */
    public Item[][] getShelfGrid() {
        return shelfGrid;
    }

}






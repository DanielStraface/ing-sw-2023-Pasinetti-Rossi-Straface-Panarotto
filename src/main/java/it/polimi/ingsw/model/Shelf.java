package it.polimi.ingsw.model;

public class Shelf {

    private static final int SHELF_ROWS=6;
    private static final int SHELF_COLUMS=5;
    private Item[][] shelfGrid = new Item[SHELF_ROWS][SHELF_COLUMS];


    public Item[][] getShelfGrid() {
        return shelfGrid;
    }

}






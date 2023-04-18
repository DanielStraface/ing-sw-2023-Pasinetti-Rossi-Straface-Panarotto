package it.polimi.ingsw.model.modelview;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.Shelf;

public class ShelfView {
    private static final int ROWS = 6;
    private static final int COLS = 5;
    private final Item[][] shelfGrid;
    public ShelfView(Shelf shelf) {
        this.shelfGrid = new Item[ROWS][COLS];
        for(int i=0;i<ROWS;i++){
            for(int j=0;j<COLS;j++){
                this.shelfGrid[i][j] = new Item(shelf.getShelfGrid()[i][j].getCategoryType());
            }
        }
    }

    public Item[][] getShelfGrid(){
        return this.shelfGrid;
    }
}

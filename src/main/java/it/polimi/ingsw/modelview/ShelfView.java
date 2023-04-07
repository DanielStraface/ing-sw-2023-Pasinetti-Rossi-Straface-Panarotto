package it.polimi.ingsw.modelview;

import it.polimi.ingsw.model.Item;

public class ShelfView {
    private final Item[][] shelfGrid;

    public ShelfView(Item[][] shelfGrid) {
        this.shelfGrid = new Item[shelfGrid.length][shelfGrid[0].length];
        for(int i=0;i<shelfGrid.length;i++){
            for(int j=0;j<shelfGrid[0].length;j++){
                this.shelfGrid[i][j] = new Item(shelfGrid[i][j].getCategoryType());
            }
        }
    }

    public Item[][] getShelfGrid(){
        return shelfGrid;
    }
}

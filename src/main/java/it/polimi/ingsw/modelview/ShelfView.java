package it.polimi.ingsw.modelview;

import it.polimi.ingsw.model.Item;

import java.io.Serializable;

public class ShelfView implements Serializable {
    private final Item[][] shelfGrid;
    private final int[] lastRow;

    public ShelfView(Item[][] shelfGrid, int[] lastRow) {
        this.shelfGrid = new Item[shelfGrid.length][shelfGrid[0].length];
        this.lastRow = new int[lastRow.length];
        for(int i=0;i<shelfGrid.length;i++){
            for(int j=0;j<shelfGrid[0].length;j++){
                this.shelfGrid[i][j] = new Item(shelfGrid[i][j].getCategoryType());
            }
            if(i<=4){
                this.lastRow[i]=lastRow[i];
            }
        }
    }

    public Item[][] getShelfGrid(){
        return shelfGrid;
    }
    public int[] getLastRow(){ return lastRow; }
}

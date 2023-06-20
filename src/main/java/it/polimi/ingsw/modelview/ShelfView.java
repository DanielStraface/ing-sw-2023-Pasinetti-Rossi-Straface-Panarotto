package it.polimi.ingsw.modelview;

import it.polimi.ingsw.model.Item;

import java.io.Serial;
import java.io.Serializable;

/**
 * The immutable view of Shelf
 */
public class ShelfView implements Serializable {
    @Serial
    private static final long serialVersionUID = 2L;
    private final Item[][] shelfGrid;
    private final int[] lastRow;

    /**
     * constructor method
      * @param shelfGrid -> item[][]
     * @param lastRow -> int[]
     */
    public ShelfView(Item[][] shelfGrid, int[] lastRow) {
        this.shelfGrid = new Item[shelfGrid.length][shelfGrid[0].length];
        this.lastRow = new int[lastRow.length];
        for(int i=0;i<shelfGrid.length;i++){
            for(int j=0;j<shelfGrid[0].length;j++){
                this.shelfGrid[i][j] = new Item(shelfGrid[i][j].getCategoryType(), shelfGrid[i][j].getVariant());
            }
            if(i<=4){
                this.lastRow[i]=lastRow[i];
            }
        }
    }

    /**
     * get method
     * @return Item[][] -> shelfGrid
     */
    public Item[][] getShelfGrid(){
        return shelfGrid;
    }

    /**
     * get method
     * @return int[] -> lastRow, the array that represents the last row of the shelf
     */
    public int[] getLastRow(){ return lastRow; }
}

package it.polimi.ingsw.model.comcard;

import it.polimi.ingsw.model.Category;
import it.polimi.ingsw.model.Item;

import java.io.Serializable;

class XCards extends StrategyCheck implements Serializable {
    /* ATTRIBUTES SECTION */

    /* METHOD SECTION */

    /** constructor method
     * @param type int
      */
    public XCards(int type){
        this.type = type;
    }

    /**
     * method that returns a true boolean if there are at least five tiles of the same type forming an "X"
     * @param grid the player's Shelf grid to be checked
     * @return a true boolean if the condition is satisfied
     */
    @Override
    public boolean check(Item[][] grid) {
        for(int i=1;i<5;i++){
            for(int j=1;j<4;j++){
                Category scanned = grid[i][j].getCategoryType();
                if(scanned != null){
                    if(scanned == grid[i-1][j-1].getCategoryType() && //element top left
                            scanned == grid[i-1][j+1].getCategoryType() && //element top right
                            scanned == grid[i+1][j-1].getCategoryType() && //element bottom left
                            scanned == grid[i+1][j+1].getCategoryType()){ //element bottom right
                        return true; //there must be at least one
                    }
                }
            }
        }
        //if the double for internal return statement is not taken there are not x in the grid. Must return false
        return false;
    }
}

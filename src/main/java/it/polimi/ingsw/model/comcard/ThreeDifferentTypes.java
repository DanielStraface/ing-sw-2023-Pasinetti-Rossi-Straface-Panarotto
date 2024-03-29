package it.polimi.ingsw.model.comcard;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.Category;

import java.io.Serializable;
import java.util.HashSet;

/**
 * A specific CommonObjCard group type (commonObjCardID 7,5)
 */
class ThreeDifferentTypes extends StrategyCheck implements Serializable {
    /* ATTRIBUTES SECTION */
    /* METHOD SECTION */

    /** constructor
     * @param type int
     */
    public ThreeDifferentTypes(int type){
        this.type = type;
    }

    /**
     * Method checker calls two different type of methods that verified the subsists of a certain condition
     * in the player shelf
     * @param grid the item matrix of the player's shelf
     * @return boolean that tells if a player satisfies a CommonObjectiveCard's condition
     */
    @Override
    public boolean check(Item[][] grid) {
        switch (this.type) {
            case 7 -> {
                return rowsDifferentTypes(grid);
            }
            case 5 -> {
                return columnsDifferentTypes(grid);
            }
            default -> {
                return false;
            }
        }
    }

    /**
     * Method that returns a true boolean if there are at least 4 full rows in the Player's Shelf with 3 different Item
     * categories at max, this is done by using a HashSet with one slot reserved for every type of category
     * @param grid -> Item[][]
     */
    private boolean rowsDifferentTypes(Item[][] grid) {
        int rowCounter = 0;
        boolean invalidRow = false;
        for(int i=0; i<6; i++) {
            HashSet<Category> uniqueObjects = new HashSet<Category>();
            for (int j = 0; j < 5; j++) {
                if (grid[i][j].getCategoryType() == null) {
                    j = 5;
                    invalidRow = true;
                }
                else {
                    Category scanned = grid[i][j].getCategoryType();
                    uniqueObjects.add(scanned);
                }
            }
            if (uniqueObjects.size() <= 3 && invalidRow == false) {
                rowCounter++;
            }
            invalidRow = false;
        }
        if(rowCounter>=4){
            return true;
        }
        return false;
    }


    /**
     * Method that returns a true boolean if there are at least 3 full columns in the Player's Shelf with 3 different
     * Item categories at max, this is done by using a HashSet with one slot reserved for every type of category
     * @param grid -> Item[][]
     */
    private boolean columnsDifferentTypes(Item[][] grid) {
        int columnCounter = 0;
        boolean invalidColumn = false;
        for(int j=0; j<5; j++) {
            HashSet<Category> uniqueObjects = new HashSet<Category>();
            for (int i = 0; i < 6; i++) {
                if (grid[i][j].getCategoryType() == null) {
                    i = 6;
                    invalidColumn = true;
                }
                else {
                    Category scanned = grid[i][j].getCategoryType();
                    uniqueObjects.add(scanned);
                }
            }
            if (uniqueObjects.size() <= 3 && !invalidColumn) {
                columnCounter++;
            }
            invalidColumn = false;
        }
        return columnCounter >= 3;
    }
}

package it.polimi.ingsw.model.comcard;

import it.polimi.ingsw.model.Category;
import it.polimi.ingsw.model.Item;

import java.io.Serializable;


/**
 * A specific CommonObjCard group type (commonObjCardID 6, 2)
 */
class RowsColumnsCard extends StrategyCheck implements Serializable {
    /* ATTRIBUTES SECTION */
    /* METHODS SECTION */

    /**
     * constructor method
     * @param type int
     */
    public RowsColumnsCard(int type){
        this.type = type;
    }

    /* -- logic methods --*/

    /**
     * Method checker calls two different type of methods that verified the subsists of a certain condition
     * in the player shelf
     * @param grid the item matrix of the player's shelf
     * @return boolean that tells if a player satisfies a CommonObjectiveCard's condition
     */
    @Override
    public boolean check(Item[][] grid) {
        switch (this.type) {
            case 6 -> {
                return rowsChecker(grid);
            }
            case 2 -> {
                return columnsChecker(grid);
            }
            default -> {
                return false;
            }
        }
    }

    /**
     * method that returns a true boolean if there are at least two full rows of items each formed by six different types
     * @param grid -> Item[][]
     */
    private boolean rowsChecker(Item[][] grid) {
        int rowsCounter = 0; //this is the counter of the rows needed for the goal (at least 2)

        for(int i=0;i<6;i++){
            int[] categoryCounter = new int[6]; //this array contains a counter for each category enum type
            int columnsCounter = 0; //this is a counter for the category types that appear in the consider row
            int zeroCounter = 0; //this is a counter for the only one category type that not appear in the consider row
            for(int j=0;j<5;j++){
                //here each category type is counted for a specific row
                if(grid[i][j].getCategoryType() == Category.CAT){categoryCounter[0]++;}
                if(grid[i][j].getCategoryType() == Category.BOOK){categoryCounter[1]++;}
                if(grid[i][j].getCategoryType() == Category.GAME){categoryCounter[2]++;}
                if(grid[i][j].getCategoryType() == Category.FRAME){categoryCounter[3]++;}
                if(grid[i][j].getCategoryType() == Category.TROPHY){categoryCounter[4]++;}
                if(grid[i][j].getCategoryType() == Category.PLANT){categoryCounter[5]++;}
            }
            for(int k=0;k<6;k++){
                //by reading the array the position with one value must be five
                if(categoryCounter[k] == 1){
                    columnsCounter++;
                }
                //only one position must be zero
                if(categoryCounter[k] == 0){
                    zeroCounter++;
                }
            }
            //if the condition express is true that column in count as "satisfy condition"
            if(columnsCounter == 5 && zeroCounter == 1){
                rowsCounter++;
            }
            //if the rows that satisfy the condition is at least two, the goal is reach. Must be returned true,
            //else another row is tested until the end of the shelf grid
            if(rowsCounter >= 2){
                return true;
            }
        }
        //if the double for internal return statement is not taken, the goal is not reach. Must be returned false
        return false;
    }

    /**
     * method that returns a true boolean if there are at least two full columns of items each formed by six different types
     * @param grid -> Item[][]
     */
    private boolean columnsChecker(Item[][] grid) {
        int columnsCounter = 0; //this is the counter of the columns needed for the goal (at least 2)

        for(int j=0;j<5;j++) {
            int[] categoryCounter = new int[6]; //this array contains a counter for each category enum type
            int rowsCounter = 0; //this is a counter for the category types that appear in the consider column
            for (int i = 0; i < 6; i++) {
                //here each category type is counted for a specific column
                if (grid[i][j].getCategoryType() == Category.CAT) {
                    categoryCounter[0]++;
                }
                if (grid[i][j].getCategoryType() == Category.BOOK) {
                    categoryCounter[1]++;
                }
                if (grid[i][j].getCategoryType() == Category.GAME) {
                    categoryCounter[2]++;
                }
                if (grid[i][j].getCategoryType() == Category.FRAME) {
                    categoryCounter[3]++;
                }
                if (grid[i][j].getCategoryType() == Category.TROPHY) {
                    categoryCounter[4]++;
                }
                if (grid[i][j].getCategoryType() == Category.PLANT) {
                    categoryCounter[5]++;
                }
            }
            for (int k = 0; k < 6; k++) {
                //by reading the array the position with one value must be six
                if (categoryCounter[k] == 1) {
                    rowsCounter++;
                }
            }
            //if the condition express is true then the consider column is assumed "satisfy condition"
            if (rowsCounter == 6) {
                columnsCounter++;
            }
            //if the columns that satisfied the condition are at least two, the goal is reach. Must be returned true
            if(columnsCounter >= 2){
                return true;
            }
        }
        //if the double for internal return statement is not taken, the goal is not reached. Must be returned false
        return false;
    }
}
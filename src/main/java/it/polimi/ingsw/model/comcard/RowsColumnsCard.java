package it.polimi.ingsw.model.comcard;

import it.polimi.ingsw.model.Category;
import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.Player;

class RowsColumnsCard implements StrategyCheck {
    /* METHODS SECTION */

    /* -- logic methods --*/

    /**
     * Method checker calls two different type of methods that verified the subsists of a certain condition
     * in the player shelf
     */
    @Override
    public boolean check(Player player, int status) {
        boolean result = false;
        switch (status) {
            case 6 -> result = rowsChecker(player);
            case 8 -> result = columnsChecker(player);
            default -> {
                return false;
            }
        }
        return true;
    }

    /**
     * Method rowsChecker controls if the conditions express in the sixth common objective card subsists.
     */
    private boolean rowsChecker(Player player) {
        Item[][] grid = player.getMyShelf().GetShelfGrid(); //import the player's shelf grid
        int[] categoryCounter = new int[6]; //this array contains a counter for each category enum type
        int rowsCounter = 0; //this is the counter of the rows needed for the goal (at least 2)

        for(int i=0;i<6;i++){
            int columnsCounter = 0; //this is a counter for the category types that appear in the consider row
            int zeroCounter = 0; //this is a counter for the only one category type that not appear in the consider row
            for(int j=1;j<5;j++){
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

    private boolean columnsChecker(Player player) {
        Item[][] grid = player.getMyShelf().GetShelfGrid(); //import the player's shelf grid
        int[] categoryCounter = new int[6]; //this array contains a counter for each category enum type
        int columnsCounter = 0; //this is the counter of the columns needed for the goal (at least 2)

        for(int j=0;j<5;j++) {
            int rowsCounter = 0; //this is a counter for the category types that appear in the consider column
            for (int i = 1; i < 6; i++) {
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
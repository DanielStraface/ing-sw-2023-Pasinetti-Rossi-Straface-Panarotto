package it.polimi.ingsw.model.comcard;

import it.polimi.ingsw.model.Category;
import it.polimi.ingsw.model.Item;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class GroupCards extends StrategyCheck implements Serializable {
    /* ATTRIBUTES SECTION */

    /* METHOD SECTION */

    /** constructor
     * @param type
     */
    public GroupCards(int type){
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
            case 4 -> {
                return groupOfTwo(grid);
            }
            case 3 -> {
                return groupOfFour(grid);
            }
            case 1 -> {
                return groupOfSquares(grid);
            }
            case 9 -> {
                return groupOfEight(grid);
            }
            default -> {
                return false;
            }
        }
    }

    /**
     * method that returns a true boolean if there are at least six groups containing two tiles of the same type
     * @param grid
     */
    private boolean groupOfTwo(Item[][] grid) {

        boolean[][] check = new boolean[6][5];
        int groups=0;

        for(int i=0; i<5; i++){
            for(int j=0; j<4; j++){
                if(!check[i][j] && grid[i][j].getCategoryType()!=null ) {
                    if (grid[i + 1][j].getCategoryType() == grid[i][j].getCategoryType() && !check[i + 1][j]) {
                            check[i][j] = true;
                            check[i + 1][j] = true;
                            groups++;
                        }
                     else if (grid[i][j + 1].getCategoryType() == grid[i][j].getCategoryType() && !check[i][j+1]){
                         check[i][j] = true;
                         check[i][j+1] = true;
                         groups++;
                     }
                }
            }
        }
        for(int i=0; i<5; i++){
            if(!check[i][4] && grid[i][4].getCategoryType()!=null &&
                    !check[i+1][4] && grid[i+1][4].getCategoryType() == grid[i][4].getCategoryType()){
                groups++;
            }
        }
        for(int j=0; j<4; j++){
            if(!check[5][j] && grid[5][j].getCategoryType()!=null &&
                    !check[5][j+1] && grid[5][j+1].getCategoryType() == grid[5][j].getCategoryType()){
                groups++;
            }
        }
        if(groups == 6){
            return true;
        }
        return false;
    }

    /**
     * method that returns a true boolean if there are at least four groups containing four adjacent tiles of the
     * same type
     * @param grid
     */
    private boolean groupOfFour(Item[][] grid) {
        Category scanned;
        int groupDimCounter = 0;
        List<Integer> groupsFound = new ArrayList<>();
        groupsFound.add(0);
        List<Boolean> groupFlag = new ArrayList<>();
        groupFlag.add(false);
        // boolean matrix in order to not revisit already checked Items
        boolean[][] visited = new boolean[grid.length][grid[0].length];
        for(int i=0; i<grid.length; i++){
            for(int j=0; j<grid[i].length; j++){
                if(!visited[i][j] && grid[i][j].getCategoryType() != null){
                    scanned = grid[i][j].getCategoryType();
                    // recursive method that checks adjacent tiles in the Shelf
                    adjacentCategoryCheck(grid, visited, scanned, i, j, groupDimCounter, groupsFound, groupFlag);
                    // counter resets
                    groupDimCounter = 0;
                    groupFlag.clear();
                    groupFlag.add(false);
                }
            }
        }
        return groupsFound.get(0) >= 4;
    }

    /**
     * method that returns a true boolean if there are at least two groups containing four tiles of the same type
     * for both groups
     * @param grid
     */
    private boolean groupOfSquares(Item[][] grid) {
        int[] categoryReference = new int[6]; //category reference for comparison
        List<Category> list = Arrays.asList(Category.CAT, Category.BOOK, Category.FRAME,
                Category.GAME, Category.TROPHY, Category.PLANT);
        for(int i=0;i<5;i++){
            for(int j=0;j<4;j++){
                //if the item in the x centre is not null
                if(grid[i][j].getCategoryType() != null){
                    //if the x is form by element of the same category
                    if(grid[i][j].getCategoryType() == grid[i][j+1].getCategoryType() &&
                            grid[i][j].getCategoryType() == grid[i+1][j].getCategoryType() &&
                            grid[i][j].getCategoryType() == grid[i+1][j+1].getCategoryType()){
                        //extract the position of the category relative counter in categoryReference from list
                        int pos = list.indexOf(grid[i][j].getCategoryType());
                        categoryReference[pos] = categoryReference[pos] + 1;
                    }
                }
            }
        }
        //if one of the occurrences is at least two return true, else return false
        for (int numberOfOccurrences : categoryReference)
            if (numberOfOccurrences == 2) return true;
        return false;
    }


    /**
     * Method that returns a true boolean if there are 8 instances of the same category in the Player's Shelf
     * @param grid
     */
    private boolean groupOfEight(Item[][] grid) {
        int catCounter = 0;
        int bookCounter = 0;
        int gameCounter = 0;
        int frameCounter = 0;
        int trophyCounter = 0;
        int plantCounter = 0;
        for(int i=0; i<6; i++){
            for(int j=0; j<5; j++){
                if(grid[i][j].getCategoryType() == Category.CAT){
                    catCounter++;
                }
                if(grid[i][j].getCategoryType() == Category.BOOK){
                    bookCounter++;
                }
                if(grid[i][j].getCategoryType() == Category.GAME){
                    gameCounter++;
                }
                if(grid[i][j].getCategoryType() == Category.FRAME){
                    frameCounter++;
                }
                if(grid[i][j].getCategoryType() == Category.TROPHY){
                    trophyCounter++;
                }
                if(grid[i][j].getCategoryType() == Category.PLANT){
                    plantCounter++;
                }
                if(catCounter >= 8 || bookCounter >= 8 || gameCounter >= 8 || frameCounter >= 8 || trophyCounter >= 8 || plantCounter >= 8){
                    return true;
                }
            }
        }
        return false;
    }

/**
 * Recursive method used in conjunction to groupOfFour to increase the counter in case there's an
 * adjacent item with the same category
 * @param shelf the current player's shelf
 * @param visited a matrix of booleans to check if the slot has already been visited
 * @param category the category of the previous item checked
 * @param i the row of the previous item checked
 * @param j the column of the previous item checked
 * @param groupDimCounter a counter that increases if the conditions are met (dimension of the current group)
 * @param groupsFound a counter that increases if a group of four dimension has been found
 * @param groupFlag a flag that stops the counter from increasing unnecessarily during the recursive calling
 * */
    private void adjacentCategoryCheck(Item[][] shelf, boolean[][] visited, Category category, int i, int j,
                                       int groupDimCounter, List<Integer> groupsFound, List<Boolean> groupFlag){
        if(groupDimCounter == 4 && !groupFlag.get(0)){
            groupsFound.add(groupsFound.remove(0) + 1);
            groupFlag.clear();
            groupFlag.add(true);
            return;
        }
        // stops the method if the item checked is out of the Shelf's bounds, has already been visited
        // or isn't of the same category as the previous one checked
        if     (i<0 || i>=shelf.length ||
                j<0|| j>=shelf[i].length ||
                visited[i][j] ||
                shelf[i][j].getCategoryType() != category){
            return;
        }
        visited[i][j] = true;  //
        // counter is incremented by 1
        groupDimCounter++;
        // calls the same method on the adjacent tiles
        adjacentCategoryCheck(shelf,visited,category,i-1,j,groupDimCounter, groupsFound, groupFlag);
        adjacentCategoryCheck(shelf,visited,category,i+1,j,groupDimCounter, groupsFound, groupFlag);
        adjacentCategoryCheck(shelf,visited,category,i,j-1,groupDimCounter, groupsFound, groupFlag);
        adjacentCategoryCheck(shelf,visited,category,i,j+1,groupDimCounter, groupsFound, groupFlag);
    }
}
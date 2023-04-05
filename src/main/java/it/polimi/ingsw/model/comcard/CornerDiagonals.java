package it.polimi.ingsw.model.comcard;

import it.polimi.ingsw.model.Category;
import it.polimi.ingsw.model.Item;

import java.io.Serializable;

class CornerDiagonals extends StrategyCheck implements Serializable {
    /* ATTRIBUTES SECTION */
    //private final int type;
    /* METHODS SECTION */

    /* -- constructor -- */
    public CornerDiagonals(int type){
        this.type = type;
    }

    /* -- logic methods --*/

    /**
     * Method checker calls three different type of methods that verified the subsists of a certain condition
     * in the player shelf
     */
    @Override
    public boolean check(Item[][] grid) {
        switch (this.type) {
            case 2 -> {
                return diagonals(grid);
            }
            case 3 -> {
                return corners(grid);
            }
            case 12 -> {
                return descMatrix(grid);
            }
            default -> {
                return false;
            }
        }
    }

    /**
     * Method diagonals controls if the conditions express in the second common objective card subsists.
     */
    public boolean diagonals(Item[][] grid) {
        boolean diag1 = true; //first main diagonal referencer
        boolean diag2 = true; //second main diagonal referencer
        boolean diag3 = true; //first opposite diagonal referencer
        boolean diag4 = true; //secondo opposite diagonal referencer
        /* Main diagonals check */
        Category c1 = grid[0][0].getCategoryType(); //first element of diag1
        Category c2 = grid[1][0].getCategoryType(); //first element of diag2
        for (int i = 1; i < 5; i++) {
            if (c1 != null) {
                //if c1 is not null, must be control the rest of the diagonal
                if (grid[i][i].getCategoryType() != c1 || grid[i][i].getCategoryType() == null) {
                    diag1 = false;
                }
            } else {
                diag1 = false;
            }
            if (c2 != null) {
                //if c2 is not null, must be control the rest of the diagonal
                if (grid[i + 1][i].getCategoryType() != c2 || grid[i + 1][i].getCategoryType() == null) {
                    diag2 = false;
                }
            } else {
                diag2 = false;
            }
        }

        /* Opposite diagonals check */
        Category c3 = grid[0][4].getCategoryType(); //first element of diag3
        Category c4 = grid[1][4].getCategoryType(); //first element of diag4
        for (int i = 1; i < 5; i++) {
            if (c3 != null) {
                //if c4 is not null, must be control the rest of the diagonal
                if (grid[i][4-i].getCategoryType() != c3 || grid[i][4-i].getCategoryType() == null) {
                    diag3 = false;
                }
            } else {
                diag3 = false;
            }
            if (c4 != null) {
                //if c4 is not null, must be control the rest of the diagonal
                if (grid[i + 1][4-i].getCategoryType() != c4 || grid[i + 1][4-i].getCategoryType() == null) {
                    diag4 = false;
                }
            } else {
                diag4 = false;
            }
        }

        /* If one of the diagonal's referencer is true, the goal is reach. True must be return */
        return diag1 || diag2 || diag3 || diag4;
    }


    /**
     * Method corners controls if the conditions express in the third common objective card subsists.
     */
    public boolean corners(Item[][] grid) {
        if (grid[0][0].getCategoryType() != null) {
            //In case the top left corner is not null, then must be control the other three corners
            return grid[0][0].getCategoryType() == grid[0][4].getCategoryType() && //top right corner
                    grid[0][0].getCategoryType() == grid[5][0].getCategoryType() && //bottom left corner
                    grid[0][0].getCategoryType() == grid[5][4].getCategoryType(); //bottom right
        }
        //if the return statement is not taken then the goal is not reach. False must be return.
        return false;
    }

    public boolean descMatrix(Item[][] grid) {
        boolean inc = true;
        boolean dec = true;

        for(int i=0; i<5; i++){
            if(grid[i][i].getCategoryType()!=null || grid[i+1][i].getCategoryType()==null){
                dec = false;
            }
        }

        for(int i=0; i<5; i++){
            if(grid[i][4-i].getCategoryType() != null || grid[i+1][4-i].getCategoryType() == null){
                inc = false;
            }
        }
        if(dec == true || inc == true){
            return true;
        }
        return false;

    }
}

package it.polimi.ingsw.model.comcard;

import it.polimi.ingsw.model.Category;
import it.polimi.ingsw.model.Item;

class CornerDiagonals implements StrategyCheck {
    /* METHODS SECTION */

    /* -- logic methods --*/

    /**
     * Method checker calls three different type of methods that verified the subsists of a certain condition
     * in the player shelf
     */
    @Override
    public boolean check(Item[][] grid, int status) {
        switch (status) {
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
        /* Main diagonal check */
        Category c1 = grid[0][0].getCategoryType(); //first element of diag1
        Category c2 = grid[1][0].getCategoryType(); //first element of diag2
        for (int i = 1; i < 5; i++) {
            for (int j = 1; j < 5; j++) {
                if (c1 != null) {
                    //if c1 is not null, must be control the rest of the diagonal
                    if (grid[i][j].getCategoryType() != c1) {
                        diag1 = false;
                    }
                }
                if (c2 != null) {
                    //if c2 is not null, must be control the rest of the diagonal
                    if (grid[i + 1][j].getCategoryType() != c2) {
                        diag2 = false;
                    }
                }
            }
        }

        /* Opposite diagonal check */
        Category c3 = grid[0][4].getCategoryType(); //first element of diag3
        Category c4 = grid[1][4].getCategoryType(); //first element of diag4
        for (int i = 1; i < 5; i++) {
            for (int j = 3; j >= 0; j--) {
                if (c3 != null) {
                    //if c4 is not null, must be control the rest of the diagonal
                    if (grid[i][j].getCategoryType() != c3) {
                        diag3 = false;
                    }
                }
                if (c4 != null) {
                    //if c4 is not null, must be control the rest of the diagonal
                    if (grid[i + 1][j].getCategoryType() != c4) {
                        diag4 = false;
                    }
                }
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

    public boolean descMatrix(Player player) {
        Item[][] grid = player.getMyShelf().GetShelfGrid();
        boolean inc = true;
        boolean dec = true;

        for(int i=0; i<5; i++){
            if(grid[i][i].getCategoryType()!=null || grid[i][i+1].getCategoryType()==null){
                dec = false;
            }
        }

        for(int i=0, j=4; i<5 && j>=0; i++, j-- ){
            if(grid[i][j].getCategoryType() != null || grid[i+1][j].getCategoryType() == null){
                inc = false;
            }
        }
        if(dec == true || inc == true){
            return true;
        }
        return false;

    }
}

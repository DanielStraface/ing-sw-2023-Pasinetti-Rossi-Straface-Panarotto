package it.polimi.ingsw.model.comcard;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.Category;

import java.util.HashSet;

class ThreeDifferentTypes implements StrategyCheck {

    @Override
    public boolean check(Item[][] grid, int status) {
        switch (status) {
            case 4 -> {
                return rowsDifferentTypes(grid);
            }
            case 9 -> {
                return columnsDifferentTypes(grid);
            }
            default -> {
                return false;
            }
        }
    }

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
            if (uniqueObjects.size() <= 3 && invalidColumn == false) {
                columnCounter++;
            }
            invalidColumn = false;
        }
        if(columnCounter>=3){
            return true;
        }
        return false;
    }
}

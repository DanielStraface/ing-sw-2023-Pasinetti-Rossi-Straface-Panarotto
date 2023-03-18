package it.polimi.ingsw.model.comcard;

import it.polimi.ingsw.model.Category;
import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.Player;

import java.util.HashSet;

class ThreeDifferentTypes implements StrategyCheck {

    @Override
    public boolean check(Player player, int status) {
        boolean result = false;
        switch (status) {
            case 4 -> result = rowsDifferentTypes();
            case 9 -> result = columnsDifferentTypes();
            default -> {
                return false;
            }
        }
        return true;
    }

    private boolean rowsDifferentTypes(Player player) {
        Item[][] grid = player.getMyShelf().GetShelfGrid();
        int rowCounter = 0;
        for(int i=0; i<6; i++) {
            HashSet<Category> uniqueObjects = new HashSet<Category>();
            for (int j = 0; j < 5; j++) {
                if (grid[i][j].getCategoryType() == null) {
                    j = 5;
                }
                else {
                    Category scanned = grid[i][j].getCategoryType();
                    uniqueObjects.add(scanned);
                }
            }
            if (uniqueObjects.size() <= 3) {
                rowCounter++;
            }
        }
        if(rowCounter>=4){
            return true;
        }
        return false;
    }



    private boolean columnsDifferentTypes() {
        return false;
    }
}
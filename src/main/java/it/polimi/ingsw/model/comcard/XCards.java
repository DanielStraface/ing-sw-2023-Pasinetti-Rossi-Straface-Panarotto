package it.polimi.ingsw.model.comcard;

import it.polimi.ingsw.model.Category;
import it.polimi.ingsw.model.Item;

class XCards implements StrategyCheck {

    @Override
    public boolean check(Item[][] grid, int status) {
        assert status == 12;
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

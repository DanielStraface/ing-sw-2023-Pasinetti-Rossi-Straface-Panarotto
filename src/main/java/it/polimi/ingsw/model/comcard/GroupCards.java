package it.polimi.ingsw.model.comcard;

import it.polimi.ingsw.model.Category;
import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.Player;

class GroupCards implements StrategyCheck {

    @Override
    public boolean check(Player player, int status) {
        boolean result = false;
        switch (status) {
            case 1 -> result = groupOfTwo();
            case 5 -> result = groupOfFour();
            case 7 -> result = groupOfSquares();
            case 11 -> result = groupOfEight(player);
            default -> {
                return false;
            }
        }
        return true;
    }

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
        for(int i=0; i<6; i++){
            if(!check[i][4] && grid[i][4].getCategoryType()!=null &&
                    !check[i+1][4] && grid[i+1][4].getCategoryType() == grid[i][5].getCategoryType()){
                groups++;
            }
        }
        for(int j=0; j<5; j++){
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

    private boolean groupOfFour() {
        return true;
    }

    private boolean groupOfSquares() {
        return true;
    }

    private boolean groupOfEight(Player player) {
        Item[][] grid = player.getMyShelf().GetShelfGrid();
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
                if(catCounter==8 || bookCounter==8 || gameCounter==8 || frameCounter==8 || trophyCounter==8 || plantCounter==8){
                    return true;
                }
            }
        }
        return false;
    }

}

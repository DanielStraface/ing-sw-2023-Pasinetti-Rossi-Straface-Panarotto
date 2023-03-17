package it.polimi.ingsw.model;

class GroupCards extends CommonObjCard {
    public GroupCards(int numberOfPlayers, int status) {
        super(numberOfPlayers);
        this.status = status;
    }

    public boolean check(Player player) {
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

    private boolean groupOfTwo() {
        return true;
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

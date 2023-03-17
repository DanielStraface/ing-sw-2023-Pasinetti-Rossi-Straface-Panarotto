package it.polimi.ingsw.model;

class RowsColumnsCard extends CommonObjCard {
    public RowsColumnsCard(int numberOfPlayers, int status) {
        super(numberOfPlayers);
        this.status = status;
    }

    public boolean checker(Player player) {
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

    private boolean rowsChecker(Player player) {
        Item[][] grid = player.getMyShelf().GetShelfGrid();
        int[] categoryCounter = new int[6];
        int rowsCounter = 0;

        for(int i=0;i<6;i++){
            int columnsCounter = 0;
            int zeroCounter = 0;
            for(int j=1;j<5;j++){
                if(grid[i][j].getCategoryType() == Category.CAT){categoryCounter[0]++;}
                if(grid[i][j].getCategoryType() == Category.BOOK){categoryCounter[1]++;}
                if(grid[i][j].getCategoryType() == Category.GAME){categoryCounter[2]++;}
                if(grid[i][j].getCategoryType() == Category.FRAME){categoryCounter[3]++;}
                if(grid[i][j].getCategoryType() == Category.TROPHY){categoryCounter[4]++;}
                if(grid[i][j].getCategoryType() == Category.PLANT){categoryCounter[5]++;}
            }
            for(int k=0;k<6;k++){
                if(categoryCounter[k] == 1){
                    columnsCounter++;
                }
                if(categoryCounter[k] == 0){
                    zeroCounter++;
                }
            }
            if(columnsCounter == 5 && zeroCounter == 1){
                rowsCounter++;
            }
            if(rowsCounter >= 2){
                return true;
            }
        }
        return false;
    }

    private boolean columnsChecker(Player player) {
        Item[][] grid = player.getMyShelf().GetShelfGrid();
        int[] categoryCounter = new int[6];
        int columnsCounter = 0;

        for(int j=0;j<5;j++) {
            int rowsCounter = 0;
            for (int i = 1; i < 6; i++) {
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
                if (categoryCounter[k] == 1) {
                    rowsCounter++;
                }
            }
            if (rowsCounter == 6) {
                columnsCounter++;
            }
            if(columnsCounter >= 2){
                return true;
            }
        }
        return false;
    }
}
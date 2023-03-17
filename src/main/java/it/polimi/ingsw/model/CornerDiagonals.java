package it.polimi.ingsw.model;

class CornerDiagonals extends CommonObjCard {
    public CornerDiagonals(int numberOfPlayers, int status) {
        super(numberOfPlayers);
        this.status = status;
    }

    public boolean checker(Player player) {
        boolean result = false;
        switch (status) {
            case 2 -> result = diagonals(player);
            case 3 -> result = corners(player);
            case 12 -> result = descMatrix(player);
            default -> {
                return false;
            }
        }
        return true;
    }

    public boolean diagonals(Player player) {
        Item[][] grid = player.getMyShelf().GetShelfGrid();
        boolean diag1 = true;
        boolean diag2 = true;
        boolean diag3 = true;
        boolean diag4 = true;
        // 00 11 22 33 44 +1 +1
        // 10 21 32 43 54 +1 +1
        // 04 13 22 31 40 +1 -1
        // 14 23 32 41 50 +1 -1
        Category c1 = grid[0][0].getCategoryType();
        Category c2 = grid[1][0].getCategoryType();
        for (int i = 1; i < 5; i++) {
            for (int j = 1; j < 5; j++) {
                if (c1 != null) {
                    if (grid[i][j].getCategoryType() != c1) {
                        diag1 = false;
                    }
                }
                if (c2 != null) {
                    if (grid[i + 1][j].getCategoryType() != c2) {
                        diag2 = false;
                    }
                }
            }
        }

        Category c3 = grid[0][4].getCategoryType();
        Category c4 = grid[1][4].getCategoryType();
        for (int i = 1; i < 5; i++) {
            for (int j = 3; j >= 0; j--) {
                if (c3 != null) {
                    if (grid[i][j].getCategoryType() != c3) {
                        diag3 = false;
                    }
                }
                if (c4 != null) {
                    if (grid[i + 1][j].getCategoryType() != c4) {
                        diag4 = false;
                    }
                }
            }
        }

        if (diag1 == true || diag2 == true || diag3 == true || diag4 == true) {
            return true;
        }
        return false;
    }


    public boolean corners(Player player) {
        Item[][] grid = player.getMyShelf().GetShelfGrid();
        if (grid[0][0].getCategoryType() != null) {
            if (grid[0][0].getCategoryType() == grid[0][4].getCategoryType() &&
                    grid[0][0].getCategoryType() == grid[5][0].getCategoryType() &&
                    grid[0][0].getCategoryType() == grid[5][4].getCategoryType()) {
                return true;
            }
        }
        return false;
    }

    public boolean descMatrix(Player player) {
        return false;
    }
}

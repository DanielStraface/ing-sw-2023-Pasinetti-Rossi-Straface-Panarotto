package it.polimi.ingsw.model.comcard;

import it.polimi.ingsw.model.Item;

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

    private boolean rowsDifferentTypes(Item[][] grid) {return false;}

    private boolean columnsDifferentTypes(Item[][] grid) {
        return false;
    }
}

package it.polimi.ingsw.model.comcard;

import it.polimi.ingsw.model.Player;

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

    private boolean rowsDifferentTypes() {
        return false;
    }

    private boolean columnsDifferentTypes() {
        return false;
    }
}

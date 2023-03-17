package it.polimi.ingsw.model;

class ThreeDifferentTypes extends CommonObjCard {
    public ThreeDifferentTypes(int numberOfPlayers, int status) {
        super(numberOfPlayers);
        this.status = status;
    }

    public boolean checker() {
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

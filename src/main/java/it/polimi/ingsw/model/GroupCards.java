package it.polimi.ingsw.model;

class GroupCards extends CommonObjCard {
    public GroupCards(int numberOfPlayers, int status) {
        super(numberOfPlayers);
        this.status = status;
    }

    public boolean check() {
        boolean result = false;
        switch (status) {
            case 1 -> result = groupOfTwo();
            case 5 -> result = groupOfFour();
            case 7 -> result = groupOfSquares();
            case 11 -> result = groupOfEight();
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

    private boolean groupOfEight() {
        return true;
    }

}

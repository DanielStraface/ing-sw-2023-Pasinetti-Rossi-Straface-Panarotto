package it.polimi.ingsw.model;

class RowsColumnsCard extends CommonObjCard {
    public RowsColumnsCard(int numberOfPlayers, int status) {
        super(numberOfPlayers);
        this.status = status;
    }

    public boolean checker() {
        boolean result = false;
        switch (status) {
            case 6 -> result = rowsChecker();
            case 8 -> result = columnsChecker();
            default -> {
                return false;
            }
        }
        return true;
    }

    private boolean rowsChecker() {
        return false;
    }

    private boolean columnsChecker() {
        return false;
    }
}

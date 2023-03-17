package it.polimi.ingsw.model;

class XCards extends CommonObjCard {
    public XCards(int numberOfPlayers, int status) {
        super(numberOfPlayers);
        this.status = status;
    }

    public boolean xChecker() {
        return true;
    }
}

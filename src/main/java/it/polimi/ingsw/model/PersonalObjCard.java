package it.polimi.ingsw.model;

class PersonalObjCard {

    /* ATTRIBUTES SECTIONS */
    private String personalObjCardDescription; // The name of the personalObjCard
    private Item[][] cardGrid; // This provides the position of item needed to score points

    /* METHODS SECTION */

    /**
     * Method getPersonalObjCardDescription returns a string with the description of this card.
     * -- Maybe this method must be edit in function of JSON loading strings
     */
    public String getPersonalObjCardDescription() {
        return personalObjCardDescription;
    }

    /**
     * Method goalReached returns true if there is a match between the item position in the player shelf and in the
     * template grid of the card. This grid is load from a Json file.
     */
    public boolean goalReached(Shelf shelf) {
        /* Two for cycle to analyse both matrix in player shelf and template grid */
        for (int i = 0; i < cardGrid.length; i++) {
            for (int j = 0; j < cardGrid[i].length; j++) {
                /* if the position in the template grid is not null there is an item type */
                if (cardGrid[i][j] != null) {
                    /* If there is at least one type of item that is not equal to the item in the player shelf
                    *  the goal is not reached. The method returns false */
                    if (cardGrid[i][i].getCategoryType() != shelf.GetShelfGrid()[i][j].getCategoryType()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
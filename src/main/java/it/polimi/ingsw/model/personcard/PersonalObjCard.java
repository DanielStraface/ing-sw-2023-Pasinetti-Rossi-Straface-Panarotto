package it.polimi.ingsw.model.personcard;

import it.polimi.ingsw.exceptions.InvalidMatchesException;
import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.Shelf;

import java.io.Serializable;

/**
 * This class is a representation of the PersonalObjCard entity. With one attribute cardGrid, it stored the goal that
 * the player must reach in his own shelf. The class has got a get method to return the cardGrid attribute and
 * a logic method (shelfCheck) that controls if the goal is reached.
 * @method getCardGrid(), shelfCheck()
 * @author Matteo Panarotto
 */

public class PersonalObjCard implements Serializable {

    /* ATTRIBUTES SECTIONS */
    private static final int SHELF_ROWS=6;
    private static final int SHELF_COLUMS=5;
    private String personalObjCardDescription; // The name of the personalObjCard, initially store in JSON file
    private Item[][] cardGrid = new Item[SHELF_ROWS][SHELF_COLUMS]; // This provides the position of item needed to score points, initially store in JSON file

    /* METHODS SECTION */

    /* -- get methods -- */
    /**
     * Method getPersonalObjCardDescription returns a string with the description of this card.
     * @return this.personalObjCardDescription
     * @author Matteo Panarotto
     */
    public String getPersonalObjCardDescription() {
        return this.personalObjCardDescription;
    }

    /**
     * Method getCardGrid returns a reference to cardGrid.
     * @return this.cardGrid
     * @author Matteo Panarotto
     */
    public Item[][] getCardGrid(){
        return this.cardGrid;
    }

    /* -- logic methods -- */
    /**
     * Method goalReached returns true if there is a match between the item position in the player shelf and in the
     * template grid of the card. This grid was loaded from a Json file.
     * @return The corresponding points of i, and i.equals( the count of :
     *                              ((Item I1 in the (x,y) position in cardGrid template && I1 != null) ==>
     *                              (exists Item I2 in the (w,z) position in player's shelf with
     *                              x == w && y == z && I1.getCategoryType() == I2.getCategoryType())))
     * @author Matteo Panarotto
     */
    public int shelfCheck(Shelf shelf) throws InvalidMatchesException {
        int matches = 0;
        int scoreAdded;
        /* Two for-cycle to analyse both matrix in player shelf and template grid */
        for (int i = 0; i < cardGrid.length; i++) {
            for (int j = 0; j < cardGrid[i].length; j++) {
                /* if the position in the template grid and player's shelf is not null and the item categories
                *  are matching the "match" integer is increased */
                if (cardGrid[i][j].getCategoryType() != null && shelf.getShelfGrid()[i][j].getCategoryType() != null
                    && cardGrid[i][j].getCategoryType() == shelf.getShelfGrid()[i][j].getCategoryType()) {
                    matches++;
                }
            }
        }
        // the score added to the player is set depending on the number of matches
        switch (matches){
            case 1 -> {
                scoreAdded = 1;
            }
            case 2 -> {
                scoreAdded = 2;
            }
            case 3 -> {
                scoreAdded = 4;
            }
            case 4 -> {
                scoreAdded = 6;
            }
            case 5 -> {
                scoreAdded = 9;
            }
            case 6 -> {
                scoreAdded = 12;
            }
            default -> {
                scoreAdded = 0;
            }
        }
        if(matches > 6){
            throw new InvalidMatchesException();
        }
        return scoreAdded;
    }

}

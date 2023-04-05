package it.polimi.ingsw.exceptions;

/**
 * class InvalidMatchesException is thrown if the number of matches between the item position in the
 * player shelf and in the template grid of the personal object card is more than 6.
 */
public class InvalidMatchesException extends Exception {
    /* Method getMessage() returns the message of this exception */

    @Override
    public String getMessage(){
        return "There can't be more than 6 matches!";
    }
}

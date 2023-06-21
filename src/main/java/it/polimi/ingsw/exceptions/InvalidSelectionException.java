package it.polimi.ingsw.exceptions;

/**
 * Class InvalidSelectionException is thrown when the Items drawn by the player:
 * -are not in the same column or in the same row on the game board;
 * -are not one after the other on the game board;
 * -have not almost a free side on the game board.
 */
public class InvalidSelectionException extends Exception {
    /**
     * Constructor for InvalidSelectionException class with the specific message of the error.
     * @param message the specific message of the error.
     */
    public InvalidSelectionException(String message){
        super(message);
    }

}

package it.polimi.ingsw.exceptions;

/**
 * class NoConsecutiveSelectionException is thrown when two non-consecutive items are drawn from the game board.
 */
public class NoConsecutiveSelectionException extends InvalidSelectionException{
    /**
     * Constructor for NoConsecutiveSelectionException class with the specific message of the error.
     * @param message the specific message of the error.
     */
    public NoConsecutiveSelectionException(String message) {
        super(message);
    }
}

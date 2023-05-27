package it.polimi.ingsw.exceptions;

/**
 * CLass SelectionInvalidOrEmptySlotException is thrown when items selected from the game board are taken
 * from empty o invalid slot.
 */
public class SelectionInvalidOrEmptySlotException extends InvalidSelectionException{
    /**
     * Constructor forElectionInvalidOrEmptySlotException class with the specific message of the error.
     * @param message the specific message of the error.
     */
    public SelectionInvalidOrEmptySlotException(String message) {
        super(message);
    }
}

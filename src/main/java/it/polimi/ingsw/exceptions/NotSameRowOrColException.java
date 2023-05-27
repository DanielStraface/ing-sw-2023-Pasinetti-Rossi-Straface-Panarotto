package it.polimi.ingsw.exceptions;

/**
 * Class NotSameRowOrColException is thrown when the selected items are nt form the same row or column:
 */
public class NotSameRowOrColException extends InvalidSelectionException{
    /**
     * Constructor for NotSmeRowOrColException class with the specific message of the error.
     * @param message the specific message of the error.
     */
    public NotSameRowOrColException(String message) {
        super(message);
    }
}

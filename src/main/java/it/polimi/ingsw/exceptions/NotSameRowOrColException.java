package it.polimi.ingsw.exceptions;

/**
 * Class NotSameRowOrColException is thrown when the selected items aren't from the same row or column:
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

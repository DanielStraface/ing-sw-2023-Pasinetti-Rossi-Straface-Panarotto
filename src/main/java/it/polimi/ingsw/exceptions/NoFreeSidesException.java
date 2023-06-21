package it.polimi.ingsw.exceptions;

/**
 * Class NoFreeSidesException is thrown when the item selected from the game board has no free sides.
 */
public class NoFreeSidesException extends InvalidSelectionException{
    /**
     * Constructor for NoFreeSidesException class with the specific message of the error.
     * @param message the specific message of the error.
     */
    public NoFreeSidesException(String message) {
        super(message);
    }
}

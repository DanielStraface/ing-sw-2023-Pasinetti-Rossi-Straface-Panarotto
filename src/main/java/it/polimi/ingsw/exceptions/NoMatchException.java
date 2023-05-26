package it.polimi.ingsw.exceptions;

/**
 * Class NoMatchException is thrown when there aren't existing matches to join.
 */
public class NoMatchException extends NotSupportedMatchesException{
    /**
     * Constructor for NoMatchException class with the specific message of the error.
     */
    public NoMatchException() {
        super("There are no existing matches to join now");
    }
}

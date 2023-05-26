package it.polimi.ingsw.exceptions;

/**
 * class NotSupportedMatchesException is thrown when there is an unsupported number of matches.
 */
public class NotSupportedMatchesException extends Exception{
    /**
     * Constructor for NotSupportedMatchesException class with the specific message of the error.
     * @param msg the specific message of the error.
     */
    public NotSupportedMatchesException(String msg){
        super(msg);
    }
}

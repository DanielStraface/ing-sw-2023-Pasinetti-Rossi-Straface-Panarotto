package it.polimi.ingsw.exceptions;
/**
 * class NotSupportedMatchesException is thrown when the server has too many matches to manage.
 */
public class TooManyMatchesException extends NotSupportedMatchesException{
    /**
     * Constructor for NotSupportedMatchesException class with the specific message of the error.
     */
    public TooManyMatchesException(){
        super("Too many matches managed by this server now! Try later");
    }
}

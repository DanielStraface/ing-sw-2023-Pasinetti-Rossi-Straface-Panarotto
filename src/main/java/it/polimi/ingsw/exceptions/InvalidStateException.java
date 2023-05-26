package it.polimi.ingsw.exceptions;

/**
 * Class InvalidStateException class is thrown as IllegalStateException.
 */
public class InvalidStateException extends Exception{
    /**
     * Constructor for InvalidStateException class with the specific message of the error.
     * @param message the specific message of the error.
     */
    public InvalidStateException(String message){super(message);}
}

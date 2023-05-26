package it.polimi.ingsw.exceptions;

/**
 * Class InvalidPointerException is thrown as NullPointerException.
 */
public class InvalidPointerException extends Exception{
    /**
     * Constructor for InvalidPointerException class with the specific message of the error.
     * @param message the specific message of the error.
     */
    public InvalidPointerException(String message){super(message);}
}

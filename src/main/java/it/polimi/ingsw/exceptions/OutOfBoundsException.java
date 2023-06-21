package it.polimi.ingsw.exceptions;

/**
 * Class OutOfBoundsException is thrown as ArrayOutOfBoundsException.
 */
public class OutOfBoundsException extends Exception{
    /**
     * Constructor for OutOfBoundException class with the specific message of the error.
     * @param message the specific message of the error.
     */
    public OutOfBoundsException(String message){super(message);}
}

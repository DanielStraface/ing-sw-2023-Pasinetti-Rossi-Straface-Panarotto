package it.polimi.ingsw.exceptions;

/**
 * Class NoElementException is thrown as NoSuchElementException.
 */

public class NoElementException extends Exception{
    /**
     * Constructor for NoElementException class with the specific message of the error.
     * @param message the specific message of the error.
     */
    public NoElementException(String message){super(message);}

}

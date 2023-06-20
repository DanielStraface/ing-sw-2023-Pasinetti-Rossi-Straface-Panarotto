package it.polimi.ingsw.exceptions;

/**
 * Class FullColumnException is thrown when the shelf column is full.
 */
public class FullColumnException extends Exception{
    /**
     * Constructor for FullColumnException class with the specific message of the error.
     */
    public FullColumnException(){
        super("Hey! This column is full. Choose another one");
    }
}

package it.polimi.ingsw.exceptions;

/**
 * WaitingForEventSocketException represent the act of waiting for an event, such as at the start of the game.
 */
public class WaitingForEventSocketException extends Exception {

    /**
     * Constructor WaitingForEventSocketException class with the specific message of the error.
     * @param message the specific message of the error.
     */
    public WaitingForEventSocketException(String message){
        super(message);
    }
}

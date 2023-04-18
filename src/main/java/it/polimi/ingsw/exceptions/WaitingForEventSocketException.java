package it.polimi.ingsw.exceptions;

/**
 * WaitingForEventSocketException represent the act of waiting for an event, such as at the start of the game.
 */
public class WaitingForEventSocketException extends Exception {
    public WaitingForEventSocketException(String message){
        super(message);
    }
}

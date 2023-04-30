package it.polimi.ingsw.exceptions;

/**
 * This exception will be used for the 2FA (persistence) and is not used yet.
 */
public class NoSavingPointException extends Exception{
    public NoSavingPointException(String message){
        super(message);
    }
}

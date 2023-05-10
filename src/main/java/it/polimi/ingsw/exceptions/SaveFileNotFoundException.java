package it.polimi.ingsw.exceptions;

/**
 * This exception will be used for the 2FA (persistence) and is not used yet.
 */
public class SaveFileNotFoundException extends Exception{
    public SaveFileNotFoundException(String fileName){
        super("Save file for this match not found: " + fileName);
    }
}

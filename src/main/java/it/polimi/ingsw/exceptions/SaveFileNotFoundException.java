package it.polimi.ingsw.exceptions;

public class SaveFileNotFoundException extends Exception{
    public SaveFileNotFoundException(String fileName){
        super("Save file for this match not found: " + fileName);
    }
}

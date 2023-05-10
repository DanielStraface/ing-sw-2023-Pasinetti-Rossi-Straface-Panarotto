package it.polimi.ingsw.exceptions;

public class FullColumnException extends Exception{
    public FullColumnException(){
        super("This column is full");
    }
}

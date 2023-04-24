package it.polimi.ingsw.exceptions;

public class TooManyMatchesException extends Exception{
    public TooManyMatchesException(){
        super("Too many matches managed by this server now! Try later");
    }
}

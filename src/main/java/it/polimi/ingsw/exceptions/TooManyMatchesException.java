package it.polimi.ingsw.exceptions;

public class TooManyMatchesException extends Exception{
    public TooManyMatchesException(){
        super("Too many matches on this server");
    }
}

package it.polimi.ingsw.exceptions;

public class EmptyCoordsListException extends InvalidStateException{
    public EmptyCoordsListException(String message) {
        super(message);
    }
}

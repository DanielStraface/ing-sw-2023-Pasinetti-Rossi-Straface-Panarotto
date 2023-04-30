package it.polimi.ingsw.exceptions;

public class NotSameRowOrColException extends InvalidSelectionException{
    public NotSameRowOrColException(String message) {
        super(message);
    }
}

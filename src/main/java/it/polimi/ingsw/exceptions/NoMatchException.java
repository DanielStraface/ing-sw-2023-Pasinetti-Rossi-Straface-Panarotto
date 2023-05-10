package it.polimi.ingsw.exceptions;

public class NoMatchException extends NotSupportedMatchesException{
    public NoMatchException() {
        super("There are no existing matches to join now");
    }
}

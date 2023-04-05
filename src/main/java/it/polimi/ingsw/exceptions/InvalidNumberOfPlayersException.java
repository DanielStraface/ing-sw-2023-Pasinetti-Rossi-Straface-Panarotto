package it.polimi.ingsw.exceptions;

public class InvalidNumberOfPlayersException extends Exception {
    @Override
    public String getMessage(){
        return "Error: the number of players is not allowed!";
    }
}
package it.polimi.ingsw.exceptions;

/**
 * Class InvalidNumberOfPlayersException is thrown when number of players entered is less than two
 * or more than four.
 */
public class InvalidNumberOfPlayersException extends Exception {

    /** Method getMessage() returns the message of this exception
     * @return the message of this InvalidNumberOfPlayersException object.
     */
    @Override
    public String getMessage(){
        return "Error: the number of players is not allowed!";

    }

}

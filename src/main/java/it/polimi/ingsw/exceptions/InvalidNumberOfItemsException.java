package it.polimi.ingsw.exceptions;

/**
 * Class InvalidNumberOfItemsException is thrown when the Items drawn by the player are more than three
 */
public class InvalidNumberOfItemsException extends Exception{
    /* Method getMessage() returns the message of this exception */

    @Override
    public String getMessage(){
        return "Error: Invalid number of Items";
    }

}
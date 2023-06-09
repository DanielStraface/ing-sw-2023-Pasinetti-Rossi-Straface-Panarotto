package it.polimi.ingsw.client.CLI.commands;

import it.polimi.ingsw.exceptions.FullColumnException;
import it.polimi.ingsw.exceptions.InvalidSelectionException;


public interface Command {
    /**
     * Method implemented in all "Command" Classes: it asks the player for a choice and then invokes a check method
     * @throws InvalidSelectionException when the items selection from the game board is not correct
     * @throws FullColumnException  when the shelf column is full
     */
    void execute() throws InvalidSelectionException, FullColumnException;

    /**
     * Method implemented in all "Command" Classes in order to do a check
     * @throws InvalidSelectionException when the items selection is not correct
     * @throws FullColumnException when the shelf column is full
     */
    void check() throws InvalidSelectionException, FullColumnException;
}

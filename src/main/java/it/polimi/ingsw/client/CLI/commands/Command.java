package it.polimi.ingsw.client.CLI.commands;

import it.polimi.ingsw.exceptions.FullColumnException;
import it.polimi.ingsw.exceptions.InvalidSelectionException;


public interface Command {
    /**
     * Method implemented in all "Command" Classes: it asks the player for a choice and then invokes a check method
     * @throws InvalidSelectionException
     * @throws FullColumnException
     */
    void execute() throws InvalidSelectionException, FullColumnException;

    /**
     * Method implemented in all "Command" Classes in order to do a check
     * @throws InvalidSelectionException
     * @throws FullColumnException
     */
    void check() throws InvalidSelectionException, FullColumnException;
}

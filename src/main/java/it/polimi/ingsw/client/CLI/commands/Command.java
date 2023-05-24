package it.polimi.ingsw.client.CLI.commands;

import it.polimi.ingsw.exceptions.FullColumnException;
import it.polimi.ingsw.exceptions.InvalidSelectionException;


public interface Command {
    /**
     * Method implemented in all "Command" Classes
     * @throws InvalidSelectionException
     * @throws FullColumnException
     */
    void execute() throws InvalidSelectionException, FullColumnException;
}

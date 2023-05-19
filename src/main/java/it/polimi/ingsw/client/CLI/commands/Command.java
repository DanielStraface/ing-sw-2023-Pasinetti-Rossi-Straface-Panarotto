package it.polimi.ingsw.client.CLI.commands;

import it.polimi.ingsw.exceptions.FullColumnException;
import it.polimi.ingsw.exceptions.InvalidSelectionException;

public interface Command {
    void execute() throws InvalidSelectionException, FullColumnException;
}

package it.polimi.ingsw.view.CLI.commands;

import it.polimi.ingsw.exceptions.FullColumnException;
import it.polimi.ingsw.exceptions.InvalidSelectionException;

public interface Command {
    void execute() throws InvalidSelectionException, FullColumnException;
}

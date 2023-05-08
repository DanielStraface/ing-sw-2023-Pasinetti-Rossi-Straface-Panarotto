package it.polimi.ingsw.view.commands;

import it.polimi.ingsw.exceptions.InvalidSelectionException;

public interface Command {
    void execute() throws InvalidSelectionException;
}

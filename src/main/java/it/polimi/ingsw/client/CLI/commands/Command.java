package it.polimi.ingsw.client.CLI.commands;

import it.polimi.ingsw.exceptions.*;


/**
 * The Command interface represents an implementation of command design pattern.
 * It manages the user decision:
 *  - column selection -> SelectColumnCommand
 *  - items selection -> SelectItemsCommand
 *  - order selection -> SelectOrderCommand
 */
public interface Command {
    /**
     * Method implemented in all "Command" Classes: it asks the player for a choice and then invokes a check method
     * @throws SelectionInvalidOrEmptySlotException when items taken from the gameBoard are from empty or invalid slots
     * @throws NoFreeSidesException when one of the items selected has no free sides on the gameBoard
     * @throws NotSameRowOrColException when the selected items aren't from the same row or column
     * @throws NoConsecutiveSelectionException when the selected items aren't adjacent
     * @throws FullColumnException when the shelf column is full
     */
    void execute() throws SelectionInvalidOrEmptySlotException,NoFreeSidesException,NotSameRowOrColException,NoConsecutiveSelectionException,FullColumnException;

    /**
     * Method implemented in all "Command" Classes in order to do a check
     * @throws SelectionInvalidOrEmptySlotException when items taken from the gameBoard are from empty or invalid slots
     * @throws NoFreeSidesException when one of the items selected has no free sides on the gameBoard
     * @throws NotSameRowOrColException when the selected items aren't from the same row or column
     * @throws NoConsecutiveSelectionException when the selected items aren't adjacent
     * @throws FullColumnException when the shelf column is full
     */
    void check() throws SelectionInvalidOrEmptySlotException,NoFreeSidesException,NotSameRowOrColException,NoConsecutiveSelectionException,FullColumnException;
}

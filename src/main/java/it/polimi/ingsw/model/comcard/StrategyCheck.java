package it.polimi.ingsw.model.comcard;

import it.polimi.ingsw.model.Item;

import java.io.Serializable;

/**
 * The StrategyCheck class is the implementation of the strategy design pattern to the CommonObjCard.
 * It contains the type of the card and the abstract method that permit to check the goal of the card.
 */
public abstract class StrategyCheck implements Serializable {
    protected int type;

    /**
     * method that checks if the condition of the commonObjCard is satisfied
     * @param grid the player's Shelf grid to be checked
     * @return a boolean that is true if the condition is satisfied
     */
    public abstract boolean check(Item[][] grid);

    /**
     * get method
     * @return type
     */
    public int getType(){return this.type;}
}

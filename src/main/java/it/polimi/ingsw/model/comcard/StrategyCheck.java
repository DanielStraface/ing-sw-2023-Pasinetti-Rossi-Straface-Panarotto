package it.polimi.ingsw.model.comcard;

import it.polimi.ingsw.model.Item;

public abstract class StrategyCheck {
    protected int type;

    /**
     * method that checks if the condition of the commonObjCard is satisfied
     * @param grid the player's Shelf grid to be checked
     * @return a boolean that is true if the condition is satisfied
     */
    public abstract boolean check(Item[][] grid);
    public int getType(){return this.type;}
}

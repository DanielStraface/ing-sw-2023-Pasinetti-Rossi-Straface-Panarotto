package it.polimi.ingsw.model.comcard;

import it.polimi.ingsw.model.Item;

public abstract class StrategyCheck {
    protected int type;
    public abstract boolean check(Item[][] grid);
    public int getType(){return this.type;}
}

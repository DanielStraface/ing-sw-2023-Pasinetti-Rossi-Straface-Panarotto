package it.polimi.ingsw.model.comcard;

import it.polimi.ingsw.model.Item;

public interface StrategyCheck {
    public boolean check(Item[][] grid, int status);
}

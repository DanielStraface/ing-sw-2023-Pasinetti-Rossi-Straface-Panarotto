package it.polimi.ingsw.listeners;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Shelf;

public interface PlayerViewListener {
    void update(Player player, Item[][] gameGrid);
    void update(Player player, Shelf shelf);
}

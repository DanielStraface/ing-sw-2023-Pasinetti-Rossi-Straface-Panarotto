package it.polimi.ingsw.listeners;

import it.polimi.ingsw.model.*;

public interface GameListener {
    void update(Game game, GameBoard gb);
    void update(Game game);
    void update(Player player, Integer column);
}
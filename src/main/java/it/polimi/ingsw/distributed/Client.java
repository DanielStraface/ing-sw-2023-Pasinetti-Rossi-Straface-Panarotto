package it.polimi.ingsw.distributed;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.modelview.GameBoardView;
import it.polimi.ingsw.modelview.GameView;
import it.polimi.ingsw.modelview.ShelfView;
import it.polimi.ingsw.view.TextualUI;

import java.util.List;

public interface Client {
    void update(GameBoardView gb);
    void update(GameView game);
    void update(Item[][] gameGrid);
    void update(ShelfView shelf);
    void update(Integer column);
}
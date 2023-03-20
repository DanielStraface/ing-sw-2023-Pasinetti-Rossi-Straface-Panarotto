package it.polimi.ingsw.model;

import java.util.List;

class GameBoard {

    private static final int DIM_GAMEBOARD=9;
    private Item[][] gameGrid = new Item[DIM_GAMEBOARD][DIM_GAMEBOARD];

    /** get methods */
    private Item[][] getGameGrid(){return gameGrid;}

}

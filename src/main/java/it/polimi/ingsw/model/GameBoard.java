package it.polimi.ingsw.model;

import java.io.Serializable;

public class GameBoard implements Serializable {

    private static final int DIM_GAMEBOARD=9;
    private Item[][] gameGrid = new Item[DIM_GAMEBOARD][DIM_GAMEBOARD];

    /** get methods */
    public Item[][] getGameGrid(){return gameGrid;}

}
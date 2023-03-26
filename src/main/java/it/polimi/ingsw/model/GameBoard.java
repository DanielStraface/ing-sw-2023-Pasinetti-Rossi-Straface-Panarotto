package it.polimi.ingsw.model;

public class GameBoard {

    private static final int DIM_GAMEBOARD=9;
    private Item[][] gameGrid = new Item[DIM_GAMEBOARD][DIM_GAMEBOARD];

    /** get methods */
    public Item[][] getGameGrid(){return gameGrid;}

}
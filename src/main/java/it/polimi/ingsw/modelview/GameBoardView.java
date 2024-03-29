package it.polimi.ingsw.modelview;

import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.Item;

import java.io.Serial;
import java.io.Serializable;

/**
 * The immutable view of Gameboard
 */
public class GameBoardView implements Serializable {
    @Serial
    private static final long serialVersionUID = 2L;
    private static final int DIM_GAMEBOARD = 9;
    private final Item[][] gameGrid;
    private final int[][] validGrid;

    /**
     * constructor method
     * @param gb GameBoard
     */
    public GameBoardView(GameBoard gb) {
        this.validGrid = new int[DIM_GAMEBOARD][DIM_GAMEBOARD];
        this.gameGrid = new Item[DIM_GAMEBOARD][DIM_GAMEBOARD];
        for(int i=0;i< DIM_GAMEBOARD;i++){
            for(int j=0;j<DIM_GAMEBOARD;j++){
                this.validGrid[i][j] = gb.getValidGrid()[i][j];
                this.gameGrid[i][j] = new Item(gb.getGameGrid()[i][j].getCategoryType(),
                        gb.getGameGrid()[i][j].getVariant());
            }
        }
    }

    /** get methods
     * @return item[][] -> gameGrid
     */
    public Item[][] getGameGrid(){return gameGrid;}

    /**
     * get method
     * @return int[][] -> validGrid
     */
    public int[][] getValidGrid(){return validGrid;}
}


package it.polimi.ingsw.modelview;

import it.polimi.ingsw.model.Item;

import java.io.Serial;
import java.io.Serializable;

public class GameBoardView implements Serializable {
    @Serial
    private static final long serialVersionUID = 2L;
    private static final int DIM_GAMEBOARD = 9;
    private final Item[][] gameGrid;

    public GameBoardView(Item[][] gameGrid) {
        this.gameGrid = new Item[DIM_GAMEBOARD][DIM_GAMEBOARD];
        for(int i=0;i< DIM_GAMEBOARD;i++){
            for(int j=0;j<DIM_GAMEBOARD;j++){
                this.gameGrid[i][j] = new Item(gameGrid[i][j].getCategoryType());
            }
        }
    }

    /** get methods */
    public Item[][] getGameGrid(){return gameGrid;}
}

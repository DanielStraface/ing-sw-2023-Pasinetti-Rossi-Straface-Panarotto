package it.polimi.ingsw.model.modelview;

import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.Item;

public class GameBoardView {
    private static final int DIM = 9;
    private final Item[][] gameGrid;
    public GameBoardView(GameBoard gameBoard) {
        this.gameGrid = new Item[DIM][DIM];
        for(int i=0;i<DIM;i++){
            for(int j=0;j<DIM;j++){
                this.gameGrid[i][j] = new Item(gameBoard.getGameGrid()[i][j].getCategoryType());
            }
        }
    }
    public Item[][] getGameGrid(){
        return this.gameGrid;
    }
}

package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.InvalidNumberOfPlayersException;

import java.io.Serializable;
import java.rmi.RemoteException;

public class GameBoard implements Serializable {
    // the GameBoard is a 9x9 matrix of Items
    private static final int DIM_GAMEBOARD=9;
    private static final int PLAYABLE = 1;
    private static final int OCCUPIED = 2;
    private int playersNumber;
    private int[][] validGrid = new int[DIM_GAMEBOARD][DIM_GAMEBOARD];
    private Item[][] gameGrid = new Item[DIM_GAMEBOARD][DIM_GAMEBOARD];
    private GameBoard gameboard;
    private Game game;

    public GameBoard(int playersNumber) throws InvalidNumberOfPlayersException, RemoteException {
        if(playersNumber <= 1 || playersNumber >= 5)
            throw new InvalidNumberOfPlayersException();
        this.playersNumber = playersNumber;
        settingValidGrid();
    }

    /**
     * Fills GameBoard slots with Items with random Categories depending on the number of players
     * @throws RemoteException
     */
    private void settingValidGrid() throws RemoteException{
        setGridForTwo(this.validGrid);
        switch(this.playersNumber) {
            case 3 -> {
                validGrid[0][3] = PLAYABLE;
                validGrid[2][2] = PLAYABLE;
                validGrid[2][6] = PLAYABLE;
                validGrid[3][8] = PLAYABLE;
                validGrid[5][0] = PLAYABLE;
                validGrid[6][2] = PLAYABLE;
                validGrid[6][6] = PLAYABLE;
                validGrid[8][5] = PLAYABLE;
            }
            case 4 -> {
                validGrid[0][3] = PLAYABLE;
                validGrid[0][4] = PLAYABLE;
                validGrid[1][5] = PLAYABLE;
                validGrid[2][2] = PLAYABLE;
                validGrid[2][6] = PLAYABLE;
                validGrid[3][1] = PLAYABLE;
                validGrid[3][8] = PLAYABLE;
                validGrid[4][0] = PLAYABLE;
                validGrid[4][8] = PLAYABLE;
                validGrid[5][0] = PLAYABLE;
                validGrid[5][7] = PLAYABLE;
                validGrid[6][2] = PLAYABLE;
                validGrid[6][6] = PLAYABLE;
                validGrid[7][3] = PLAYABLE;
                validGrid[8][4] = PLAYABLE;
                validGrid[8][5] = PLAYABLE;
            }
            default -> {}
        }
    }

    /**
     * method to set the GameGrid for two players
     * 0 = invalid slot
     * 1 = playable slot
     * @param Grid the GameBoard's item matrix
     */
    private void setGridForTwo (int[][] Grid) {
        int i, j;
        for (i = 0; i < 9; i++) {
            for (j = 0; j < 9; j++) {
                if (i==1){
                    if(j>2 && j<5){
                        Grid[i][j] = PLAYABLE;
                    }
                }
                if (i==2 || i==6){
                    if(j>2 && j<6){
                        Grid[i][j] = PLAYABLE;
                    }
                }
                if (i==3){
                    if(j>1 && j<8){
                        Grid[i][j] = PLAYABLE;
                    }
                }
                if (i==4){
                    if(j>0 && j<8){
                        Grid[i][j] = PLAYABLE;
                    }
                }
                if (i==5){
                    if(j>0 && j<7){
                        Grid[i][j] = PLAYABLE;
                    }
                }
                if (i==7){
                    if(j>3 && j<6){
                        Grid[i][j] = PLAYABLE;
                    }
                }
            }
        }
    }
    public void setValidGrid (int[][] validGrid) { this.validGrid = validGrid; }
    // get methods
    public Item[][] getGameGrid(){return gameGrid;}
    public int[][] getValidGrid(){return validGrid;}
}

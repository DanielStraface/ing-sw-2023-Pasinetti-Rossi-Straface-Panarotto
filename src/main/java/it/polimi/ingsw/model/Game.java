package it.polimi.ingsw.model;

import java.util.List;

class Game {

    private int playersNumber;
    private List<Player> players;
    private GameBoard gameboard;
    private int[][] validGrid = new int[9][9];
    private Bag bag;
    private CommonObjCard commonObjCard;
    private Player currentPlayer;

    /** constructor for Game class */
    public Game (int playersNumber){
        this.playersNumber = playersNumber;
        switch(playersNumber){
            case 2 : {
                setGridForTwo(validGrid);
            }
            case 3 : {
                setGridForTwo(validGrid);
                validGrid[0][3] = 1;
                validGrid[2][2] = 1;
                validGrid[2][6] = 1;
                validGrid[3][8] = 1;
                validGrid[5][0] = 1;
                validGrid[6][2] = 1;
                validGrid[6][6] = 1;
                validGrid[8][5] = 1;
            }
            case 4 : {
                setGridForTwo(validGrid);
                validGrid[0][3] = 1;
                validGrid[0][4] = 1;
                validGrid[1][5] = 1;
                validGrid[2][2] = 1;
                validGrid[2][6] = 1;
                validGrid[3][1] = 1;
                validGrid[3][8] = 1;
                validGrid[4][0] = 1;
                validGrid[4][8] = 1;
                validGrid[5][0] = 1;
                validGrid[5][7] = 1;
                validGrid[6][2] = 1;
                validGrid[6][6] = 1;
                validGrid[7][3] = 1;
                validGrid[8][4] = 1;
                validGrid[8][5] = 1;
            }
        }
    }

    /** method to set the GameGrid for two players
     *  0 = invalid slot
     *  1 = playable slot */
    private void setGridForTwo (int[][] Grid) {
        int i, j;
        for (i = 0; i < 9; i++) {
            for (j = 0; j < 9; j++) {
                if (i==1){
                    if(j>2 && j<5){
                        Grid[i][j] = 1;
                    }
                }
                if (i==2 || i==6){
                    if(j>2 && j<6){
                        Grid[i][j] = 1;
                    }
                }
                if (i==3){
                    if(j>1 && j<8){
                        Grid[i][j] = 1;
                    }
                }
                if (i==4){
                    if(j>0 && j<8){
                        Grid[i][j] = 1;
                    }
                }
                if (i==5){
                    if(j>0 && j<7){
                        Grid[i][j] = 1;
                    }
                }
                if (i==7){
                    if(j>3 && j<6){
                        Grid[i][j] = 1;
                    }
                }
            }
        }
    }

    /** get methods */
    public List<Player> getPlayers(){return players;}
    public GameBoard getGameboard(){return gameboard;}
    public CommonObjCard getCommonObjCard(){return commonObjCard;}
    public Bag getBag(){return bag;}
    public Player getCurrentPlayer(){return currentPlayer;}


}
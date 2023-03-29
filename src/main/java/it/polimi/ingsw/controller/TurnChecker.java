package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.comcard.CommonObjCard;


public class TurnChecker {

    private static final int SHELF_ROWS=6;
    private static final int SHELF_COLUMNS=5;
    private static final int OCCUPIED = 2;



    // all checks that have to be done before ending a player's turn
    public boolean manageCheck(Player player, Game game){
        boolean isLastTurn;
        commonObjCardCheck(player, game);
        isLastTurn = player.getMyShelf().isFull();
        refillGameBoardCheck(game);
        return isLastTurn;
    }

    private void commonObjCardCheck(Player player,Game game){
        CommonObjCard commonObjCard;

        // check if the current player has reached the goal for both CommonObjectiveCards in Game
        for(int i=0; i<game.getCommonObjCard().size(); i++){
            commonObjCard = game.getCommonObjCard().get(i);
            commonObjCard.doCheck(player);
        }
    }

    private void refillGameBoardCheck(Game game){
        boolean check = true;
        int[][] validGrid = game.getValidGrid();

        /* check for every row and column (except first and last): if the slot is occupied by an Item,
         *  check if any of its adjacent places are occupied by an Item as well.
         *  If there's a single adjacent item, there's no need to refill the GameBoard and the check is
         *  set to false. */
        for(int i=1; i<8; i++){
            for(int j=1; j<8; j++){
                if(validGrid[i][j] == OCCUPIED){
                    if(validGrid[i-1][j] == OCCUPIED ||
                       validGrid[i][j-1] == OCCUPIED ||
                       validGrid[i][j+1] == OCCUPIED ||
                       validGrid[i+1][j] == OCCUPIED){
                        check = false;
                    }
                }
            }
        }

        /* these last two for cycles are used to check slots 3 and 4 for the first row and last column,
         *  slots 4 and 5 for the first column and last row */
        for(int i=3; i<5; i++){
            if(validGrid[0][i] == OCCUPIED){
                if(validGrid[1][i] == OCCUPIED ||
                   validGrid[0][i+1] == OCCUPIED ||
                   validGrid[0][i-1] == OCCUPIED){
                    check = false;
                }
            }
            if(validGrid[i][8] == OCCUPIED){
                if(validGrid[i][7] == OCCUPIED ||
                   validGrid[i-1][8] == OCCUPIED ||
                   validGrid[i+1][8] == OCCUPIED){
                    check = false;
                }
            }
        }
        for(int i=4; i<6; i++){
            if(validGrid[i][0] == OCCUPIED){
                if(validGrid[i][1] == OCCUPIED ||
                   validGrid[i-1][0] == OCCUPIED ||
                   validGrid[i+1][0] == OCCUPIED){
                    check = false;
                }
            }
            if(validGrid[8][i] == OCCUPIED){
                if(validGrid[7][i] == OCCUPIED ||
                   validGrid[8][i-1] == OCCUPIED ||
                   validGrid[8][i+1] == OCCUPIED){
                    check = false;
                }
            }
        }

        //if no Item has adjacent Items, the GameBoard needs to be refilled
        if(check){
            game.refillGameBoard();
        }
    }


}

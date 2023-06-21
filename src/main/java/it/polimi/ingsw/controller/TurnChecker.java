package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.InvalidPointerException;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.comcard.CommonObjCard;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * The TurnChecker class is responsible for managing checks about CommonObjectiveCards' goal,
 * the number of adjacent items and possibly the need to refill the game bord which have to
 * be performed before ending a player's turn.
 */
public class TurnChecker {
    private static final int OCCUPIED = 2;
    private static final int SHELF_ROWS=6;
    private static final int SHELF_COLUMNS=5;

    /**
     * all checks that have to be done before ending a player's turn
     * @param player the player whose turn in ending
     * @param game Game
     * @return shelfFull <==> boolean to check if the current player's shelf is full
     */
    public boolean manageCheck(Player player, Game game) {
        boolean shelfFull;
        commonObjCardCheck(player, game);
        shelfFull = player.getMyShelf().isFull();
        try{
            refillGameBoardCheck(game);
        } catch (RemoteException e) {
            System.err.println("Error occurred while check the gameboard refill: " + e.getMessage());
            System.err.println("Skipping this problem for now");
        }
        return shelfFull;
    }

    /**
     * checks if the Player currently playing has reached any of the two CommonObjectiveCards' goal
     * after his turn and adds points to its Score tally , throws an InvalidPointerException if
     * all the points of one CommonObjectiveCard have already been taken
     * @param player - the player to be tested for this card
     * @param game - the model (contains the commonObjCard list)
     */
    private void commonObjCardCheck(Player player, Game game) {
        // check if the current player has reached the goal for both CommonObjectiveCards in Game
        for(CommonObjCard commonObjCard : game.getCommonObjCard()){
            try{
                int numOfPoints = commonObjCard.doCheck(player);
                if(numOfPoints != -1){
                    player.addPoints(numOfPoints);
                    CommonObjCard goalCard = game.getCommonObjCard().stream()
                            .filter(card -> card.getType() == commonObjCard.getType())
                            .toList()
                            .get(0);
                    int index = game.getCommonObjCard().indexOf(goalCard);
                    index += 1;
                    game.commonObjCardReached(player ,"Common Objective Card " + index +
                            " goal reached!\n"+ player.getNickname() + " obtains +" + numOfPoints + " points!");
                    try{
                        TimeUnit.SECONDS.sleep(3);
                    } catch (InterruptedException e) {
                        System.err.println("Cannot sleep while commonObjCard: " + e.getMessage());
                    }
                }
            } catch (InvalidPointerException e) {
                System.err.println("Something went wrong, the obj points list of this card is less than zero: "
                        + e.getMessage());
            }
        }
    }

    /**
     * check for every row and column (except first and last): if the slot is occupied by an Item,
     * check if any of its adjacent places are occupied by an Item as well.
     * If there's a single adjacent item, there's no need to refill the GameBoard and the check is
     * set to false.
     * @param game Game
     * @throws RemoteException if the execution of a remote method call goes wrong
     */
    private void refillGameBoardCheck(Game game) throws RemoteException {
        boolean check = true;
        int[][] validGrid = game.getGameboard().getValidGrid();
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

    /**
     * Returns points to assign to the player based on the number of adjacent items
     * @param player Player
     * @return integer of the score to be added to the player
     */
    public int adjacentItemsCheck(Player player) {
        Item[][] shelf = player.getMyShelf().getShelfGrid();
        Category scanned;
        List<Integer> counter = new ArrayList<>();
        counter.add(0);   // counts the number of adjacent items
        int score = 0;
        List<Integer> groups = new ArrayList<>();
        // boolean matrix in order to not revisit already checked Items
        boolean[][] visited = new boolean[SHELF_ROWS][SHELF_COLUMNS];
        for(int i=0; i<SHELF_ROWS; i++){
            for(int j=0; j<SHELF_COLUMNS; j++){
                if(!visited[i][j] && shelf[i][j].getCategoryType() != null){
                    scanned = shelf[i][j].getCategoryType();
                    // recursive method that checks adjacent tiles in the Shelf
                    adjacentCategoryCheck(shelf, visited, scanned, i, j, counter);
                    groups.add(counter.get(0));  // adds dimension of found group to list
                    // counter resets
                    counter.remove(0);
                    counter.add(0);
                }
            }
        }
        // for every slot in the list, points are added based on the dimensions found
        for(int i=0; i<groups.size(); i++){
            if(groups.get(i) == 3) { score += 2; }
            if(groups.get(i) == 4) { score += 3; }
            if(groups.get(i) == 5) { score += 5; }
            if(groups.get(i) >= 6) { score += 8; }
        }
        return score;
    }


    /**
     * Recursive method used in conjunction to adjacentItemsCheck to increase the counter in case there's an
     * adjacent item with the same category
     * @param shelf the current player's shelf
     * @param visited a matrix of booleans to check if the slot has already been visited
     * @param category the category of the previous item checked
     * @param i the row of the previous item checked
     * @param j the column of the previous item checked
     * @param counter a counter that increases if the conditions are met
     */
    private void adjacentCategoryCheck(Item[][] shelf, boolean[][] visited, Category category, int i, int j, List<Integer> counter){
        Integer temp;
        // stops the method if the item checked is out of the Shelf's bounds, has already been visited
        // or isn't of the same category as the previous one checked
        if     (i<0 || i>=SHELF_ROWS ||
                j<0|| j>=SHELF_COLUMNS ||
                visited[i][j] ||
                shelf[i][j].getCategoryType() != category){
            return;
        }
        visited[i][j] = true;  //
        // counter is incremented by 1
        temp = counter.remove(0);
        counter.add(temp.intValue()+1);
        // calls the same method on the adjacent tiles
        adjacentCategoryCheck(shelf,visited,category,i-1,j,counter);
        adjacentCategoryCheck(shelf,visited,category,i+1,j,counter);
        adjacentCategoryCheck(shelf,visited,category,i,j-1,counter);
        adjacentCategoryCheck(shelf,visited,category,i,j+1,counter);
    }

}

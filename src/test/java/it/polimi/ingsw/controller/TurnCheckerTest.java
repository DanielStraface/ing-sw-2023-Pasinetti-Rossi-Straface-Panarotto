package it.polimi.ingsw.controller;


import it.polimi.ingsw.exceptions.InvalidNumberOfPlayersException;
import it.polimi.ingsw.exceptions.InvalidPointerException;
import it.polimi.ingsw.exceptions.OutOfBoundsException;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.comcard.CommonObjCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)

public class TurnCheckerTest {

    private Shelf shelf;
    private TurnChecker turnChecker;
    private Game game;
    private Player player;
    private static final int INVALID=0;
    private static final int PLAYABLE=1;
    private static final int OCCUPIED=2;


    /**
     * Setup method for all tests
     */
    @BeforeEach
    public void setup() throws Exception {
        game = new Game(4);
        shelf = new Shelf();
        turnChecker = new TurnChecker();
        player = game.getPlayers().get(0);
        player.setIsFirstPlayer();
    }


    /**
     * Tests if the commonObjCard check doesn't assign points to the current player
     * @throws InvalidNumberOfPlayersException when the number of players given is invalid
     */
    @Test
    public void manageCheckCommonObjCardTest() throws InvalidNumberOfPlayersException{
        turnChecker.manageCheck(player,game);
        assertEquals(0,player.getScore(),"Points have been wrongly added!");
        for(int i=0;i<6;i++){
            for(int j=0;j<5;j++){
                player.getMyShelf().getShelfGrid()[i][j] = new Item(Category.CAT,1);
            }
        }
        game.setCommonObjCards(8,9);
        turnChecker.manageCheck(player,game);
        assertEquals(16,player.getScore(),"Points haven't been correctly added!");
    }


    /**
     * Tests if the gameBoard is refilled under the correct conditions
     */
    @Test
    public void manageCheckRefillGameBoardCheck(){
        int[][] validGrid;
        GameBoard gameBoard;
        validGrid = game.getValidGrid();
        gameBoard = game.getGameboard();
        boolean checker = true;
        for(int i=0; i<9; i++){
            for(int j=0; j<9; j++){
                if( validGrid[i][j] != INVALID){
                    gameBoard.getGameGrid()[i][j] = new Item(null,0);
                    validGrid[i][j] = PLAYABLE;
                }
            }
        }
        gameBoard.getGameGrid()[3][3] = new Item(Category.PLANT,1);
        validGrid[3][3] = OCCUPIED;
        gameBoard.getGameGrid()[5][5] = new Item(Category.TROPHY,1);
        validGrid[5][5] = OCCUPIED;
        gameBoard.getGameGrid()[6][4] = new Item(Category.CAT,1);
        validGrid[6][4] = OCCUPIED;
        game.setGameBoard(gameBoard);
        game.setValidGrid(validGrid);
        turnChecker.manageCheck(player,game);
        for(int i=0; i<9; i++){
            for(int j=0; j<9; j++){
                if(game.getValidGrid()[i][j] != INVALID && game.getGameboard().getGameGrid()[i][j].getCategoryType()==null){
                    checker = false;
                }
            }
        }
        assertTrue(checker,"The gameBoard hasn't been refilled correctly!");
    }


    /**
     * Checks if lastTurn is triggered or not under the correct conditions
     */
    @Test
    public void manageCheckLastTurnTest(){
        Shelf shelf;
        boolean check;
        check = turnChecker.manageCheck(player,game);
        assertFalse(check,"Last turn has been triggered even if the player's shelf isn't full!");
        shelf = player.getMyShelf();
        Random random = new Random();
        List<Category> categories = new ArrayList<Category>(Arrays.asList(Category.values()));
        for(int i=0; i<6; i++){
            for(int j=0; j<5; j++){
               shelf.getShelfGrid()[i][j]= new Item(categories.get(random.nextInt(6)),1);
            }
        }
        player.setMyShelf(shelf);
        check = turnChecker.manageCheck(player,game);
        assertTrue(check,"Last turn hasn't been triggered even if the player's shelf is full!");
    }


    /**
     * Tests every amount of points assigned (2,3,5,8) for adjacent Items
     */
    @Test
    public void adjacentPointsCheck(){
        for(int i=0; i<3; i++){
            shelf.getShelfGrid()[0][i] = new Item(Category.CAT,1);
            shelf.getShelfGrid()[4][i] = new Item(Category.TROPHY,1);
        }
        for(int i=0; i<4 ; i++){
            shelf.getShelfGrid()[1][i] = new Item(Category.BOOK,1);
        }
        for(int i=0; i<5; i++){
            shelf.getShelfGrid()[2][i] = new Item(Category.FRAME,1);
            shelf.getShelfGrid()[3][i] = new Item(Category.TROPHY,1);
        }
        player.setMyShelf(shelf);
        assertEquals(18,turnChecker.adjacentItemsCheck(player),"The score hasn't been correctly calculated");
    }


    /**
     * Tests the adjacent Items score assignment with the example given in the rulebook
     */
    @Test
    public void adjacentPointExampleCheck(){
        for(int i=0; i<2; i++){
            shelf.getShelfGrid()[0][i] = new Item(Category.PLANT,1);
            shelf.getShelfGrid()[1][i] = new Item(Category.PLANT,1);
            shelf.getShelfGrid()[2][i] = new Item(Category.FRAME,1);
            shelf.getShelfGrid()[5][i] = new Item(Category.TROPHY,1);
        }
        for(int i=2; i<4 ; i++){
            shelf.getShelfGrid()[1][i] = new Item(Category.PLANT,1);
            shelf.getShelfGrid()[2][i] = new Item(Category.PLANT,1);
            shelf.getShelfGrid()[i][4] = new Item(Category.BOOK,1);
        }
        for(int i=3; i<5; i++){
            shelf.getShelfGrid()[4][i] = new Item(Category.CAT,1);
            shelf.getShelfGrid()[5][i] = new Item(Category.CAT,1);
            shelf.getShelfGrid()[i][0] = new Item(Category.FRAME,1);
        }
        for(int i=1; i<4; i++){
            shelf.getShelfGrid()[3][i] = new Item(Category.GAME,1);
        }
        shelf.getShelfGrid()[4][1] = new Item(Category.TROPHY,1);
        shelf.getShelfGrid()[4][2] = new Item(Category.CAT,1);
        shelf.getShelfGrid()[5][2] = new Item(Category.TROPHY,1);
        player.setMyShelf(shelf);
        assertEquals(21,turnChecker.adjacentItemsCheck(player),"The score hasn't been correctly calculated");
    }


    /**
     * Tests the adjacent Items score assignment with a Shelf filled with Items with random categories
     */
    @Test
    public void adjacentRandomItemsCheck(){
        Random random = new Random();
        List<Category> categories = new ArrayList<Category>(Arrays.asList(Category.values()));
        for(int i=0; i<6; i++){
            for(int j=0; j<5; j++){
                shelf.getShelfGrid()[i][j]= new Item(categories.get(random.nextInt(6)),1);
            }
        }
        player.setMyShelf(shelf);
        assert (turnChecker.adjacentItemsCheck(player)) >= 0: "The score hasn't been correctly calculated" ;
    }

}

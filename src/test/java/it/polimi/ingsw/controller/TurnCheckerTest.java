package it.polimi.ingsw.controller;


import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)

public class TurnCheckerTest {

    private Shelf shelf;
    private Player player;
    private TurnChecker turnChecker;
    private Game game;
    private static final int INVALID=0;
    private static final int PLAYABLE=1;



    @BeforeEach
    public void setup() throws Exception {
        shelf = new Shelf();
        player = new Player();
        turnChecker = new TurnChecker();
        game = new Game(4);
    }


    /** Tests if the commonObjCard check doesn't assign points to the current player */
    @Test
    public void manageCheckCommonObjCardTest(){
        game.setCurrentPlayer(player);
        turnChecker.manageCheck(game.getCurrentPlayer(),game);
        assertEquals(0,game.getCurrentPlayer().getScore(),"Points have been wrongly added!");
    }


    /** Tests if the gameBoard is refilled under the correct conditions */
    @Test
    public void manageCheckRefillGameBoardCheck(){
        game.setCurrentPlayer(player);
        boolean checker = true;
        for(int i=0; i<9; i++){
            for(int j=0; j<9; j++){
                game.getGameboard().getGameGrid()[i][j] = new Item(null);
                game.getValidGrid()[i][j] = PLAYABLE;
            }
        }
        game.getGameboard().getGameGrid()[3][3] = new Item(Category.PLANT);
        game.getGameboard().getGameGrid()[5][5] = new Item(Category.TROPHY);
        game.getGameboard().getGameGrid()[6][4] = new Item(Category.CAT);
        turnChecker.manageCheck(game.getCurrentPlayer(),game);
        for(int i=0; i<9; i++){
            for(int j=0; j<9; j++){
                if(game.getValidGrid()[i][j] != INVALID && game.getGameboard().getGameGrid()[i][j].getCategoryType()==null){
                    checker = false;
                }
            }
        }
        assertTrue(checker,"The gameBoard hasn't been refilled correctly!");
    }



    /** Checks if lastTurn is triggered or not under the correct conditions */
    @Test
    public void manageCheckLastTurnTest(){
        boolean check;
        game.setCurrentPlayer(player);
        check = turnChecker.manageCheck(game.getCurrentPlayer(),game);
        assertFalse(check,"Last turn has been triggered even if the player's shelf isn't full!");
        Random random = new Random();
        List<Category> categories = new ArrayList<Category>(Arrays.asList(Category.values()));
        for(int i=0; i<6; i++){
            for(int j=0; j<5; j++){
               game.getCurrentPlayer().getMyShelf().getShelfGrid()[i][j]= new Item(categories.get(random.nextInt(6)));
            }
        }
        check= turnChecker.manageCheck(game.getCurrentPlayer(),game);
        assertTrue(check,"Last turn hasn't been triggered even if the player's shelf is full!");
    }



    /** Tests every amount of points assigned for adjacent Items */
    @Test
    public void adjacentPointsCheck(){
        for(int i=0; i<3; i++){
            shelf.getShelfGrid()[0][i] = new Item(Category.CAT);
            shelf.getShelfGrid()[4][i] = new Item(Category.TROPHY);
        }
        for(int i=0; i<4 ; i++){
            shelf.getShelfGrid()[1][i] = new Item(Category.BOOK);
        }
        for(int i=0; i<5; i++){
            shelf.getShelfGrid()[2][i] = new Item(Category.FRAME);
            shelf.getShelfGrid()[3][i] = new Item(Category.TROPHY);
        }
        player.setMyShelf(shelf);
        assertEquals(18,turnChecker.adjacentItemsCheck(player),"The score hasn't been correctly calculated");
    }


    /** Tests the adjacent Items score assignment with the example given in the rulebook */
    @Test
    public void adjacentPointExampleCheck(){
        for(int i=0; i<2; i++){
            shelf.getShelfGrid()[0][i] = new Item(Category.PLANT);
            shelf.getShelfGrid()[1][i] = new Item(Category.PLANT);
            shelf.getShelfGrid()[2][i] = new Item(Category.FRAME);
            shelf.getShelfGrid()[5][i] = new Item(Category.TROPHY);
        }
        for(int i=2; i<4 ; i++){
            shelf.getShelfGrid()[1][i] = new Item(Category.PLANT);
            shelf.getShelfGrid()[2][i] = new Item(Category.PLANT);
            shelf.getShelfGrid()[i][4] = new Item(Category.BOOK);
        }
        for(int i=3; i<5; i++){
            shelf.getShelfGrid()[4][i] = new Item(Category.CAT);
            shelf.getShelfGrid()[5][i] = new Item(Category.CAT);
            shelf.getShelfGrid()[i][0] = new Item(Category.FRAME);
        }
        for(int i=1; i<4; i++){
            shelf.getShelfGrid()[3][i] = new Item(Category.GAME);
        }
        shelf.getShelfGrid()[4][1] = new Item(Category.TROPHY);
        shelf.getShelfGrid()[4][2] = new Item(Category.CAT);
        shelf.getShelfGrid()[5][2] = new Item(Category.TROPHY);
        player.setMyShelf(shelf);
        assertEquals(21,turnChecker.adjacentItemsCheck(player),"The score hasn't been correctly calculated");
    }



    /** Tests the adjacent Items score assignment with a Shelf filled with Items with random categories */
    @Test
    public void adjacentRandomItemsCheck(){
        Random random = new Random();
        List<Category> categories = new ArrayList<Category>(Arrays.asList(Category.values()));
        for(int i=0; i<6; i++){
            for(int j=0; j<5; j++){
                shelf.getShelfGrid()[i][j]= new Item(categories.get(random.nextInt(6)));
            }
        }
        player.setMyShelf(shelf);
        assert (turnChecker.adjacentItemsCheck(player)) > 0: "The score hasn't been correctly calculated" ;
    }

}

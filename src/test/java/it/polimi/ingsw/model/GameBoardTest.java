package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.InvalidNumberOfPlayersException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GameBoardTest {

    private GameBoard gameboard;
    private Game game;

    /**
     * BeforeEach setup method set the gameboard = null
     */
    @BeforeEach
    public void setupTest(){
        gameboard = null;
    }

    /**
     * Test method allItemNull controls that every item of the grid is null
     */
    @Test
    public void allItemNull() throws InvalidNumberOfPlayersException, RemoteException {
        final int DIM = 9;
        gameboard = new GameBoard(2);
        for(int i=0;i<DIM;i++){
            for(int j=0;j<DIM;j++){
                assertSame(null, gameboard.getGameGrid()[i][j], "The item in this position is not null");
            }
        }
    }

    /**
     * Test method gridForTwoTester controls that the grid is set for two players
     */
    @Test
    public void gridForTwoTester(){
        final int NUM_OF_PLAYER = 2;
        final int ROW = 4;
        final int COL = 4;
        try{
            game = new Game(NUM_OF_PLAYER);
            gameboard = game.getGameboard(); //obtain game gameboard
            /* Two player game grid position */
            assertNotEquals(null, gameboard.getGameGrid()[ROW][COL],
                    "The item in this position is null"); //item in the position
            assertNotSame(null, gameboard.getGameGrid()[ROW][COL],
                    "The category of this position is null"); //category of that item
            assertNull(gameboard.getGameGrid()[ROW + 4][COL + 1].getCategoryType()); //for 3 players
            assertNull(gameboard.getGameGrid()[ROW - 4][COL].getCategoryType()); //for 4 player
            assertNull(gameboard.getGameGrid()[ROW + 4][COL + 4].getCategoryType()); //always null
        } catch (Exception e) {fail();}
    }

    /**
     * Test method gridForThreeTester controls that the grid is set for three players
     */
    @Test
    public void gridForThreeTester(){
        final int NUM_OF_PLAYER = 3;
        final int ROW = 4;
        final int COL = 4;
        try{
            game = new Game(NUM_OF_PLAYER);
            gameboard = game.getGameboard(); //obtain game gameboard
            /* Two player game grid position */
            assertNotEquals(null, gameboard.getGameGrid()[ROW][COL],
                    "The item in this position is null");
            assertNotSame(null, gameboard.getGameGrid()[ROW][COL],
                    "The category of this position is null");
            assertNotEquals(null, gameboard.getGameGrid()[ROW+4][COL+1]); //for 3 players
            assertNotSame(null, gameboard.getGameGrid()[ROW+4][COL+1].getCategoryType());
            assertNull(gameboard.getGameGrid()[ROW - 4][COL].getCategoryType()); //for 4 player
            assertNull(gameboard.getGameGrid()[ROW + 4][COL + 4].getCategoryType()); //always null
        } catch (Exception e) {fail();}
    }

    /**
     *  Test method gridForFourTester controls that the grid is set for four player
     */
    @Test
    public void gridForFourTester(){
        final int NUM_OF_PLAYER = 4;
        final int ROW = 4;
        final int COL = 4;
        try{
            game = new Game(NUM_OF_PLAYER);
            gameboard = game.getGameboard();
            /* Two player game grid position */
            assertNotEquals(null, gameboard.getGameGrid()[ROW][COL],
                    "The item in this position is null");
            assertNotSame(null, gameboard.getGameGrid()[ROW][COL],
                    "The category of this position is null");
            assertNotEquals(null, gameboard.getGameGrid()[ROW+4][COL+1]); //for 3 players
            assertNotSame(null, gameboard.getGameGrid()[ROW+4][COL+1].getCategoryType());
            assertNotEquals(null, gameboard.getGameGrid()[ROW-4][COL]); //for 4 player
            assertNotSame(null, gameboard.getGameGrid()[ROW+4][COL].getCategoryType());
            assertNull(gameboard.getGameGrid()[ROW + 4][COL + 4].getCategoryType()); //always null
        } catch (Exception e) {fail();}
    }
}
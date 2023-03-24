package it.polimi.ingsw.model;

import it.polimi.ingsw.model.personcard.PersonalObjCard;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PlayerTest {
    Player testPlayer;
    private static String nickname = "player0";
    private static int clientID = 0;
    private static boolean firstPlayer = false;
    private int score;
    private Shelf myShelf;
    private PersonalObjCard myPersonalObjCard;
    private List<Item> selectItems;

    private Game game;
    private GameBoard gameBoard;
    private Item[][] gameGrid;
    private int[][] validGrid;
    public int[][] selectedCoords;
    public int selectedCol;

    @BeforeEach
    void creationPlayerAndGame() throws Exception {
        testPlayer = new Player(nickname, clientID, firstPlayer);
        game = null;
        try {
            game = new Game(4);
        } catch (Exception e) {
            fail("Game not created successfully");
        }
        gameBoard= game.getGameboard();
        gameGrid = gameBoard.getGameGrid();
        validGrid = game.getValidGrid();
        myShelf = testPlayer.getMyShelf();
        myPersonalObjCard = testPlayer.getMyPersonalOBjCard();
    }


    @Test
    public void setTestPlayer() {
        assertEquals(nickname, testPlayer.getNickname(), "Not the same nickname");
        assertEquals(clientID, testPlayer.getClientID(), "Not the same clientID");
        assertEquals(firstPlayer, testPlayer.getFirstPlayer(), "Variable is not false");
    }

    @Test
    public void diffRowsAndColsTest() throws Exception {
        selectedCoords = new int[][]{{1, 2}, {2, 4}, {5, 6}};
        selectedCol = 0;
        try {
            Assertions.assertThrows(Exception.class, () -> testPlayer.playerChoice(selectedCoords, gameGrid, validGrid, selectedCol), "Not same row or column Exception");
        } catch (Exception e) {
            fail("You can take from these coordinates");
        }
    }

    @Test
    public void noConsecutiveCoordsTest() throws Exception {
        selectedCoords = new int[][]{{1, 2}, {1, 3}, {1, 5}};
        selectedCol = 0;
        try {
            Assertions.assertThrows(Exception.class, () -> testPlayer.playerChoice(selectedCoords, gameGrid, validGrid, selectedCol), "No consecutive coords");
        } catch (Exception e) {
            fail("You can take from these coordinates");
        }
    }

    @Test
    public void noFreeSidesTest() throws Exception {
        selectedCoords = new int[][]{{1, 2}, {1, 3}, {1, 4}};
        selectedCol = 0;
        try {
            Assertions.assertThrows(Exception.class, () -> testPlayer.playerChoice(selectedCoords, gameGrid, validGrid, selectedCol), "No free sides");
        } catch (Exception e) {
            fail("You can take from these coordinates");
        }
    }

    @Test
    public void noAvailableColTest() throws Exception {
        selectedCoords = new int[][]{{0, 0}, {0, 1}, {0, 2}};
        selectedCol = 6;
        try {
            Assertions.assertThrows(Exception.class, () -> testPlayer.playerChoice(selectedCoords, gameGrid, validGrid, selectedCol), "No Available Column");
        } catch (Exception e) {
            fail("You can take from these coordinates");
        }
    }

    @Test
    public void correctParamsTest() throws Exception {
        selectedCoords = new int[][]{{0, 0}, {0, 1}, {0, 2}};
        selectedCol = 3;
        try {
            Assertions.assertDoesNotThrow(()-> testPlayer.playerChoice(selectedCoords, gameGrid, validGrid, selectedCol), "No Available Column");
        } catch (Exception e) {
            fail("You can take from these coordinates");
        }
    }

}



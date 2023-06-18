package it.polimi.ingsw.controller;

import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.exceptions.InvalidMatchesException;
import it.polimi.ingsw.exceptions.InvalidNumberOfItemsException;
import it.polimi.ingsw.exceptions.InvalidPointerException;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.personcard.PersonalObjCard;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;


import java.rmi.Remote;
import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)

/**
 * TurnHandlerTest class tests TurnHandler class.
 */
public class TurnHandlerTest {
    private Player player1, player2, player3;
    private Game game;
    private TurnHandler turnHandler;
    private TurnChecker turnChecker;
    private Shelf shelf0, shelf1, shelf2, shelf3, shelf4, shelf5, shelf6;
    private PersonalObjCard card1, card2, card3;
    private Item[][] grid0, grid1, grid2, grid3;
    private final int ROWS = 6;
    private final int COLS = 5;
    private Client o;

 /*
    @BeforeAll
    public void setup() {
        shelf0 = new Shelf();
        shelf1 = new Shelf();
        shelf2 = new Shelf();
        shelf3 = new Shelf();
        shelf4 = new Shelf();
        shelf5 = new Shelf();
        shelf6 = new Shelf();
        grid0 = new Item[ROWS][COLS];
        grid1 = new Item[ROWS][COLS];
        grid2 = new Item[ROWS][COLS];
        grid3 = new Item[ROWS][COLS];
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                shelf1.getShelfGrid()[i][j] = new Item(null,0);
                shelf2.getShelfGrid()[i][j] = new Item(null,0);
                shelf3.getShelfGrid()[i][j] = new Item(null,0);
                shelf0.getShelfGrid()[i][j] = new Item(null,0);
                shelf4.getShelfGrid()[i][j] = new Item(null,0);
                shelf5.getShelfGrid()[i][j] = new Item(null,0);
                shelf6.getShelfGrid()[i][j] = new Item(null,0);
                grid1[i][j] = new Item(null,0);
                grid2[i][j] = new Item(null,0);
                grid3[i][j] = new Item(null,0);
                grid0[i][j] = new Item(null,0);
            }
        }

    }

    @BeforeEach
    public void setUp() throws Exception {
        player1 = new Player();
        player2 = new Player();
        player3 = new Player();
        game = new Game(3);
        turnHandler = new TurnHandler(game);
        turnChecker = new TurnChecker();
        Client o;
        player1 = game.getPlayers().get(0);
        player2 = game.getPlayers().get(1);
        player3 = game.getPlayers().get(2);


    }




    /**
     * correctNextTurnShift verify that the turn has passed correctly to the next player.
     */

    /*
    @Test
     public void correctNextTurnShift() throws RemoteException {
        game.setCurrentPlayer(player1);
        turnHandler.nextTurn(player1);
        assertEquals(player2, game.getCurrentPlayer());
        turnHandler.nextTurn(player2);
        assertEquals(player3, game.getCurrentPlayer());
        turnHandler.nextTurn(player3);
        assertEquals(player1, game.getCurrentPlayer());
        turnHandler.nextTurn(player1);
        assertEquals(player2, game.getCurrentPlayer());
    }


    /**
     * correctCallToGameOver verify that gameOverHandler method is called when the game is over.
     *
     * @throws InvalidPointerException
     */

    /*
    @Test
    public void correctCallToGameOver() throws InvalidPointerException, RemoteException {
        boolean gameOver;
        game.setCurrentPlayer(player2);
        gameOver = turnChecker.manageCheck(player2, game);
        turnHandler.nextTurn(player2);
        if (gameOver) {
            turnHandler.callGameOverHandler();
        } else {
            assertEquals(player3, game.getCurrentPlayer());
        }
    }

    /**
     * correctGameOverHandler verify that GameOverHandler method correctly assigns point to player.
     *
     * @throws InvalidMatchesException
     */

    /*
    @Test
    public void correctGameOverHandler() throws InvalidMatchesException {
        card1 = player1.getMyPersonalOBjCard();
        card2 = player2.getMyPersonalOBjCard();
        card3 = player3.getMyPersonalOBjCard();

        shelf1 = player1.getMyShelf();
        shelf2 = player2.getMyShelf();
        shelf3 = player3.getMyShelf();


        grid1 = card1.getCardGrid();
        grid2 = card2.getCardGrid();
        grid3 = card3.getCardGrid();

        grid1[0][0] = new Item(Category.CAT);
        grid1[2][1] = new Item(Category.BOOK);
        grid1[0][2] = new Item(Category.TROPHY);
        grid1[3][3] = new Item(Category.GAME);
        grid1[2][4] = new Item(Category.PLANT);
        grid1[3][4] = new Item(Category.FRAME);

        grid2[1][0] = new Item(Category.BOOK);
        grid2[2][1] = new Item(Category.CAT);
        grid2[0][2] = new Item(Category.TROPHY);
        grid2[4][3] = new Item(Category.FRAME);
        grid2[5][3] = new Item(Category.PLANT);
        grid2[3][4] = new Item(Category.GAME);

        grid3[5][0] = new Item(Category.BOOK);
        grid3[2][1] = new Item(Category.PLANT);
        grid3[4][1] = new Item(Category.CAT);
        grid3[1][2] = new Item(Category.GAME);
        grid3[3][3] = new Item(Category.FRAME);
        grid3[4][0] = new Item(Category.TROPHY);

        shelf1.getShelfGrid()[0][0] = new Item(Category.CAT);
        shelf1.getShelfGrid()[1][0] = new Item(Category.CAT);
        shelf1.getShelfGrid()[2][0] = new Item(Category.CAT);
        shelf1.getShelfGrid()[3][1] = new Item(Category.GAME);
        shelf1.getShelfGrid()[3][2] = new Item(Category.GAME);
        shelf1.getShelfGrid()[3][3] = new Item(Category.GAME);
        shelf1.getShelfGrid()[0][2] = new Item(Category.TROPHY);

        shelf2.getShelfGrid()[0][2] = new Item(Category.FRAME);
        shelf2.getShelfGrid()[1][2] = new Item(Category.FRAME);
        shelf2.getShelfGrid()[2][2] = new Item(Category.FRAME);
        shelf2.getShelfGrid()[3][2] = new Item(Category.FRAME);
        shelf2.getShelfGrid()[3][4] = new Item(Category.GAME);

        shelf3.getShelfGrid()[5][0] = new Item(Category.BOOK);
        shelf3.getShelfGrid()[1][2] = new Item(Category.GAME);
        shelf3.getShelfGrid()[3][3] = new Item(Category.FRAME);

        turnHandler.callGameOverHandler();
        assertEquals(8, player1.getScore(), "points are not added correctly");
        assertEquals(4, player2.getScore(), "points are not added correctly");
        assertEquals(4, player3.getScore(), "points are not added correctly");
    }
     */
    /**
     * ManageTurnPlayerNotFirstPlayer verify that ManageTurn added a point to the first player who fills the library
     * and if the player isn't the FirstPlayer or the last one, passes the turn to the next player.
     * @throws RemoteException
     * @throws InvalidPointerException
     */

    /*
    @Test
    public void ManageTurnPlayerNotFirstPlayer() throws RemoteException, InvalidPointerException{
        int points;
        shelf4 = player2.getMyShelf();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                shelf4.getShelfGrid()[i][j] = new Item(Category.TROPHY);
            }
        }
        player1.setIsFirstPlayer();
        game.setCurrentPlayer(player2);
        turnChecker.manageCheck(player2, game);
        points = player2.getScore();


        turnHandler.manageTurn(o);
        assertEquals(points + 1, player2.getScore());
        turnHandler.nextTurn(player2);
        assertEquals(player3, game.getCurrentPlayer());
    }

    @BeforeEach
    public void setUpShelf4() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                shelf4.getShelfGrid()[i][j] = new Item(null);
            }
        }
    }

    /**
     * ManageTurnIsFirstPlayer check manageTurn method on the player seated before the firstPlayer.
     * @throws RemoteException
     * @throws InvalidPointerException
     */

    /*
    @Test
    public void ManageTurnIsFirstPlayer() throws RemoteException, InvalidPointerException {
        int points;
        shelf4 = player2.getMyShelf();
        shelf5 = player1.getMyShelf();
        shelf6 = player3.getMyShelf();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                shelf4.getShelfGrid()[i][j] = new Item(Category.TROPHY);
            }
        }

        shelf5.getShelfGrid()[0][2] = new Item(Category.TROPHY);
        shelf5.getShelfGrid()[5][2] = new Item(Category.CAT);
        shelf6.getShelfGrid()[2][2] = new Item(Category.BOOK);
        shelf6.getShelfGrid()[0][4] = new Item(Category.TROPHY);

        player2.setIsFirstPlayer();
        game.setCurrentPlayer(player1);
        turnChecker.manageCheck(player2, game);

        assertFalse(turnChecker.manageCheck(player1, game));
        assertEquals(game.getPlayers().indexOf(player1), game.getPlayers().indexOf(player2) - 1);
        points = player1.getScore();
        turnHandler.manageTurn(o);
        assertEquals(points, player1.getScore());
        turnHandler.nextTurn(player1);
    }

    /**
     * manageTurnLastPlayer check manageTurn method on the last player of the player list.
     * @throws RemoteException
     * @throws InvalidPointerException
     */

    /*
    @Test
    public void manageTurnLastPlayer() throws RemoteException, InvalidPointerException{
        int points2, points3;
        shelf4 = player2.getMyShelf();
        shelf5 = player1.getMyShelf();
        shelf6 = player3.getMyShelf();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                shelf4.getShelfGrid()[i][j] = new Item(Category.TROPHY);
            }
        }

        shelf5.getShelfGrid()[0][2] = new Item(Category.TROPHY);
        shelf5.getShelfGrid()[5][2] = new Item(Category.CAT);
        shelf6.getShelfGrid()[2][2] = new Item(Category.BOOK);
        shelf6.getShelfGrid()[0][4] = new Item(Category.TROPHY);

        player1.setIsFirstPlayer();
        game.setCurrentPlayer(player3);
        points2 = player2.getScore();
        points3= player3.getScore();
        turnChecker.manageCheck(player2, game);
        assertFalse(turnChecker.manageCheck(player3,game));
        assertEquals(game.getPlayers().indexOf(player3), game.getPlayers().indexOf(player1) - 1);
        turnHandler.manageTurn(o);
        assertEquals(points2 + 1, player2.getScore());
        assertEquals(points3, player3.getScore());

    }
    */
}




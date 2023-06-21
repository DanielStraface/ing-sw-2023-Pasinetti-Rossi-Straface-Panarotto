package it.polimi.ingsw.controller;

import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.exceptions.InvalidNumberOfPlayersException;
import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;


import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)

/**
 * TurnHandlerTest class tests TurnHandler class.
 * @see TurnHandler
 */
public class TurnHandlerTest {
    private Player player1, player2, player3;
    private Game game;
    private TurnHandler turnHandler;
    private TurnChecker turnChecker;
    private Controller controller;
    private Shelf shelf0, shelf1, shelf2, shelf3, shelf4, shelf5, shelf6;
    private Item[][] grid0, grid1, grid2, grid3;
    private final int ROWS = 6;
    private final int COLS = 5;

    /**
     * Setup method for all tests
     */
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

    /**
     * Setup method for all tests
     * @throws InvalidNumberOfPlayersException if the number of players int given is invalid
     * @throws RemoteException if the execution of a remote method call goes wrong
     */
    @BeforeEach
    public void setUp() throws InvalidNumberOfPlayersException, RemoteException {
        player1 = new Player();
        player2 = new Player();
        player3 = new Player();
        game = new Game(3);
        turnHandler = new TurnHandler(game);
        turnChecker = new TurnChecker();
        controller = new Controller(game);
        Client o;
        player1 = game.getPlayers().get(0);
        player2 = game.getPlayers().get(1);
        player3 = game.getPlayers().get(2);


    }


    /**
     * correctNextTurnShift verify that the turn has passed correctly to the next player.
     * @throws RemoteException if the execution of a remote method call goes wrong
     */
    @Test
     public void correctNextTurnShift() throws RemoteException {
        game.setCurrentPlayer(player1);
        turnHandler.nextTurn(controller.getMatchID(), player1);
        assertEquals(player2, game.getCurrentPlayer());
        turnHandler.nextTurn(controller.getMatchID(), player2);
        assertEquals(player3, game.getCurrentPlayer());
        turnHandler.nextTurn(controller.getMatchID(), player3);
        assertEquals(player1, game.getCurrentPlayer());
        turnHandler.nextTurn(controller.getMatchID(), player1);
        assertEquals(player2, game.getCurrentPlayer());
    }


    /**
     * correctCallToGameOver verify that gameOverHandler method is called when the game is over.
     * @throws RemoteException if the execution of a remote method call goes wrong
     */
    @Test
    public void correctCallToGameOver() throws RemoteException {
        boolean gameOver;
        game.setCurrentPlayer(player2);
        gameOver = turnChecker.manageCheck(player2, game);
        turnHandler.nextTurn(controller.getMatchID(), player2);
        if (gameOver) {
            turnHandler.callGameOverHandler();
        } else {
            assertEquals(player3, game.getCurrentPlayer());
        }
    }

    /**
     * ManageTurnPlayerNotFirstPlayer verify that ManageTurn added a point to the first player who fills the library
     * and if the player isn't the FirstPlayer or the last one, passes the turn to the next player.
     * @throws RemoteException if the execution of a remote method call goes wrong
     */
    @Test
    public void ManageTurnPlayerNotFirstPlayer() throws RemoteException{
        int points;
        shelf4 = player2.getMyShelf();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                shelf4.getShelfGrid()[i][j] = new Item(Category.TROPHY,1);
            }
        }
        player1.setIsFirstPlayer();
        game.setCurrentPlayer(player2);
        turnChecker.manageCheck(player2, game);
        points = player2.getScore();


        turnHandler.manageTurn(controller.getMatchID());
        assertEquals(points + 1, player2.getScore());
        turnHandler.nextTurn(controller.getMatchID(), player2);
        assertEquals(player3, game.getCurrentPlayer());
    }

    /**
     * Setup method for all tests
     */
    @BeforeEach
    public void setUpShelf4() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                shelf4.getShelfGrid()[i][j] = new Item(null,0);
            }
        }
    }

    /**
     * ManageTurnIsFirstPlayer check manageTurn method on the player seated before the firstPlayer.
     * @throws RemoteException if the execution of a remote method call goes wrong
     */
    @Test
    public void ManageTurnIsFirstPlayer() throws RemoteException{
        int points;
        shelf4 = player2.getMyShelf();
        shelf5 = player1.getMyShelf();
        shelf6 = player3.getMyShelf();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                shelf4.getShelfGrid()[i][j] = new Item(Category.TROPHY,1);
            }
        }

        shelf5.getShelfGrid()[0][2] = new Item(Category.TROPHY,1);
        shelf5.getShelfGrid()[5][2] = new Item(Category.CAT,1);
        shelf6.getShelfGrid()[2][2] = new Item(Category.BOOK,1);
        shelf6.getShelfGrid()[0][4] = new Item(Category.TROPHY,1);

        player2.setIsFirstPlayer();
        game.setCurrentPlayer(player1);
        turnChecker.manageCheck(player2, game);

        assertFalse(turnChecker.manageCheck(player1, game));
        assertEquals(game.getPlayers().indexOf(player1), game.getPlayers().indexOf(player2) - 1);
        points = player1.getScore();
        turnHandler.manageTurn(controller.getMatchID());
        assertEquals(points, player1.getScore());
        turnHandler.nextTurn(controller.getMatchID(), player1);
    }

    /**
     * manageTurnLastPlayer check manageTurn method on the last player of the player list.
     * @throws RemoteException if the execution of a remote method call goes wrong
     */
    @Test
    public void manageTurnLastPlayer() throws RemoteException{
        int points2, points3;
        shelf4 = player2.getMyShelf();
        shelf5 = player1.getMyShelf();
        shelf6 = player3.getMyShelf();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                shelf4.getShelfGrid()[i][j] = new Item(Category.TROPHY,1);
            }
        }

        shelf5.getShelfGrid()[0][2] = new Item(Category.TROPHY,1);
        shelf5.getShelfGrid()[5][2] = new Item(Category.CAT,1);
        shelf6.getShelfGrid()[2][2] = new Item(Category.BOOK,1);
        shelf6.getShelfGrid()[0][4] = new Item(Category.TROPHY,1);

        player1.setIsFirstPlayer();
        game.setCurrentPlayer(player3);
        turnChecker.manageCheck(player2, game);
        assertFalse(turnChecker.manageCheck(player3,game));
        turnHandler.manageTurn(controller.getMatchID());
        points3 = player3.getScore();
        assertEquals(points3, player3.getScore());

    }

}




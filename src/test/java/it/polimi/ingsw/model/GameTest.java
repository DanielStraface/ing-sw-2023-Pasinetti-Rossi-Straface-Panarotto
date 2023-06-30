package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.InvalidNumberOfPlayersException;
import it.polimi.ingsw.model.comcard.CommonObjCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class GameTest tests Game class
 * @see Game
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GameTest {

    private Game game;
    private GameBoard gameBoard;
    private List<Player> players;
    private List<CommonObjCard> commonObjCards;
    private static final int OCCUPIED = 2;
    private int playersNumber;

    /**
     * Setup method for all tests
     */
    @BeforeEach
    public void createGame() {
        game = null;
        try{
            game = new Game(4);
        }
        catch(InvalidNumberOfPlayersException e) {
            fail("Game not created successfully");
        }
        catch (RemoteException e){
            fail("Game not created successfully");
        }
        gameBoard = game.getGameboard();
        players = game.getPlayers();
        commonObjCards = game.getCommonObjCard();
        playersNumber = game.getPlayersNumber();
    }

    /**
     * Method to test if players are successfully created in a match
     */
    @Test
    public void createPlayersTest(){
        boolean checker = true;
        for(int i=0;i<players.size();i++) {
            if (!Objects.equals(players.get(i).getNickname(), null) &&
                    players.get(i).getClientID() != 0 &&
                    !players.get(i).getIsFirstPlayer()) {
                checker = false;
                break;
            }
        }
        assertTrue(checker,"Players not created successfully");
    }


    /**
     *  Method to test if the bag is created successfully
     */
    @Test
    public void createBag(){
        Bag bag;
        bag = game.getBag();
        for(Item item: bag.getItemCards()){
            assertNotEquals(null,item,"An item is missing");
        }
    }


    /**
     *  Method to test if the GameBoard is refilled correctly
     */
    @Test
    public void gameBoardRefillTest() {
        boolean checker = true;
        for (int i = 0; i < gameBoard.getGameGrid().length; i++) {
            for (int j = 0; j < gameBoard.getGameGrid()[i].length; j++) {
                game.getValidGrid()[i][j] = 1;
                game.getGameboard().getGameGrid()[i][j] = new Item(null,0);
                }
        }
        game.refillGameBoard();
        for (int i = 0; i < gameBoard.getGameGrid().length; i++) {
            for (int j = 0; j < gameBoard.getGameGrid()[i].length; j++) {
                if (game.getValidGrid()[i][j] == 0 && gameBoard.getGameGrid()[i][j].getCategoryType() != null) {
                    checker = false;
                }
                if (game.getValidGrid()[i][j] == OCCUPIED && gameBoard.getGameGrid()[i][j].getCategoryType() == null) {
                    checker = false;
                }
            }
        }
        assertTrue(checker, "The GameBoard hasn't been refilled correctly");
    }


    /**
     * Method to test if each player successfully receives its own PersonalObjectiveCard
     */
    @Test
    public void persObjCardGenerationTest(){
        for(int i=0; i<playersNumber; i++){
            assertNotEquals(null,game.getPlayers().get(i).getMyPersonalOBjCard(),
                            "Card not assigned correctly to a player");
            for(int j=0; j<game.getPlayers().size(); j++){
                if(i!=j){
                    assertNotSame(game.getPlayers().get(i).getMyPersonalOBjCard(),
                                  game.getPlayers().get(j).getMyPersonalOBjCard(),
                                 "Same PersonalObjCard assigned to two or more players");
                    }
                }
        }
    }


    /**
     * Method to test if two commonObjectiveCards are correctly extracted
     */
    @Test
    public void commonObjCardGenerationTest(){
        for(int i=0; i<commonObjCards.size(); i++){
            assertNotEquals(null,commonObjCards.get(i),
                            "CommonObjectiveCard not correctly assigned to match");
            for(int j=0; j<commonObjCards.size(); j++){
                if(i!=j){
                    assertNotSame(commonObjCards.get(i),commonObjCards.get(j),
                                  "Same CommonObjCards assigned to match");
                }
            }
        }
    }

    /**
     * setTurnFinishedPlayerID set method test
     */
    @Test
    public void setTurnFinishedPlayerIDTest(){
        int clientID = 10;
        game.setTurnFinishedPlayerID(clientID);
        assertSame(game.getPrevClientID(),clientID,"The clientID set is not the same");
    }

    /**
     * setGameBoard set method test
     * @throws InvalidNumberOfPlayersException if the number of players int given is invalid
     * @throws RemoteException if the execution of a remote method call goes wrong
     */
    @Test
    public void setGameBoardTest() throws InvalidNumberOfPlayersException, RemoteException {
        GameBoard gameBoard1 = new GameBoard(4);
        game.setGameBoard(gameBoard1);
        assertSame(game.getGameboard(),gameBoard1,"The gameBoard set is not the same");
    }

    /**
     * setValidGrid set method test
     */
    @Test
    public void setValidGridTest(){
        int[][] validGrid = new int[9][9];
        for(int i=0; i<9; i++){
            for(int j=0; j<9; j++){
                validGrid[i][j] = 1;
            }
        }
        game.setValidGrid(validGrid);
        assertSame(game.getValidGrid(),validGrid,"The validGrid set is not the same");
    }

    /**
     * setCommonObjCard set method test
     * @throws InvalidNumberOfPlayersException when the number of players given is invalid
     */
    @Test
    public void setCommonObjCardsTest() throws InvalidNumberOfPlayersException {
        game.setCommonObjCards(0,0);
        assertSame(game.getCommonObjCard().get(0).getType(),1,"The commonObjCard 1 set is not the same");
        assertSame(game.getCommonObjCard().get(1).getType(),2,"The commonObjCard 2 set is not the same");
    }

    /**
     * setGameOverPointToken set method test
     */
    @Test
    public void setGameOverPointTokenTest(){
        boolean value = true;
        String nickname = "Player nick";
        game.setGameOverPointToken(value,nickname);
        assertSame(game.getGameOverPointToken(),value,"The gameOver boolean set is not the same");
        assertSame(game.getGameOverPointPlayerName(),nickname,"The gameOver player nickname set is not the same");
    }

    /**
     * getPlayersNumber get method test
     */
    @Test
    public void getPlayersNumberTest(){
        assertSame(game.getPlayersNumber(),4,"The Game class hasn't given the correct number of players");
    }

    /**
     * getPlayers get method test
     */
    @Test
    public void getPlayersTest(){
        Player player = game.getPlayers().get(0);
        assertSame(game.getPlayers().get(0),player,"The Game class hasn't given the correct Player");
    }

    /**
     * getGameBoard get method test
     */
    @Test
    public void getGameBoardTest(){
        GameBoard gameBoard = game.getGameboard();
        assertSame(game.getGameboard(),gameBoard,"The Game class hasn't given the gameBoard correctly");
    }

    /**
     * getCommonObjCard get method test
     */
    @Test
    public void getCommonObjCardTest(){
        List<CommonObjCard> gameCommonObjCards = game.getCommonObjCard();
        assertSame(game.getCommonObjCard(),gameCommonObjCards,"The Game class hasn't given a correct list " +
                "of commonObjCards");
    }

    /**
     * getBag get method test
     */
    @Test
    public void getBagTest(){
        Bag bag = game.getBag();
        assertSame(game.getBag(),bag,"The Game class hasn't given the Bag correctly");
    }

    /**
     * getCurrentPlayer get method test
     */
    @Test
    public void getCurrentPlayerTest(){
        Player player = game.getCurrentPlayer();
        assertSame(game.getCurrentPlayer(),player,"The Game class hasn't given the correct currentPlayer");
    }

    /**
     * getPrevClientID get method test
     */
    @Test
    public void getPrevClientIDTest(){
        int clientID = game.getPrevClientID();
        assertSame(game.getPrevClientID(),clientID,"The Game class hasn't given the correct previous clientID");
    }

    /**
     * getValidGrid get method test
     */
    @Test
    public void getValidGridTest(){
        int[][] validGrid = game.getValidGrid();
        assertSame(game.getValidGrid(),validGrid,"The Game class hasn't given the validGrid correctly");
    }

    /**
     * getGameOverFinalMessage get method test
     */
    @Test
    public void getGameOverFinalMessageTest(){
        String finalMessage = game.getGameOverFinalMessage();
        assertSame(game.getGameOverFinalMessage(),finalMessage,"The Game class hasn't given the game over message correctly");
    }

    /**
     * getGameOverPointToken get method test
     */
    @Test
    public void getGameOverPointTokenTest(){
        boolean gameOver = game.getGameOverPointToken();
        assertSame(game.getGameOverPointToken(),gameOver,"The Game class hasn't given the gameOver flag correctly");
    }

    /**
     * getGameOverPointPlayerName get method test
     */
    @Test
    public void getGameOverPointPlayerNameTest(){
        String nickname = game.getGameOverPointPlayerName();
        assertSame(game.getGameOverPointPlayerName(),nickname,"The Game class hasn't given the correct game over player nickname");
    }

    /**
     * getTurnCounter get method test
     */
    @Test
    public void getTurnCounterTest(){
        int turnCounter = game.getTurnCounter();
        assertSame(game.getTurnCounter(),turnCounter,"The turnCounter given is not the same");
    }
}



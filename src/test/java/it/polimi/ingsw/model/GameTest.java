package it.polimi.ingsw.model;

import it.polimi.ingsw.model.comcard.CommonObjCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)

public class GameTest {

    private Game game;
    private GameBoard gameBoard;
    private List<Player> players;
    private List<CommonObjCard> commonObjCards;
    private static final int OCCUPIED = 2;
    private int playersNumber;


    @BeforeEach
    public void createGame() {
        game = null;
        try{
            game = new Game(4);
        }
        catch(Exception e) {
            fail("Game not created successfully");
        }
        gameBoard = game.getGameboard();
        players = game.getPlayers();
        commonObjCards = game.getCommonObjCard();
        playersNumber = game.getPlayers().size();
    }


    /** Method to test if players are successfully created in a match */
    @Test
    public void playerCreationTest(){
        boolean checker = true;
        for(int i=0;i<players.size();i++) {
            if (!Objects.equals(players.get(i).getNickname(), " ") &&
                    players.get(i).getClientID() != i &&
                    players.get(i).getFirstPlayer()) {
                checker = false;
                break;
            }
        }
        assertTrue(checker,"Players not created successfully");
    }



    /** Method to test if the bag is created successfully */
    @Test
    public void bagCreation(){
        Bag bag;
        bag = game.getBag();
        for(Item item: bag.getItemCards()){
            assertNotEquals(null,item,"An item is missing");
        }
    }


    /** Method to test if the list of players is successfully returned */
    @Test
    public void getPlayersTest(){
        List<Player> players;
        players = game.getPlayers();
        assertEquals(game.getPlayers(), players, "The Players list returned is not the same");
    }


    /** Method to test if the GameBoard is refilled correctly */
    @Test
    public void gameBoardRefillTest(){
        boolean checker = true;
        for(int i=0; i<gameBoard.getGameGrid().length; i++){
            for(int j=0; j<gameBoard.getGameGrid()[i].length; j++){
                if(game.getValidGrid()[i][j] == 0 && gameBoard.getGameGrid()[i][j] != null){
                    checker = false;
                }
                if(game.getValidGrid()[i][j] == OCCUPIED && gameBoard.getGameGrid()[i][j] == null){
                    checker = false;
                }
            }
        }
        assertTrue(checker,"The GameBoard hasn't been refilled correctly");
    }


    /** Method to test if each player successfully receives its own PersonalObjectiveCard */
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



    /** Method to test if two commonObjectiveCards are correctly extracted */
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


    /** Method to test if the CommonObjectiveCard is successfully returned */
    @Test
    public void getCommonObjCardTest(){
        List <CommonObjCard> commonObjCard;
        commonObjCard = game.getCommonObjCard();
        assertEquals(game.getCommonObjCard(), commonObjCard,
                    "The PersonalObjectCard list returned is not the same");
    }


    /** Method to test if the current player is correctly changed */
    @Test
    public void setCurrentPlayerTest(){
        try {
            Player player = new Player("TestName", 0, true);
            game.setCurrentPlayer(player);
            assertSame(game.getCurrentPlayer(), player, "The current Player set is not the same");
        }
        catch (Exception e){
            fail("Exception");
        }
    }


    @Test
    public void gameCreationTest(){
        playerCreationTest();
        bagCreation();
        gameBoardRefillTest();
        persObjCardGenerationTest();
        commonObjCardGenerationTest();
    }



    // Method to test all the other Get methods in Game

    @Test
    public void getCurrentPlayerTest(){
        Player player;
        player = game.getCurrentPlayer();
        assertEquals(game.getCurrentPlayer(), player, "The current Player returned is not the same");
    }


    @Test
    public void getGameBoardTest(){
        GameBoard gameboard;
        gameboard = game.getGameboard();
        assertEquals(game.getGameboard(), gameboard, "The GameBoard returned is not the same");
    }



    @Test
    public void getValidGridTest(){
        int[][] grid;
        grid = game.getValidGrid();
        assertEquals(game.getValidGrid(), grid, "The valid grid returned is not the same");
    }


    @Test
    public void getBagTest(){
        Bag bag;
        bag = game.getBag();
        assertEquals(game.getBag(), bag, "The bag returned is not the same");
    }


}



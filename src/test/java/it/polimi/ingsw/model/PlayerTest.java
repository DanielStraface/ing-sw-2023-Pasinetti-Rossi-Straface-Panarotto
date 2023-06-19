package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.personcard.PersonalObjCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PlayerTest {
    Player testPlayer;
    private Shelf myShelf;
    private List<Item> selectItems;
    private Game testGame;
    private List<int[]> selectedCoords;
    private int selectedCol;
    private Item[][] gameBoard;
    private int[][] validGrid;

    /**
     * Setup method for all tests
     * @throws InvalidNumberOfPlayersException if the number of players int given is invalid
     * @throws RemoteException if the execution of a remote method call goes wrong
     */
    @BeforeEach
    void creationPlayerAndGame() throws InvalidNumberOfPlayersException, RemoteException {
        final int NUM_OF_PLAYER = 4;
        testGame = new Game(NUM_OF_PLAYER);
        testPlayer = new Player();
        myShelf=testPlayer.getMyShelf();
        gameBoard = testGame.getGameboard().getGameGrid();
        validGrid=testGame.getValidGrid();
        selectItems=testPlayer.getSelectItems();
    }

    /**
     * Method to test if player is successfully created
     */
    @Test
    public void setTestPlayer(){
        assertNull(testPlayer.getNickname());
        assertEquals(0, testPlayer.getClientID());
        System.out.println("Nickname: " + testPlayer.getNickname());
        System.out.println("ClientID: " + testPlayer.getClientID());
    }

    /**
     * Method to test if isFirstPlayer and score attributes are successfully set
     */
    @Test
    public void setAttributesTestPlayer(){
        final int SHELF_ROWS=6;
        final int SHELF_COLUMNS=5;
        Item[][] expectedShelf= new Item[SHELF_ROWS][SHELF_COLUMNS];
        for (int i = 0; i < SHELF_ROWS; i++) {
            for (int j = 0; j < SHELF_COLUMNS; j++) {
                expectedShelf[i][j] =new Item(null,0);
            }
        }
        assertFalse(testPlayer.getIsFirstPlayer());
        assertEquals(0, testPlayer.getScore());
        for (int i = 0; i < SHELF_ROWS; i++) {
            for (int j = 0; j < SHELF_COLUMNS; j++) {
                assertEquals(expectedShelf[i][j].getCategoryType(), myShelf.getShelfGrid()[i][j].getCategoryType());
            }
        }
        System.out.println("FirstPlayer: " + testPlayer.getIsFirstPlayer());
        System.out.println("Score: " + testPlayer.getScore());
        for (int i = 0; i < myShelf.getShelfGrid().length; i++) {
            for (int j = 0; j < myShelf.getShelfGrid()[i].length; j++) {
                System.out.print(myShelf.getShelfGrid()[i][j].getCategoryType() + " ");
            }
            System.out.println();
        }

    }


    /**
     * Method to test if an item which has the next row free can be picked
     */
    @Test
    public void FreeSideNexTrowIsFreeTest(){
        final int ROW=3;
        final int COL=3;
        validGrid[ROW+1][COL]=1;
        for (int i = 0; i < validGrid.length; i++) {
            for (int j = 0; j < validGrid[i].length; j++) {
                System.out.print(validGrid[i][j] + " ");
            }
            System.out.println();
        }
        gameBoard[ROW+1][COL]=new Item(null,0);
        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard[i].length; j++) {
                System.out.print(gameBoard[i][j].getCategoryType() + " ");
            }
            System.out.println();
        }
        selectedCoords=new ArrayList<>();
        int[] coord1 = {ROW, COL};
        selectedCoords.add(coord1);
        assertDoesNotThrow(()->testPlayer.pickItems(selectedCoords,gameBoard,validGrid));
    }


    /**
     * Method to test if validGrid and GameGrid values are successfully set after pickItems call
     */
    @Test
    public void rightValuesInValidAndGameGridTest(){
        final int ROW=0;
        for (int[] ints : validGrid) {
            for (int j = 0; j < ints.length; j++) {
                System.out.print(ints[j] + " ");
            }
            System.out.println();
        }
        for (Item[] items : gameBoard) {
            for (int j = 0; j < items.length; j++) {
                System.out.print(items[j].getCategoryType() + " ");
            }
            System.out.println();
        }
        selectedCoords=new ArrayList<>();
        int[] coord1 = {ROW, 4};
        int[] coord2 = {ROW, 3};
        selectedCoords.add(coord1);
        selectedCoords.add(coord2);
        assertDoesNotThrow(()->testPlayer.pickItems(selectedCoords,gameBoard,validGrid));
        assertNull(gameBoard[ROW][4].getCategoryType(),"Item is not replaced by null");
        assertNull(gameBoard[ROW][3].getCategoryType(),"Item is not replaced by null");
        assertEquals(1,validGrid[ROW][3],"Value in validGrid is not 1");
        assertEquals(1,validGrid[ROW][4], "Value in validGrid is not 1");

        for (int i = 0; i < validGrid.length; i++) {
            for (int j = 0; j < validGrid[i].length; j++) {
                System.out.print(validGrid[i][j] + " ");
            }
            System.out.println();
        }
        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard[i].length; j++) {
                System.out.print(gameBoard[i][j].getCategoryType() + " ");
            }
            System.out.println();
        }
    }

    /**
     *  Method to test if selected items in GameBoard are the same in selectedItems list
     */
    @Test
    public void rightItemsInSelectItemsTest(){
        final int ROW = 0;
        for (int i = 0; i < validGrid.length; i++) {
            for (int j = 0; j < validGrid[i].length; j++) {
                System.out.print(validGrid[i][j] + " ");
            }
            System.out.println();
        }
        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard[i].length; j++) {
                System.out.print(gameBoard[i][j].getCategoryType() + " ");
            }
            System.out.println();
        }
        selectedCoords=new ArrayList<>();
        int[] coord1 = {ROW, 3};
        int[] coord2 = {ROW, 4};
        selectedCoords.add(coord1);
        selectedCoords.add(coord2);
        Item[] expectedItems = new Item[]{gameBoard[ROW][3], gameBoard[ROW][4]};

        assertDoesNotThrow(() -> testPlayer.pickItems(selectedCoords, gameBoard, validGrid));
        for (int i = 0; i < selectItems.size(); i++) {
            assertEquals(expectedItems[i].getCategoryType(), selectItems.get(i).getCategoryType(),
                    "Items in selectItems and in game Board are different");
        }

        for (int i = 0; i < validGrid.length; i++) {
            for (int j = 0; j < validGrid[i].length; j++) {
                System.out.print(validGrid[i][j] + " ");
            }
            System.out.println();
        }
        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard[i].length; j++) {
                System.out.print(gameBoard[i][j].getCategoryType() + " ");
            }
            System.out.println();
        }
        for (int i = 0; i < selectItems.size(); i++) {
            System.out.print(selectItems.get(i).getCategoryType() + " ");
        }
        System.out.println();
    }

    /**
     * Method to test if Items of selectedItems list are successfully placed in personal shelf
     */
    @Test
    public void rightPositionsOfItemsInMyShelfTest(){
        final int VALID_COLUMN=3;
        final int LAST_ROW=5;
        selectedCol=VALID_COLUMN;
        selectItems.add(new Item(Category.TROPHY,1));
        selectItems.add(new Item(Category.CAT,1));
        List<Item> expectedItems=new ArrayList<>();
        expectedItems.add(new Item(Category.TROPHY,1));
        expectedItems.add(new Item(Category.CAT,1));
        for(int i=0;i<selectItems.size();i++) {
            System.out.println(selectItems.get(i).getCategoryType() + " ");
        }

        assertDoesNotThrow(() -> testPlayer.putItemInShelf(selectedCol));
        assertEquals(expectedItems.get(0).getCategoryType(),myShelf.getShelfGrid()[LAST_ROW][VALID_COLUMN].getCategoryType(),
                "Item in selectItems and in myShelf are different");
        assertEquals(expectedItems.get(1).getCategoryType(),myShelf.getShelfGrid()[LAST_ROW-1][VALID_COLUMN].getCategoryType(),
                "Item in selectItems and in myShelf are different");

        for (int i = 0; i < myShelf.getShelfGrid().length; i++) {
            for (int j = 0; j < myShelf.getShelfGrid()[i].length; j++) {
                System.out.print(myShelf.getShelfGrid()[i][j].getCategoryType() + " ");
            }
            System.out.println();
        }
    }

    /**
     * Tests if the Shelf lastRows are updated correctly
     */
    @Test
    public void updateLastRowsTest(){
        int selectedItems = 1;
        int selectedColumn = 1;
        assertTrue(testPlayer.updateLastRows(selectedColumn,selectedItems),"The LastRow hasn't been updated correctly");
        selectedItems = 2;
        assertTrue(testPlayer.updateLastRows(selectedColumn,selectedItems),"The LastRow hasn't been updated correctly");
        selectedItems = 3;
        assertTrue(testPlayer.updateLastRows(selectedColumn,selectedItems),"The LastRow hasn't been updated correctly");
    }

    /**
     * Method to test if points are successfully added to personal score
     */
    @Test
    public void addPointsTest(){
        final int POINTS=10;
        testPlayer.addPoints(POINTS);

        assertEquals(POINTS,testPlayer.getScore(), "Score is not updated");
    }

    /**
     * setMyShelf set method test
     */
    @Test
    public void setMyShelfTest(){
        Shelf shelf = new Shelf();
        for(int i=0; i<6; i++){
            for(int j=0; j<5; j++){
                shelf.getShelfGrid()[i][j] = new Item(Category.CAT,1);
            }
        }
        testPlayer.setMyShelf(shelf);
        assertSame(testPlayer.getMyShelf(),shelf,"The shelf set is not the same");
    }

    /**
     * setFirstPlayer set method test
     */
    @Test
    public void setFirstPlayerTest(){
        testPlayer.setIsFirstPlayer();
        assertTrue(testPlayer.getIsFirstPlayer(),"isFirstPlayer is not true");
    }

    /**
     * setNickNameAndClientID set method test
     */
    @Test
    public void setNicknameAndClientIDTest(){
        final String NICKNAME = "Player0";
        final int CLIENTID = 10;
        assertNull(testPlayer.getNickname(), "The player name is not null");
        assertSame(0, testPlayer.getClientID(), "The player's clientID is not zero");

        testPlayer.setNicknameAndClientID(NICKNAME, CLIENTID);
        assertEquals(NICKNAME, testPlayer.getNickname(), "The player name is not set correctly");
        assertSame(CLIENTID, testPlayer.getClientID(), "The player's clientID is not set correctly");
    }

    /**
     * getMyPersonalObjCard get method test
     */
    @Test
    public void getMyPersonalObjCardTest(){
        PersonalObjCard personalObjCard = testPlayer.getMyPersonalOBjCard();
        assertSame(testPlayer.getMyPersonalOBjCard(),personalObjCard,"The PersonalObjCard gotten is not the same");
    }
}



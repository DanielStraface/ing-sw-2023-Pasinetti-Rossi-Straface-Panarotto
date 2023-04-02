package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.InvalidSelectionException;
import it.polimi.ingsw.model.personcard.PersonalObjCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PlayerTest {
    Player testPlayer;
    private Shelf myShelf;
    private PersonalObjCard myPersonalObjCard;
    private List<Item> selectItems;
    private boolean isFirstPlayer;
    private Game testGame;
    private List<int[]> selectedCoords;
    private int selectedCol;
    private List<Item> sortedItems;
    private Item[][] gameBoard;
    private int[][] validGrid;

    @BeforeEach
    void creationPlayerAndGame() throws Exception {
        final int NUM_OF_PLAYER = 4;
        testGame = new Game(NUM_OF_PLAYER);
        testPlayer = new Player();
        myShelf=testPlayer.getMyShelf();
        gameBoard = testGame.getGameboard().getGameGrid();
        validGrid=testGame.getValidGrid();
        selectItems=testPlayer.getSelectItems();
    }

    @Test
    public void setTestPlayer() throws Exception {
        assertNull(testPlayer.getNickname());
        assertEquals(0, testPlayer.getClientID());
        System.out.println("Nickname: " + testPlayer.getNickname());
        System.out.println("ClientID: " + testPlayer.getClientID());
    }

    @Test
    public void setAttributesTestPlayer() throws Exception {
        final int SHELF_ROWS=6;
        final int SHELF_COLUMNS=5;
        Item[][] expectedShelf= new Item[SHELF_ROWS][SHELF_COLUMNS];
        for (int i = 0; i < SHELF_ROWS; i++) {
            for (int j = 0; j < SHELF_COLUMNS; j++) {
                expectedShelf[i][j] =new Item(null);
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

    @Test
    public void emptyListOfCoordinates() throws Exception{
        selectedCoords=new ArrayList<>();
        Exception e= assertThrows(IllegalStateException.class, ()-> testPlayer.pickItems(selectedCoords,gameBoard,validGrid));
        assertEquals("selectedCoords is empty", e.getMessage());
    }

    @Test
    public void diffRowsAndColumnsTest() throws Exception{
        selectedCoords=new ArrayList<>();
        int[] coord1 = {0, 3};
        int[] coord2 = {1, 4};
        int[] coord3 = {2, 5};
        selectedCoords.add(coord1);
        selectedCoords.add(coord2);
        selectedCoords.add(coord3);
        Exception e=assertThrows(InvalidSelectionException.class,()-> testPlayer.pickItems(selectedCoords,gameBoard,validGrid));
        assertEquals("Invalid selection: no same rows or cols", e.getMessage());
    }

    @Test
    public void NoConsecutiveRowsTest() throws Exception{
        final int COLUMN=2;
        selectedCoords=new ArrayList<>();
        int[] coord1 = {2, COLUMN};
        int[] coord2 = {3, COLUMN};
        int[] coord3 = {5, COLUMN};
        selectedCoords.add(coord1);
        selectedCoords.add(coord2);
        selectedCoords.add(coord3);
        Exception e=assertThrows(InvalidSelectionException.class,()-> testPlayer.pickItems(selectedCoords,gameBoard,validGrid));
        assertEquals("Invalid selection: No consecutive selection", e.getMessage());
    }

    @Test
    public void NoConsecutiveColumnsTest() throws Exception{
        final int ROW=2;
        selectedCoords=new ArrayList<>();
        int[] coord1 = {ROW, 2};
        int[] coord2 = {ROW, 3};
        int[] coord3 = {ROW, 5};
        selectedCoords.add(coord1);
        selectedCoords.add(coord2);
        selectedCoords.add(coord3);
        Exception e=assertThrows(InvalidSelectionException.class,()-> testPlayer.pickItems(selectedCoords,gameBoard,validGrid));
        assertEquals("Invalid selection: No consecutive selection", e.getMessage());
    }

    @Test
    public void FreeSideTest() throws Exception{
        final int ROW=2;
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
        int[] coord1 = {ROW, 2};
        int[] coord2 = {ROW, 3};
        int[] coord3 = {ROW, 4};
        selectedCoords.add(coord1);
        selectedCoords.add(coord2);
        selectedCoords.add(coord3);
        Exception e=assertThrows(InvalidSelectionException.class,()-> testPlayer.pickItems(selectedCoords,gameBoard,validGrid));
        assertEquals("Invalid selection: no free sides", e.getMessage());
    }

    @Test
    public void FreeSideFirstROWTest() throws Exception{
        final int ROW=0;
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
        assertDoesNotThrow(()->testPlayer.pickItems(selectedCoords,gameBoard,validGrid));

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
    @Test
    public void FreeSideNextColIsFreeTest() throws Exception{
        final int ROW=3;
        final int COL=3;
        validGrid[ROW][COL+1]=1;
        for (int i = 0; i < validGrid.length; i++) {
            for (int j = 0; j < validGrid[i].length; j++) {
                System.out.print(validGrid[i][j] + " ");
            }
            System.out.println();
        }
        gameBoard[ROW][COL+1]=new Item(null);
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

    @Test
    public void FreeSideNexTrowIsFreeTest() throws Exception{
        final int ROW=3;
        final int COL=3;
        validGrid[ROW+1][COL]=1;
        for (int i = 0; i < validGrid.length; i++) {
            for (int j = 0; j < validGrid[i].length; j++) {
                System.out.print(validGrid[i][j] + " ");
            }
            System.out.println();
        }
        gameBoard[ROW+1][COL]=new Item(null);
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



    @Test
    public void rightValuesInValidAndGameGridTest() throws Exception{
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
        int[] coord1 = {ROW, 3};
        int[] coord2 = {ROW, 4};
        selectedCoords.add(coord1);
        selectedCoords.add(coord2);
        assertDoesNotThrow(()->testPlayer.pickItems(selectedCoords,gameBoard,validGrid));
        assertNull(gameBoard[ROW][3].getCategoryType(),"Item is not replaced by null");
        assertNull(gameBoard[ROW][4].getCategoryType(),"Item is not replaced by null");
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

    @Test
    public void rightItemsInSelectItemsTest() throws Exception {
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

    @Test
    public void copiedItemsTest() throws Exception{
        selectItems.add(new Item(Category.TROPHY));
        selectItems.add(new Item(Category.CAT));

        List<Item> expectedCopiedItems=new ArrayList<>();
        expectedCopiedItems.add(new Item(Category.TROPHY));
        expectedCopiedItems.add(new Item(Category.CAT));

        for (int i = 0; i < expectedCopiedItems.size(); i++) {
            System.out.print(expectedCopiedItems.get(i).getCategoryType() + " ");
        }
        System.out.println();

        List<Item> copiedItems= testPlayer.getCopiedItems();
        assertTrue(selectItems.isEmpty(), "SelectItems is not empty");
        for(int i=0;i<copiedItems.size();i++){
            assertEquals(expectedCopiedItems.get(i).getCategoryType(),copiedItems.get(i).getCategoryType(),
                    "Items in selectItems and copiedItems are diffrent");
        }
        for (int i = 0; i < copiedItems.size(); i++) {
            System.out.print(copiedItems.get(i).getCategoryType() + " ");
        }
        System.out.println();
        for (int i = 0; i < selectItems.size(); i++) {
            System.out.print(selectItems.get(i).getCategoryType() + " ");
        }
        System.out.println();
    }

    @Test
    public void noAvailableColumnTest() throws Exception{
        final int INVALID_COLUMN=6;
        selectedCol=INVALID_COLUMN;
        sortedItems= new ArrayList<>();
        sortedItems.add(new Item(Category.TROPHY));
        sortedItems.add(new Item(Category.CAT));

        Exception e=assertThrows(IndexOutOfBoundsException.class,()-> testPlayer.putItemInShelf(selectedCol,sortedItems));
        assertEquals("selectedCol must be less than 5", e.getMessage());

    }

    @Test
    public void rightPositionsOfItemsInMyShelfTest() throws Exception{
        final int VALID_COLUMN=3;
        final int LAST_ROW=5;
        selectedCol=VALID_COLUMN;
        sortedItems= new ArrayList<>();
        sortedItems.add(new Item(Category.TROPHY));
        sortedItems.add(new Item(Category.CAT));
        for(int i=0;i<sortedItems.size();i++) {
            System.out.println(sortedItems.get(i).getCategoryType() + " ");
        }

        assertDoesNotThrow(() -> testPlayer.putItemInShelf(selectedCol,sortedItems));
        assertEquals(sortedItems.get(0).getCategoryType(),myShelf.getShelfGrid()[LAST_ROW][VALID_COLUMN].getCategoryType(),
                "Item in sortedItems and in myShelf are different");
        assertEquals(sortedItems.get(1).getCategoryType(),myShelf.getShelfGrid()[LAST_ROW-1][VALID_COLUMN].getCategoryType(),
                "Item in sortedItems and in myShelf are different");

        for (int i = 0; i < myShelf.getShelfGrid().length; i++) {
            for (int j = 0; j < myShelf.getShelfGrid()[i].length; j++) {
                System.out.print(myShelf.getShelfGrid()[i][j].getCategoryType() + " ");
            }
            System.out.println();
        }
    }

    @Test
    public void addPointsTest() throws Exception{
        final int POINTS=10;
        testPlayer.addPoints(POINTS);

        assertEquals(POINTS,testPlayer.getScore(), "Score is not updated");
    }

    @Test
    public void setFirstPlayerTest(){
        testPlayer.setIsFirstPlayer();
        assertTrue(testPlayer.getIsFirstPlayer(),"isFirstPlayer is not true");
    }

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
}



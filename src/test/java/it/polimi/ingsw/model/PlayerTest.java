package it.polimi.ingsw.model;

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
    private int[][] selectedCoords;
    private int selectedCol;
    private List<Item> sortedItems;
    private Item[][] gameBoard;
    private int[][] validGrid;

    @BeforeEach
    void creationPlayerAndGame() throws Exception {
        final int NUM_OF_PLAYER = 4;
        testGame = new Game(NUM_OF_PLAYER);
        testPlayer = new Player("Player1", 1);
        myShelf=testPlayer.getMyShelf();
        gameBoard = testGame.getGameboard().getGameGrid();
        validGrid=testGame.getValidGrid();
        selectItems=testPlayer.getSelectItems();
    }

    @Test
    public void setTestPlayer() throws Exception {
        assertEquals("Player1", testPlayer.getNickname());
        assertEquals(1, testPlayer.getClientID());
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
        assertEquals(false, testPlayer.getIsFirstPlayer());
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
    public void diffRowsAndColumnsTest() throws Exception{
        selectedCoords=new int[][]{{1,2},{2,3},{3,4}};
        Exception e=assertThrows(Exception.class,()-> testPlayer.pickItems(selectedCoords,gameBoard,validGrid));
        assertEquals("Invalid selection: no same rows or cols", e.getMessage());
    }

    @Test
    public void NoConsecutiveRowsTest() throws Exception{
        final int COLUMN=2;
        selectedCoords=new int[][]{{1,COLUMN},{2,COLUMN},{4,COLUMN}};
        Exception e=assertThrows(Exception.class,()-> testPlayer.pickItems(selectedCoords,gameBoard,validGrid));
        assertEquals("Invalid selection: No consecutive selection", e.getMessage());
    }

    @Test
    public void NoConsecutiveColumnsTest() throws Exception{
        final int ROW=2;
        selectedCoords=new int[][]{{ROW,1},{ROW,2},{ROW,4}};
        Exception e=assertThrows(Exception.class,()-> testPlayer.pickItems(selectedCoords,gameBoard,validGrid));
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
                if(gameBoard[i][j] != null)
                    System.out.print(gameBoard[i][j].getCategoryType() + " ");
                else System.out.println("null");
            }
            System.out.println();
        }
        selectedCoords=new int[][]{{ROW,1},{ROW,2},{ROW,3}};
        Exception e=assertThrows(Exception.class,()-> testPlayer.pickItems(selectedCoords,gameBoard,validGrid));
        assertEquals("Invalid selection: no free sides", e.getMessage());
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
                if (items[j] != null)
                    System.out.print(items[j].getCategoryType() + " ");
                else System.out.println("null");
            }
            System.out.println();
        }
        selectedCoords=new int[][]{{ROW,3},{ROW,4}};
        assertDoesNotThrow(()->testPlayer.pickItems(selectedCoords,gameBoard,validGrid));
        assertNull(gameBoard[ROW][3].getCategoryType());
        assertNull(gameBoard[ROW][4].getCategoryType());
        assertEquals(1,validGrid[ROW][3]);
        assertEquals(1,validGrid[ROW][4]);

        for (int i = 0; i < validGrid.length; i++) {
            for (int j = 0; j < validGrid[i].length; j++) {
                System.out.print(validGrid[i][j] + " ");
            }
            System.out.println();
        }
        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard[i].length; j++) {
                if(gameBoard[i][j] != null)
                    System.out.print(gameBoard[i][j].getCategoryType() + " ");
                else System.out.println("null");
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
                if (gameBoard[i][j] != null)
                    System.out.print(gameBoard[i][j].getCategoryType() + " ");
                else System.out.println("null");
            }
            System.out.println();
        }
        selectedCoords = new int[][]{{ROW, 3}, {ROW, 4}};
        Item[] expectedItems = new Item[]{gameBoard[ROW][3], gameBoard[ROW][4]};

        assertDoesNotThrow(() -> testPlayer.pickItems(selectedCoords, gameBoard, validGrid));
        for (int i = 0; i < selectItems.size(); i++) {
            assertEquals(expectedItems[i].getCategoryType(), selectItems.get(i).getCategoryType());
        }

        for (int i = 0; i < validGrid.length; i++) {
            for (int j = 0; j < validGrid[i].length; j++) {
                System.out.print(validGrid[i][j] + " ");
            }
            System.out.println();
        }
        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard[i].length; j++) {
                if (gameBoard[i][j] != null)
                    System.out.print(gameBoard[i][j].getCategoryType() + " ");
                else System.out.println("null");
            }
            System.out.println();
        }
        for (int i = 0; i < selectItems.size(); i++) {
            if (selectItems.get(i) != null)
                System.out.print(selectItems.get(i).getCategoryType() + " ");
            else System.out.println("null");
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

        Exception e=assertThrows(Exception.class,()-> testPlayer.putItemInShelf(selectedCol,sortedItems));
        assertEquals("selectedCol must be less than 5", e.getMessage());

    }

    @Test
    public void rightPositionsOfItemsInMyShelfTest() throws Exception{
        final int VALID_COLUMN=3;
        selectedCol=VALID_COLUMN;
        sortedItems= new ArrayList<>();
        sortedItems.add(new Item(Category.TROPHY));
        sortedItems.add(new Item(Category.CAT));
        assertDoesNotThrow(() -> testPlayer.putItemInShelf(selectedCol,sortedItems));
    }
}



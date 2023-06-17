package it.polimi.ingsw.model.comcard;

import it.polimi.ingsw.model.Category;
import it.polimi.ingsw.model.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RowsColumnsCardTest {
    private static final int ROWS = 6;
    private static final int COLS = 5;
    private static final int TYPE_ROWS = 6;
    private static final int TYPE_COLS = 2;
    private RowsColumnsCard card;
    private Item[][] grid;

    @BeforeEach
    public void setup(){
        card = null;
        grid = new Item[ROWS][COLS];
        for(int i=0;i<ROWS;i++){
            for(int j=0;j<COLS;j++){
                grid[i][j] = new Item(null,0);
            }
        }
    }

    @Test
    public void nullGridTest(){
        /* The grid is null. check() to return false */
        final int TYPE_CORNERS = 3;
        card = new RowsColumnsCard(TYPE_CORNERS);
        assertFalse(card.check(grid));
    }

    @Test
    public void colsCheckerOkTest(){
        /* The grid contains three columns with six different category types. check() to return true */
        card = new RowsColumnsCard(TYPE_COLS);
        List<Category> itemList = new LinkedList<>(Arrays.asList(Category.values()));
        for(int i=0;i<3;i++){
            for(int j=0;j<ROWS;j++){
                grid[j][i] = new Item(itemList.get(j),1);
            }
        }
        assertTrue(card.check(grid), "The grid contains full columns with a repeat category type");
    }

    @Test
    public void colsCheckerFailTest(){
        /* The grid contains one column with six different category types and another with a category type repetition.
           check() to return false */
        card = new RowsColumnsCard(TYPE_COLS);
        List<Category> itemList = new LinkedList<>(Arrays.asList(Category.values()));
        for(int i=0;i<2;i++){
            for(int j=0;j<ROWS;j++){
                grid[j][i+2] = new Item(itemList.get(j),1);
            }
        }
        grid[4][3] = new Item(Category.CAT,1);
        assertFalse(card.check(grid), "The grid contains full columns with no repetition");
    }

    @Test
    public void rowsCheckerOkTest(){
        /* The grid contains three rows with five different category types. check() to return true */
        card = new RowsColumnsCard(TYPE_ROWS);
        List<Category> itemList = new LinkedList<>(Arrays.asList(Category.values()));
        for(int i=0;i<3;i++){
            for(int j=0;j<COLS;j++){
                grid[i][j] = new Item(itemList.get(j),1);
            }
        }
        assertTrue(card.check(grid), "The grid contains rows with a repeat category type");
    }

    @Test
    public void rowsCheckerFailTest(){
        /* The grid contains one rows with five different category types and a one column with a category repetition.
           check() to return true */
        card = new RowsColumnsCard(TYPE_ROWS);
        List<Category> itemList = new LinkedList<>(Arrays.asList(Category.values()));
        for(int i=0;i<2;i++){
            for(int j=0;j<COLS;j++){
                grid[i][j] = new Item(itemList.get(j),1);
            }
        }
        grid[0][0] = new Item(Category.BOOK,1);
        assertFalse(card.check(grid), "The grid contains rows with no repeat category type");
    }
}

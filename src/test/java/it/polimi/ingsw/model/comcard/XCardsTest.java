package it.polimi.ingsw.model.comcard;

import it.polimi.ingsw.model.Category;
import it.polimi.ingsw.model.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class XCardsTest {
    private static final int TYPE_X = 10;
    private static final int ROWS = 6;
    private static final int COLS = 5;
    private XCards card;
    private Item[][] grid;

    @BeforeEach
    public void setup(){
        card = null;
        card = new XCards(TYPE_X);
        grid = new Item[ROWS][COLS];
        for(int i=0;i<ROWS;i++){
            for(int j=0;j<COLS;j++){
                grid[i][j] = new Item(null,0);
            }
        }
    }

    @Test
    public void gridNullTest(){
        /* The item grid is null. check() to return false */
        assertFalse(card.check(grid), "The grid contains an x");
    }

    @Test
    public void gridWithTwoX(){
        /* The item grid contains two x. check() to return true */
        grid[1][2] = new Item(Category.CAT,1);
        grid[0][1] = new Item(Category.CAT,1);
        grid[0][3] = new Item(Category.CAT,1);
        grid[2][2] = new Item(Category.CAT,1);
        grid[2][3] = new Item(Category.CAT,1);

        grid[4][2] = new Item(Category.BOOK,1);
        grid[3][1] = new Item(Category.BOOK,1);
        grid[3][3] = new Item(Category.BOOK,1);
        grid[5][1] = new Item(Category.BOOK,1);
        grid[5][3] = new Item(Category.BOOK,1);

        assertTrue(card.check(grid), "The grid doesn't contain an x");
    }
}

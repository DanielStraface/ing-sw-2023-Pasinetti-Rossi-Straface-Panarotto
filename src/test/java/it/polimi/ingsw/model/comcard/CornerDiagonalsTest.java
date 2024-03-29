package it.polimi.ingsw.model.comcard;

import it.polimi.ingsw.model.Category;
import it.polimi.ingsw.model.Item;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class CornerDiagonalsTest tests CornerDiagonals class
 * @see CornerDiagonals
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CornerDiagonalsTest {
    private static final int TYPE_X = 10;
    private static final int TYPE_CORNERS = 8;
    private static final int ROWS = 6;
    private static final int COLS = 5;
    private static final int TYPE_DIAGONALS = 11;
    private static final int TYPE_DESC_MATRIX = 12;
    private CornerDiagonals card;
    private Item[][] grid0, grid1, grid2, grid3, grid4, grid5;

    /**
     * Setup Method for all tests
     */
    @BeforeAll
    public void setupForAll(){
        /* creation of grids with all item category equals null*/
        grid0 = new Item[ROWS][COLS];
        grid1 = new Item[ROWS][COLS];
        grid2 = new Item[ROWS][COLS];
        grid3 = new Item[ROWS][COLS];
        grid4 = new Item[ROWS][COLS];
        grid5 = new Item[ROWS][COLS];
        for(int i=0;i<ROWS;i++){
            for(int j=0;j<COLS;j++){
                grid0[i][j] = new Item(null,0);
                grid1[i][j] = new Item(null,0);
                grid2[i][j] = new Item(null,0);
                grid3[i][j] = new Item(null,0);
                grid4[i][j] = new Item(null,0);
                grid5[i][j] = new Item(null,0);
            }
        }
    }

    /**
     * Setup method for all tests
     */
    @BeforeEach
    public void setup(){
        card = null;
    }

    /**
     * Checks if the Card n.8's objective correctly fails
     */
    @Test
    public void checkCornersFailNull(){
        /* In the corners there is null type. check() to return false */
        card = new CornerDiagonals(TYPE_CORNERS);
        assertFalse(card.check(grid0));
    }

    /**
     * Checks if the Card n.8's objective is correctly reached
     */
    @Test
    public void checkCornersOkTest(){
        /* The corner has got the same category type. check() to return true */
        card = new CornerDiagonals(TYPE_CORNERS);
        grid1[0][0] = new Item(Category.TROPHY,1);
        grid1[0][4] = new Item(Category.TROPHY,1);
        grid1[5][0] = new Item(Category.TROPHY,1);
        grid1[5][4] = new Item(Category.TROPHY,1);
        assertTrue(card.check(grid1), "Wrong: there is a different corner");
    }

    /**
     * Checks if the Card n.8's objective correctly fails
     */
    @Test
    public void checkCornersFailTest(){
        /* One of the corners has got a null category type. check() to return false */
        card = new CornerDiagonals(TYPE_CORNERS);
        grid1[0][0] = new Item(Category.TROPHY,1);
        grid1[0][4] = new Item(null,0);
        grid1[5][0] = new Item(Category.CAT,1);
        grid1[5][4] = new Item(Category.TROPHY,1);
        assertFalse(card.check(grid1), "Wrong: the corners are the same");
    }

    /**
     * Checks if the Card n.11's objective is correctly reached
     */
    @Test
    public void checkDiagonalsOkTest(){
        /* The diagonals contain the same item category type not null. check() to return true */
        card = new CornerDiagonals(TYPE_DIAGONALS);
        for(int i=0;i<5;i++){
            grid2[i][i] = new Item(Category.FRAME,1);
            grid2[i+1][i] = new Item(Category.GAME,1);
        }
        assertTrue(card.check(grid2), "The main diagonals contain different types");
        for(int i=0;i<5;i++){
            grid2[i][4-i] = new Item(Category.PLANT,1);
            grid2[i+1][4-i] = new Item(null,0);
        }
        assertTrue(card.check(grid2), "The reverse diagonals contain different types");
    }

    /**
     * Checks if the Card n.11's objective correctly fails
     */
    @Test
    public void checkDiagonalsFailTest(){
        /* The diagonals contain all null category type and a different item category type. check() to return false */
        /* fail the main diagonals */
        card = new CornerDiagonals(TYPE_DIAGONALS);
        assertFalse(card.check(grid3), "The main diagonals are not null");
        for(int i=0;i<5;i++) grid3[i][i] = new Item(Category.FRAME,1);
        grid3[2][2] = new Item(Category.CAT,1);
        assertFalse(card.check(grid3), "The reverse diagonals contains the same element");
    }

    /**
     * Checks if the Card n.11's objective correctly fails
     */
    @Test
    public void checkOppositeDiagonalsFail(){
        /* The top right cells of the diagonals are not null, but the rest is null. check() to return false */
        /* fail the other opposite diagonals */
        card = new CornerDiagonals(TYPE_DIAGONALS);
        for(int i=0;i<ROWS;i++){
            for(int j=0;j<COLS;j++){
                grid3[i][j] = new Item(null,0);
            }
        }
        grid3[0][4] = new Item(Category.CAT,1);
        grid3[1][4] = new Item(Category.BOOK,1);
        assertFalse(card.check(grid0), "The grid contains item category != null");
    }

    /**
     * Checks if the Card n.12's objective is correctly reached
     */
    @Test
    public void checkDescMatrixOkTest(){
        /* The grid contains a "stair of items". check() to return true */
        card = new CornerDiagonals(TYPE_DESC_MATRIX);
        /* controls the main diagonals */
        for(int i=0;i<5;i++){
            grid4[i+1][i] = new Item(Category.PLANT,1);
        }
        grid4[3][2] = new Item(Category.TROPHY,1);
        assertTrue(card.check(grid4), "The main diagonal contains a null type");

        /* controls the opposite diagonals */
        for(int i=0;i<5;i++){
            grid4[i+1][4-i] = new Item(Category.BOOK,1);
        }
        grid4[3][2] = new Item(Category.GAME,1);
        assertTrue(card.check(grid4), "The opposite diagonal contains a null type");
    }

    /**
     * Checks if the Card n.12's objective correctly fails
     */
    @Test
    public void checkDescMatrixFailTest(){
        /* The grid items type is all null and there is a null category type in opposite diagonal.
        check() to return false */
        card = new CornerDiagonals(TYPE_DESC_MATRIX);
        /* controls the main diagonals */
        assertFalse(card.check(grid5), "The main diagonal contains for all position a category type != null");

        /* controls the opposite diagonals */
        for(int i=0;i<5;i++){
            grid5[i+1][4-i] = new Item(Category.CAT,1);
        }
        grid5[2][3] = new Item(null,0);
        assertFalse(card.check(grid5),
                "The opposite diagonal contains in a certain position a category type != null");
    }

    /**
     * Checks if an incorrect card type is rejected
     */
    @Test
    public void differentTypeNotManagedByThis(){
        /* The this.type of the card is not supported by this implementation. check() return false
           Maybe will be introduced an Exception */
        card = new CornerDiagonals(TYPE_X);
        assertFalse(card.check(grid0), "This type is not managed by this card");
    }
}

package it.polimi.ingsw.model.comcard;

import it.polimi.ingsw.model.Category;
import it.polimi.ingsw.model.Item;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

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
                grid0[i][j] = new Item(null);
                grid1[i][j] = new Item(null);
                grid2[i][j] = new Item(null);
                grid3[i][j] = new Item(null);
                grid4[i][j] = new Item(null);
                grid5[i][j] = new Item(null);
            }
        }
    }
    @BeforeEach
    public void setup(){
        card = null;
    }

    @Test
    public void checkCornersFailNull(){
        /* In the corners there is null type. check() to return false */
        card = new CornerDiagonals(TYPE_CORNERS);
        assertFalse(card.check(grid0));
    }

    @Test
    public void checkCornersOkTest(){
        /* The corner has got the same category type. check() to return true */
        card = new CornerDiagonals(TYPE_CORNERS);
        grid1[0][0] = new Item(Category.TROPHY);
        grid1[0][4] = new Item(Category.TROPHY);
        grid1[5][0] = new Item(Category.TROPHY);
        grid1[5][4] = new Item(Category.TROPHY);
        assertTrue(card.check(grid1), "Wrong: there is a different corner");
    }

    @Test
    public void checkCornersFailTest(){
        /* One of the corners has got a null category type. check() to return false */
        card = new CornerDiagonals(TYPE_CORNERS);
        grid1[0][0] = new Item(Category.TROPHY);
        grid1[0][4] = new Item(null);
        grid1[5][0] = new Item(Category.CAT);
        grid1[5][4] = new Item(Category.TROPHY);
        assertFalse(card.check(grid1), "Wrong: the corners are the same");
    }

    @Test
    public void checkDiagonalsOkTest(){
        /* The diagonals contain the same item category type not null. check() to return true */
        card = new CornerDiagonals(TYPE_DIAGONALS);
        for(int i=0;i<5;i++){
            grid2[i][i] = new Item(Category.FRAME);
            grid2[i+1][i] = new Item(Category.GAME);
        }
        assertTrue(card.check(grid2), "The main diagonals contain different types");
        for(int i=0;i<5;i++){
            grid2[i][4-i] = new Item(Category.PLANT);
            grid2[i+1][4-i] = new Item(null);
        }
        assertTrue(card.check(grid2), "The reverse diagonals contain different types");
    }

    @Test
    public void checkDiagonalsFailTest(){
        /* The diagonals contain all null category type and a different item category type. check() to return false */
        /* fail the main diagonals */
        card = new CornerDiagonals(TYPE_DIAGONALS);
        assertFalse(card.check(grid3), "The main diagonals are not null");
        for(int i=0;i<5;i++) grid3[i][i] = new Item(Category.FRAME);
        grid3[2][2] = new Item(Category.CAT);
        assertFalse(card.check(grid3), "The reverse diagonals contains the same element");
    }

    @Test
    public void checkOppositeDiagonalsFail(){
        /* The top right cells of the diagonals are not null, but the rest is null. check() to return false */
        /* fail the other opposite diagonals */
        card = new CornerDiagonals(TYPE_DIAGONALS);
        for(int i=0;i<ROWS;i++){
            for(int j=0;j<COLS;j++){
                grid3[i][j] = new Item(null);
            }
        }
        grid3[0][4] = new Item(Category.CAT);
        grid3[1][4] = new Item(Category.BOOK);
        assertFalse(card.check(grid0), "The grid contains item category != null");
    }

    @Test
    public void checkDescMatrixOkTest(){
        /* The grid contains a "stair of items". check() to return true */
        card = new CornerDiagonals(TYPE_DESC_MATRIX);
        /* controls the main diagonals */
        for(int i=0;i<5;i++){
            grid4[i+1][i] = new Item(Category.PLANT);
        }
        grid4[3][2] = new Item(Category.TROPHY);
        assertTrue(card.check(grid4), "The main diagonal contains a null type");

        /* controls the opposite diagonals */
        for(int i=0;i<5;i++){
            grid4[i+1][4-i] = new Item(Category.BOOK);
        }
        grid4[3][2] = new Item(Category.GAME);
        assertTrue(card.check(grid4), "The opposite diagonal contains a null type");
    }

    @Test
    public void checkDescMatrixFailTest(){
        /* The grid items type is all null and there is a null category type in opposite diagonal.
        check() to return false */
        card = new CornerDiagonals(TYPE_DESC_MATRIX);
        /* controls the main diagonals */
        assertFalse(card.check(grid5), "The main diagonal contains for all position a category type != null");

        /* controls the opposite diagonals */
        for(int i=0;i<5;i++){
            grid5[i+1][4-i] = new Item(Category.CAT);
        }
        grid5[2][3] = new Item(null);
        assertFalse(card.check(grid5),
                "The opposite diagonal contains in a certain position a category type != null");
    }

    @Test
    public void differentTypeNotManagedByThis(){
        /* The this.type of the card is not supported by this implementation. check() return false
           Maybe will be introduced an Exception */
        card = new CornerDiagonals(TYPE_X);
        assertFalse(card.check(grid0), "This type is not managed by this card");
    }
}

package it.polimi.ingsw.model.comcard;

import it.polimi.ingsw.model.Category;
import it.polimi.ingsw.model.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;


import static org.junit.jupiter.api.Assertions.*;

/**
 * Class ThreeDifferentTypesTest tests ThreeDifferentTypes class
 * @see ThreeDifferentTypes
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ThreeDifferentTypesTest {

    private static final int R0 = 0, R1 = 1, R3 = 3, R5= 5;
    private static final int C0 = 0, C2 = 2, C4 = 4;
    private static final int DIFFERENT_ROWS = 7;
    private static final int DIFFERENT_COLS = 5;
    private static final int ROWS = 6;
    private static final int COLS = 5;
    private Item[][] grid;
    private ThreeDifferentTypes card;

    /**
     * Setup method for all tests
     */
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


    /**
     * Checks CommonObjectiveCard n.7 conditions on an empty GameBoard
     */
    @Test
    public void gridNullRowsTest(){
        card = new ThreeDifferentTypes(DIFFERENT_ROWS);
        assertFalse(card.check(grid), "The grid satisfies the CommonObjectiveCard n.7");
    }


    /**
     *  Checks CommonObjectiveCard n.5 conditions on an empty GameBoard
     */
    @Test
    public void gridNullColumnsTest(){
        card = new ThreeDifferentTypes(DIFFERENT_COLS);
        assertFalse(card.check(grid), "The grid satisfies the CommonObjectiveCard n.5");
    }


    /**
     * Verifies that the check method returns a true boolean when the
     * CommonObjectiveCard n.7's condition is met
     */
    @Test
    public void checkDifferentRowsOkTest(){
        card = new ThreeDifferentTypes(DIFFERENT_ROWS);

        // Row 0 has 3 different categories, Row 1 has 2, Row 3 has 1, Row 5 has 2

        grid[R0][0] = new Item(Category.CAT,1);
        grid[R0][1] = new Item(Category.CAT,1);
        grid[R0][2] = new Item(Category.FRAME,1);
        grid[R0][3] = new Item(Category.FRAME,1);
        grid[R0][4] = new Item(Category.GAME,1);

        grid[R1][0] = new Item(Category.BOOK,1);
        grid[R1][1] = new Item(Category.PLANT,1);
        grid[R1][2] = new Item(Category.PLANT,1);
        grid[R1][3] = new Item(Category.BOOK,1);
        grid[R1][4] = new Item(Category.BOOK,1);

        grid[R3][0] = new Item(Category.FRAME,1);
        grid[R3][1] = new Item(Category.FRAME,1);
        grid[R3][2] = new Item(Category.FRAME,1);
        grid[R3][3] = new Item(Category.FRAME,1);
        grid[R3][4] = new Item(Category.FRAME,1);

        grid[R5][0] = new Item(Category.CAT,1);
        grid[R5][1] = new Item(Category.BOOK,1);
        grid[R5][2] = new Item(Category.CAT,1);
        grid[R5][3] = new Item(Category.BOOK,1);
        grid[R5][4] = new Item(Category.CAT,1);

        assertTrue(card.check(grid),"The grid doesn't satisfy the CommonObjectiveCard n.7");
    }


    /**
     * Various test methods to verify that the check method returns a false boolean when the
     * CommonObjectiveCard n.7's conditions aren't met
     */
    @Test
    public void checkDifferentRowsFailTest1(){
        card = new ThreeDifferentTypes(DIFFERENT_ROWS);

        // The row 0 has 4 different category types, 3 is the maximum allowed
        grid[R0][0] = new Item(Category.CAT,1);
        grid[R0][1] = new Item(Category.CAT,1);
        grid[R0][2] = new Item(Category.FRAME,1);
        grid[R0][3] = new Item(Category.PLANT,1);
        grid[R0][4] = new Item(Category.GAME,1);

        grid[R1][0] = new Item(Category.BOOK,1);
        grid[R1][1] = new Item(Category.PLANT,1);
        grid[R1][2] = new Item(Category.PLANT,1);
        grid[R1][3] = new Item(Category.BOOK,1);
        grid[R1][4] = new Item(Category.BOOK,1);

        grid[R3][0] = new Item(Category.FRAME,1);
        grid[R3][1] = new Item(Category.FRAME,1);
        grid[R3][2] = new Item(Category.FRAME,1);
        grid[R3][3] = new Item(Category.FRAME,1);
        grid[R3][4] = new Item(Category.FRAME,1);

        grid[R5][0] = new Item(Category.CAT,1);
        grid[R5][1] = new Item(Category.BOOK,1);
        grid[R5][2] = new Item(Category.CAT,1);
        grid[R5][3] = new Item(Category.BOOK,1);
        grid[R5][4] = new Item(Category.CAT,1);

        assertFalse(card.check(grid),"The grid satisfies the CommonObjectiveCard n.7");
    }


    /**
     * Checks if the Card n.7's objective correctly fails
     */
    @Test
    public void checkdifferentRowsFailTest2(){
        card = new ThreeDifferentTypes(DIFFERENT_ROWS);

        //Only 3 out of 4 rows required satisfy the condition

        grid[R1][0] = new Item(Category.BOOK,1);
        grid[R1][1] = new Item(Category.PLANT,1);
        grid[R1][2] = new Item(Category.PLANT,1);
        grid[R1][3] = new Item(Category.BOOK,1);
        grid[R1][4] = new Item(Category.BOOK,1);

        grid[R3][0] = new Item(Category.FRAME,1);
        grid[R3][1] = new Item(Category.FRAME,1);
        grid[R3][2] = new Item(Category.FRAME,1);
        grid[R3][3] = new Item(Category.FRAME,1);
        grid[R3][4] = new Item(Category.FRAME,1);

        grid[R5][0] = new Item(Category.CAT,1);
        grid[R5][1] = new Item(Category.BOOK,1);
        grid[R5][2] = new Item(Category.CAT,1);
        grid[R5][3] = new Item(Category.BOOK,1);
        grid[R5][4] = new Item(Category.CAT,1);

        assertFalse(card.check(grid),"The grid satisfies the CommonObjectiveCard n.7");
    }

    /**
     * Checks if the Card n.7's objective correctly fails
     */
    @Test
    public void checkdifferentRowsFailTest3(){
        card = new ThreeDifferentTypes(DIFFERENT_ROWS);

        // The row 1 isn't completely filled
        grid[R0][0] = new Item(Category.CAT,1);
        grid[R0][1] = new Item(Category.CAT,1);
        grid[R0][2] = new Item(Category.FRAME,1);
        grid[R0][3] = new Item(Category.PLANT,1);
        grid[R0][4] = new Item(Category.GAME,1);

        grid[R1][0] = new Item(Category.BOOK,1);
        grid[R1][1] = new Item(Category.PLANT,1);
        grid[R1][2] = new Item(Category.PLANT,1);
        grid[R1][4] = new Item(Category.BOOK,1);

        grid[R3][0] = new Item(Category.FRAME,1);
        grid[R3][1] = new Item(Category.FRAME,1);
        grid[R3][2] = new Item(Category.FRAME,1);
        grid[R3][3] = new Item(Category.FRAME,1);
        grid[R3][4] = new Item(Category.FRAME,1);

        grid[R5][0] = new Item(Category.CAT,1);
        grid[R5][1] = new Item(Category.BOOK,1);
        grid[R5][2] = new Item(Category.CAT,1);
        grid[R5][3] = new Item(Category.BOOK,1);
        grid[R5][4] = new Item(Category.CAT,1);

        assertFalse(card.check(grid),"The grid satisfies the CommonObjectiveCard n.7");
    }


    /**
     * Verifies that the check method returns a true boolean when the
     * CommonObjectiveCard n.5's condition is met
     */
    @Test
    public void checkDifferentColumnsOkTest(){
        card = new ThreeDifferentTypes(DIFFERENT_COLS);

        // Column 0 has 3 different categories, Column 2 has 2, Column 4 has 1

        grid[0][C0] = new Item(Category.CAT,1);
        grid[1][C0] = new Item(Category.CAT,1);
        grid[2][C0] = new Item(Category.FRAME,1);
        grid[3][C0] = new Item(Category.PLANT,1);
        grid[4][C0] = new Item(Category.CAT,1);
        grid[5][C0] = new Item(Category.PLANT,1);

        grid[0][C2] = new Item(Category.CAT,1);
        grid[1][C2] = new Item(Category.CAT,1);
        grid[2][C2] = new Item(Category.PLANT,1);
        grid[3][C2] = new Item(Category.PLANT,1);
        grid[4][C2] = new Item(Category.CAT,1);
        grid[5][C2] = new Item(Category.PLANT,1);

        grid[0][C4] = new Item(Category.BOOK,1);
        grid[1][C4] = new Item(Category.BOOK,1);
        grid[2][C4] = new Item(Category.BOOK,1);
        grid[3][C4] = new Item(Category.BOOK,1);
        grid[4][C4] = new Item(Category.BOOK,1);
        grid[5][C4] = new Item(Category.BOOK,1);

        assertTrue(card.check(grid),"The grid doesn't satisfy the CommonObjectiveCard n.5");
    }


    /**
     * Various test methods to verify that the check method returns a false boolean when the
     * CommonObjectiveCard n.5's conditions aren't met
     */
    @Test
    public void checkDifferentColumnsFailTest1(){
        card = new ThreeDifferentTypes(DIFFERENT_COLS);

        grid[0][C0] = new Item(Category.CAT,1);
        grid[1][C0] = new Item(Category.CAT,1);
        grid[2][C0] = new Item(Category.FRAME,1);
        grid[3][C0] = new Item(Category.PLANT,1);
        grid[4][C0] = new Item(Category.CAT,1);
        grid[5][C0] = new Item(Category.PLANT,1);

        // Column 2 has 4 different categories, the maximum allowed is 3
        grid[0][C2] = new Item(Category.FRAME,1);
        grid[1][C2] = new Item(Category.CAT,1);
        grid[2][C2] = new Item(Category.PLANT,1);
        grid[3][C2] = new Item(Category.PLANT,1);
        grid[4][C2] = new Item(Category.CAT,1);
        grid[5][C2] = new Item(Category.BOOK,1);

        grid[0][C4] = new Item(Category.BOOK,1);
        grid[1][C4] = new Item(Category.BOOK,1);
        grid[2][C4] = new Item(Category.BOOK,1);
        grid[3][C4] = new Item(Category.BOOK,1);
        grid[4][C4] = new Item(Category.BOOK,1);
        grid[5][C4] = new Item(Category.BOOK,1);

        assertFalse(card.check(grid),"The grid satisfies the CommonObjectiveCard n.5");

    }

    /**
     * Checks if the Card n.5's objective correctly fails
     */
    @Test
    public void checkDifferentColumnsFailTest2(){
        card = new ThreeDifferentTypes(DIFFERENT_COLS);

        // Only 2 out of 3 columns satisfy the condition

        grid[0][C0] = new Item(Category.CAT,1);
        grid[1][C0] = new Item(Category.CAT,1);
        grid[2][C0] = new Item(Category.FRAME,1);
        grid[3][C0] = new Item(Category.PLANT,1);
        grid[4][C0] = new Item(Category.CAT,1);
        grid[5][C0] = new Item(Category.PLANT,1);

        grid[0][C4] = new Item(Category.BOOK,1);
        grid[1][C4] = new Item(Category.BOOK,1);
        grid[2][C4] = new Item(Category.BOOK,1);
        grid[3][C4] = new Item(Category.BOOK,1);
        grid[4][C4] = new Item(Category.BOOK,1);
        grid[5][C4] = new Item(Category.BOOK,1);

        assertFalse(card.check(grid),"The grid satisfies the CommonObjectiveCard n.5");
    }

    /**
     * Checks if the Card n.5's objective correctly fails
     */
    @Test
    public void checkDifferentColumnsFailTest3(){
        card = new ThreeDifferentTypes(DIFFERENT_COLS);

        grid[0][C0] = new Item(Category.CAT,1);
        grid[1][C0] = new Item(Category.CAT,1);
        grid[2][C0] = new Item(Category.FRAME,1);
        grid[3][C0] = new Item(Category.PLANT,1);
        grid[4][C0] = new Item(Category.CAT,1);
        grid[5][C0] = new Item(Category.PLANT,1);

        grid[0][C2] = new Item(Category.FRAME,1);
        grid[1][C2] = new Item(Category.CAT,1);
        grid[2][C2] = new Item(Category.PLANT,1);
        grid[3][C2] = new Item(Category.PLANT,1);
        grid[4][C2] = new Item(Category.CAT,1);
        grid[5][C2] = new Item(Category.BOOK,1);

        // The column 4 isn't completely filled
        grid[0][C4] = new Item(Category.BOOK,1);
        grid[1][C4] = new Item(Category.BOOK,1);
        grid[2][C4] = new Item(Category.BOOK,1);
        grid[4][C4] = new Item(Category.BOOK,1);
        grid[5][C4] = new Item(Category.BOOK,1);

        assertFalse(card.check(grid),"The grid satisfies the CommonObjectiveCard n.5");
    }
}

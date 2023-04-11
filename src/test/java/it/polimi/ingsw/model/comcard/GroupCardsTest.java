package it.polimi.ingsw.model.comcard;

import it.polimi.ingsw.model.Category;
import it.polimi.ingsw.model.Item;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GroupCardsTest {
    private GroupCards card;
    private Item[][] grid0, grid1, grid2, grid3, grid4, grid5, grid6, grid7, grid8, grid9, grid10;
    private static final int ROWS = 6;
    private static final int COLS = 5;
    private static final int TYPE_GROUP_OF_TWO = 4;
    private static final int TYPE_GROUP_OF_FOUR = 3;
    private static final int TYPE_GROUP_OF_SQUARES = 1;
    private static final int TYPE_GROUP_OF_EIGHT = 9;
    private static final int TYPE_CORNERS = 8;


    @BeforeAll
    public void generalSetup() {
        grid0 = new Item[ROWS][COLS];
        grid1 = new Item[ROWS][COLS];
        grid2 = new Item[ROWS][COLS];
        grid3 = new Item[ROWS][COLS];
        grid4 = new Item[ROWS][COLS];
        grid5 = new Item[ROWS][COLS];
        grid6 = new Item[ROWS][COLS];
        grid7 = new Item[ROWS][COLS];
        grid8 = new Item[ROWS][COLS];
        grid9 = new Item[ROWS][COLS];
        grid10 = new Item[ROWS][COLS];
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                grid0[i][j] = new Item(null);
                grid1[i][j] = new Item(null);
                grid2[i][j] = new Item(null);
                grid3[i][j] = new Item(null);
                grid4[i][j] = new Item(null);
                grid5[i][j] = new Item(null);
                grid6[i][j] = new Item(null);
                grid7[i][j] = new Item(null);
                grid8[i][j] = new Item(null);
                grid9[i][j] = new Item(null);
                grid10[i][j] = new Item(null);
            }
        }
    }

    @BeforeEach
    public void setUp() {
        card = null;
    }

    /** verifies that the method returns true when the commonObjCard n.4 is met*/
    @Test
    public void checkSixGroupOfTwo() {
        /* The grid contains six groups of two items of the same type. check() to return true. */
        card = new GroupCards(TYPE_GROUP_OF_TWO);
        grid0[0][0] = new Item(Category.FRAME);
        grid0[0][1] = new Item(Category.FRAME);
        grid0[0][4] = new Item(Category.BOOK);
        grid0[1][4] = new Item(Category.BOOK);
        grid0[1][0] = new Item(Category.CAT);
        grid0[2][0] = new Item(Category.CAT);
        grid0[2][2] = new Item(Category.CAT);
        grid0[3][2] = new Item(Category.CAT);
        grid0[4][2] = new Item(Category.TROPHY);
        grid0[4][3] = new Item(Category.TROPHY);
        grid0[5][1] = new Item(Category.PLANT);
        grid0[5][2] = new Item(Category.PLANT);
        grid0[5][3] = new Item(Category.CAT);
        grid0[0][3] = new Item(Category.GAME);


        assertTrue(card.check(grid0), "The grid doesn't contain six groups of two items of the same type!");
    }

    /** verifies that the method returns false when the conditions of the commonObjCard n.4 aren't met*/
    @Test
    public void checkNotSixGroupsOfTwo() {
        /* The grid contains less than six groups of two items of the same type. check() to return false. */
        card = new GroupCards(TYPE_GROUP_OF_TWO);
        grid1[0][0] = new Item(Category.FRAME);
        grid1[0][1] = new Item(Category.FRAME);
        grid1[0][4] = new Item(Category.BOOK);
        grid1[1][4] = new Item(Category.BOOK);
        grid1[1][0] = new Item(Category.CAT);
        grid1[2][0] = new Item(Category.PLANT);
        grid1[2][2] = new Item(Category.CAT);
        grid1[3][2] = new Item(Category.CAT);
        grid1[4][2] = new Item(Category.BOOK);
        grid1[4][3] = new Item(Category.TROPHY);
        grid1[5][1] = new Item(Category.PLANT);
        grid1[5][2] = new Item(Category.PLANT);
        grid1[5][3] = new Item(Category.CAT);
        grid1[0][3] = new Item(Category.GAME);

        assertFalse(card.check(grid1), "The grid contains six groups of two items of the same type!");

        /* The grid contains more than six groups of two items of the same type. check() to return false. */
        grid1[0][0] = new Item(Category.FRAME);
        grid1[0][1] = new Item(Category.FRAME);
        grid1[0][4] = new Item(Category.BOOK);
        grid1[1][4] = new Item(Category.BOOK);
        grid1[1][0] = new Item(Category.CAT);
        grid1[2][0] = new Item(Category.CAT);
        grid1[2][2] = new Item(Category.CAT);
        grid1[3][2] = new Item(Category.CAT);
        grid1[4][2] = new Item(Category.TROPHY);
        grid1[4][3] = new Item(Category.TROPHY);
        grid1[5][1] = new Item(Category.PLANT);
        grid1[5][2] = new Item(Category.PLANT);
        grid1[5][3] = new Item(Category.CAT);
        grid1[5][4] = new Item(Category.CAT);
        grid1[0][3] = new Item(Category.GAME);

        assertFalse(card.check(grid1), "The grid contains six groups of two items of the same type!");

    }

    /** verifies that the method returns false when the grid is empty */
    @Test
    public void checkNullGroupOfTwo() {
        card = new GroupCards(TYPE_GROUP_OF_TWO);
        assertFalse(card.check(grid2));
    }

    /** verifies that the method returns true when the commonObjCard n.8 is met*/
    @Test
    public void checkGroupOfSquares() {
        /* The grid contains two different groups of squares of the same type. check() to return true*/
        card = new GroupCards(TYPE_GROUP_OF_SQUARES);
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                grid3[i][j] = new Item(Category.CAT);
                grid3[i+1][j+2] = new Item(Category.CAT);
            }
        }
        assertTrue(card.check(grid3), "The grid doesn't contain two squares of items of the same type!");
    }

    /** verifies that the method returns false when the conditions of the commonObjCard n.8 aren't met*/
    @Test
    public void checkNotTwoSquares() {
        /* The grid contains less than two squares of four items of the same type. check() to return false. */
        card = new GroupCards(TYPE_GROUP_OF_SQUARES);
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                grid4[i][j] = new Item(Category.CAT);
            }
        }
        grid4[4][2] = new Item(Category.PLANT);
        grid4[5][0] = new Item(Category.GAME);
        assertFalse(card.check(grid4), "The grid contains two squares of four items of the same type, not less then two");

        /* The grid contains more than two squares of four items of the same type. check() to return false*/
        card = new GroupCards(TYPE_GROUP_OF_SQUARES);
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                grid4[i][j] = new Item(Category.CAT);
                grid4[i][j + 2] = new Item(Category.CAT);
                grid4[i + 2][j] = new Item(Category.CAT);
            }
        }
        assertFalse(card.check(grid4), "The grid contains two squares of four items of the same type, not more than two");
    }

    /** verifies that the method returns false when the conditions of the commonObjCard n.8 aren't met*/
    @Test
    public void checkDifferentSquares() {
        /* The grid has two squares of four items of the same type, but the two squares has different types.
         check() to return false. */
        card = new GroupCards(TYPE_GROUP_OF_SQUARES);
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                grid5[i][j] = new Item(Category.GAME);
                grid5[i][j + 2] = new Item(Category.CAT);
            }
        }
        assertFalse(card.check(grid5), "The grid contains two squares of four items of the same type");
    }

    /** verifies that the method returns true when the commonObjCard n.3 is met*/
    @Test
    public void checkGroupOfFour() {
        /* The grid contains four groups of four items of the same type. check() to return true. */
        card = new GroupCards(TYPE_GROUP_OF_FOUR);
        for (int j = 0; j < 4; j++) {
            grid6[0][j] = new Item(Category.CAT);
            grid6[1][j] = new Item(Category.GAME);
            grid6[2][j] = new Item(Category.PLANT);
            grid6[3][j] = new Item(Category.CAT);
        }
        assertTrue(card.check(grid6), "The grid doesn't contain four groups four item of the same type");
    }

    /** verifies that the method returns false when the conditions of the commonObjCard n.3 aren't met*/
    @Test
    public void checkNotGroupOFour() {
        /* The grid contains less than four groups of four items of the same time. check() to return false.*/
        card = new GroupCards(TYPE_GROUP_OF_FOUR);
        for (int j = 0; j < 4; j++) {
            grid7[0][j] = new Item(Category.CAT);
            grid7[1][j] = new Item(Category.GAME);
            grid7[2][j] = new Item(Category.PLANT);
        }
        assertFalse(card.check(grid7), "The grid contains four groups four item of the same type, not less than four");


    /* The grid contains less than four groups of four items of the same time. check() to return false.*/
        card = new GroupCards(TYPE_GROUP_OF_FOUR);
        for(int j = 0;j<4;j++){
        grid7[0][j] = new Item(Category.CAT);
        grid7[1][j] = new Item(Category.GAME);
        grid7[2][j] = new Item(Category.PLANT);
        grid7[3][j] = new Item(Category.CAT);
        grid7[4][j] = new Item(Category.BOOK);
        }
        assertFalse(card.check(grid7), "The grid contains four groups of four items of the same type, not more than four");
    }

    /** verifies that the method returns false when the grid is empty */
    @Test
    public void checkNullGroupOfFour(){
        card = new GroupCards(TYPE_GROUP_OF_FOUR);
        assertFalse(card.check(grid8));
    }

    /** verifies that the method returns true when the commonObjCard n.9 is met*/
    @Test
    public void CheckGroupOfEight(){
        /* The grid contains eight items of the same type. check() to return true */
        card = new GroupCards(TYPE_GROUP_OF_EIGHT);
        grid9[0][0]= new Item(Category.BOOK);
        grid9[0][4]= new Item(Category.BOOK);
        grid9[1][0]= new Item(Category.BOOK);
        grid9[2][4]= new Item(Category.BOOK);
        grid9[2][1]= new Item(Category.BOOK);
        grid9[3][3]= new Item(Category.BOOK);
        grid9[4][4]= new Item(Category.BOOK);
        grid9[5][0]= new Item(Category.BOOK);

        assertTrue(card.check(grid9), "The grid doesn't contain eight items of the same type");
    }

    /** verifies that the method returns false when the conditions of the commonObjCard n.9 aren't met*/
    @Test
    public void checkLessThanGroupOfEight() {
        /* The grid contains less than eight items of the same type. check() to return false */
        card = new GroupCards(TYPE_GROUP_OF_EIGHT);
        grid9[0][0] = new Item(Category.BOOK);
        grid9[0][4] = new Item(Category.BOOK);
        grid9[1][0] = new Item(Category.BOOK);
        grid9[2][4] = new Item(Category.BOOK);
        grid9[2][1] = new Item(Category.BOOK);
        grid9[3][3] = new Item(Category.BOOK);
        grid9[4][4] = new Item(Category.BOOK);


        assertFalse(card.check(grid9), "The grid contains eight items of the same type, not less than eight");
    }

    @BeforeEach
    public void setUpGrid9() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                grid9[i][j] = new Item(null);
            }
        }
    }

    /** verifies that the method returns false when the conditions of the commonObjCard n.9 aren't met*/
    @Test
    public void checkMoreThanGroupOfEight(){
         /*The grid contains more than eight items of the same type. check() to return false */
       card = new GroupCards(TYPE_GROUP_OF_EIGHT);
        grid9[0][0]= new Item(Category.BOOK);
        grid9[0][4]= new Item(Category.BOOK);
        grid9[1][0]= new Item(Category.BOOK);
        grid9[2][4]= new Item(Category.BOOK);
        grid9[2][1]= new Item(Category.BOOK);
        grid9[3][3]= new Item(Category.BOOK);
        grid9[4][4]= new Item(Category.BOOK);
        grid9[5][0]= new Item(Category.BOOK);
        grid9[5][3]= new Item(Category.BOOK);

        assertTrue(card.check(grid9), "The grid contains eight items of the same type, not more than eight");
    }

    /** verifies that the method returns false when the grid is empty */
    @Test
    public void checkNullGroupOfEight(){
        card = new GroupCards(TYPE_GROUP_OF_EIGHT);
        assertFalse(card.check(grid10));
    }

    /** the method checks if the type is allowed in the card*/
    @Test
    public void NotAllowedType(){
        /* The implementation can't support this.type. check() to return false. */
        card = new GroupCards(TYPE_CORNERS);
        assertFalse(card.check(grid1), "This type is not allowed in this card");

    }
}













package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertSame;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ShelfTest {

    private Shelf shelf;

    /**
     * Setup method for all tests
     */
    @BeforeEach
    public void SetUp() {
        shelf = null;
    }

    /** method to test if the Shelf is full */
    @Test
    public void isRealFull() {
        final int ROWS = 6;
        final int COLUMS = 5;
        shelf = new Shelf();
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMS; j++) {
                shelf.getShelfGrid()[i][j] = new Item(Category.BOOK,1);
            }
        }
        assertSame(true, shelf.isFull(), "The shelf is empty");
    }

    /** method to test if the shelf is empty */
    @Test
    public void isNotAllFull() {
        final int COLUMS = 5;
        shelf = new Shelf();
        for (int i = 0; i < COLUMS; i++) {
            shelf.getShelfGrid()[0][i] = new Item(Category.BOOK,1);

        }
        shelf.getShelfGrid()[0][3] = new Item(null,0);

        assertSame(false, shelf.isFull(), "The shelf is full");
    }

   /** method to test if all items are null */
    @Test
    public void isEmpty() {
        final int ROWS = 6;
        final int COLUMS = 5;
        shelf = new Shelf();
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMS; j++) {
                shelf.getShelfGrid()[i][j] = new Item(null,0);
            }
        }
        assertSame(false, shelf.isFull(), "The shelf is not empty");
    }
}


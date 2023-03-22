package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ShelfTest {

    private Shelf shelf;

    @BeforeEach
    public void SetUp() {
        shelf = null;
    }

    @Test
    public void isRealFull() {
        final int ROWS = 6;
        final int COLUMS = 5;
        shelf = new Shelf();
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMS; j++) {
                shelf.getShelfGrid()[i][j] = new Item(Category.BOOK);
            }
        }
        assertSame(true, shelf.isFull(), "The shelf is empty");
    }

    @Test
    public void isNotAllFull() {
        final int ROWS = 6;
        final int COLUMS = 5;
        shelf = new Shelf();
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMS; j++) {
                shelf.getShelfGrid()[i][j] = new Item(Category.BOOK);
            }
        }
        shelf.getShelfGrid()[3][4] = new Item(null);

        assertSame(false, shelf.isFull(), "The shelf is full");
    }

    @Test
    public void isEmpty() {
        final int ROWS = 6;
        final int COLUMS = 5;
        shelf = new Shelf();
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMS; j++) {
                shelf.getShelfGrid()[i][j] = new Item(null);
            }
        }
        assertSame(false, shelf.isFull(), "The shelf is not empty");
    }
}


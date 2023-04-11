package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ItemTest {

    private Item item;

    @BeforeEach
    public void cleanItem(){
        item = null;
    }

   /** method to test if the item is null */
    @Test
    public void itemCreationOfNullCategory(){
        item = new Item(null);
        assertSame(null, item.getCategoryType(), "The item is not null");
    }

    /** method to test if the Item category exists */
    @Test
    public void existingItemCategory(){
        item = new Item(Category.CAT);
        assertSame(Category.CAT, item.getCategoryType(), "The item is not cat");
    }

   /** method to test if two items are the same */
   @Test
    public void categoryIsNotSame(){
        item = new Item(Category.CAT);
        assertNotSame(Category.FRAME, item.getCategoryType(), "The item is the same");
    }

    /** method to test that an item category isn't null*/
    @Test
    public void getCategoryTestNull(){
        item = new Item(null);
        Category categoryGotten = item.getCategoryType();
        assertSame(null, categoryGotten, "The category is not same");
    }

    @Test
    public void getCategoryTestNotNull(){
        item = new Item(Category.BOOK);
        Category categoryGotten = item.getCategoryType();
        assertSame(Category.BOOK, categoryGotten, "The category is not same");
    }
}

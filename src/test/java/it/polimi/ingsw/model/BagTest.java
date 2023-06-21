package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.NoElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class BagTest tests Bag class
 * @see Bag
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BagTest {

    private Bag bag;
    private Game game;
    private List<Item> itemList;
    private Item item;


    /**
     * Setup method for all tests
     */
    @BeforeEach
    public void cleanBag() {
        bag = null;
        game = null;
        try{
            game = new Game(4);
        }
        catch(Exception e) {
            fail("Game not created successfully");
        }
        bag = game.getBag();
    }


    /**
     * Method to test if an Item is successfully drawn from the bag
     * @throws Exception
     */
    @Test
    public void drawItemTest() throws NoElementException {
        try {
            List<Category> categoryList = Arrays.asList(Category.values());
            Item itemDraw = bag.drawItem();
            assertNotEquals(null,itemDraw,"The item you drew is missing");
            assertTrue(categoryList.contains(itemDraw.getCategoryType()),"The item you drew doesn't have a category");
        }
        catch (NoElementException e){
            fail("You cannot draw from the bag");
        }
    }

    /**
     * Method to test if the exception triggers when the bag is empty
     */
    @Test
    public void emptyBagTest(){
        for(int i=bag.getItemCards().size()-1; i>=0 ; i--){
            bag.getItemCards().remove(i);
        }
        assertTrue(0 == bag.getItemCards().size(), "The size is not 0");
        try {
            Item itemDraw = bag.drawItem();
            fail("The bag is not empty");
        }
        catch (NoElementException e){
        }
    }

    /**
     * Method to test if the List of Items is successfully returned
     */
    @Test
    public void getItemCardsTest(){
        itemList = bag.getItemCards();
        assertEquals(bag.getItemCards(), itemList, "The bag returned is not the same");
    }

    /**
     * Method to test if an Item is successfully added to the bag
     */
    @Test
    public void setItemCardsTest(){
        item = new Item(Category.CAT,1);
        bag.setItemCards(item);
        assertSame(Category.CAT,bag.getItemCards().get(bag.getItemCards().size()-1).getCategoryType(),
                  "The item added is not a cat");
    }

    /**
     * Method to draw Items until the bag gets empty
     */
    @Test
    public void bagEmptierTest() {
        final int DIM = 87;
        try {
            game = new Game(3);  // Game created has 3 players
            for (int i = DIM - 1; i >= 0; i--) bag.drawItem();
            assertTrue(0 == bag.getItemCards().size(), "The bag is not empty");
        } catch (Exception e) {
            fail("Game not created successfully");
        }
    }

    /**
     * Tests if the NoElement exception is triggered successfully
     */
    @Test
    public void EmptyDrawBagTest(){
        Bag bag = new Bag();
        Item item = new Item(null,0);
        assertThrows(NoElementException.class,()->{
            bag.drawItem();}, "NoElementException hasn't been triggered");
        try{
            assertSame(bag.drawItem(),item,"NoElementException hasn't been triggered");
        } catch (NoElementException ignored){
        }
    }

}

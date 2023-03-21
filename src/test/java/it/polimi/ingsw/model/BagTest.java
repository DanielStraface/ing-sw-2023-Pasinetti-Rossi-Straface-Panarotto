package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BagTest {

    private Bag bag;
    private Game game;
    private List<Item> itemList;
    private Item item;


    @BeforeEach
    public void cleanBag() {
        bag = null;
        game = null;
        try{
            game = new Game(4);
        }
        catch(Exception e) {
            fail();
        }
        bag = game.getBag();
    }

    /** Method to test if the bag is created successfully */
    @Test
    public void BagCreation(){
        //game.createBag();
        //bag = game.getBag();
        for(Item item: bag.getItemCards()){
            assertNotEquals(null,item,"An item is missing");
        }
    }

    /** Method to test if an Item is successfully drawn from the bag */
    @Test
    public void drawItemTest() throws Exception {
        //game.createBag();
        //bag = game.getBag();
        try {
            List<Category> categoryList = Arrays.asList(Category.values());
            Item itemDraw = bag.drawItem();
            assertNotEquals(null,itemDraw,"The item you drew is missing");
            assertTrue(categoryList.contains(itemDraw.getCategoryType()),"The item you drew doesn't have a category");
        }
        catch (Exception e){
            fail("You cannot draw from the bag");
        }
    }

    /** Method to test if the exception triggers when the bag is empty */
    @Test
    public void emptyBagTest(){
        //game.createBag();
        //bag = game.getBag();
        for(int i=bag.getItemCards().size()-1; i>=0 ; i--){
            bag.getItemCards().remove(i);
        }
        assertTrue(0 == bag.getItemCards().size(), "The size is not 0");
        try {
            Item itemDraw = bag.drawItem();
            fail("The bag is not empty");
        }
        catch (Exception e){
        }
    }

    /** Method to test if the List of Items is successfully returned */
    @Test
    public void getItemCardsTest(){
        //game.createBag();
        //bag = game.getBag();
        itemList = bag.getItemCards();
        assertEquals(bag.getItemCards(), itemList, "The bag is not the same");
    }

    /** Method to test if an Item is successfully added to the bag */
    @Test
    public void setItemCardsTest(){
        //game.createBag();
        //bag = game.getBag();
        item = new Item(Category.CAT);
        bag.setItemCards(item);
        assertSame(Category.CAT,bag.getItemCards().get(bag.getItemCards().size()-1).getCategoryType(),
                "The item added is not a cat");
    }
}

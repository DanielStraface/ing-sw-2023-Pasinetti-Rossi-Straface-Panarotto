package it.polimi.ingsw.model.comcard;

import it.polimi.ingsw.model.Category;
import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Shelf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CommonObjCardTest {
    private CommonObjCard card;
    private Player player;
    private final int NUMBER_OF_PLAYERS = 3;
    private final int BASE_TYPE = 3;

    @BeforeEach
    public void setupPlayer() throws Exception {
        player = new Player("TEST NAME", 0);
        Shelf shelf = new Shelf();
        shelf.getShelfGrid()[0][0] = new Item(Category.BOOK);
        shelf.getShelfGrid()[0][4] = new Item(Category.BOOK);
        shelf.getShelfGrid()[5][0] = new Item(Category.BOOK);
        shelf.getShelfGrid()[5][4] = new Item(Category.BOOK);
        player.setMyShelf(shelf);
    }

    @BeforeEach
    public void setup(){
        card = null;
        try{
            card = new CommonObjCard(NUMBER_OF_PLAYERS, BASE_TYPE);
        } catch (Exception e){
            fail("CommonObjCard not create correctly");
        }
    }

    @Test
    public void gameOfThreePlayersTest(){
        assertEquals(BASE_TYPE, card.getType(), "The type of the card is wrong");
        for(int i=0; i<NUMBER_OF_PLAYERS;i++){
            assertTrue(0 < card.getPoints(), "The points of the card is wrong");
        }
        ArrayIndexOutOfBoundsException exception = assertThrows(ArrayIndexOutOfBoundsException.class, () ->{
            card.getPoints();
        });
        assertEquals("Index -1 out of bounds for length 4", exception.getMessage());
    }

    @Test
    public void gameOfTwoTest(){
        try{
            card = new CommonObjCard(2, BASE_TYPE);
        } catch (Exception e){
            fail("CommonObjCard not create correctly");
        }
        for(int i=0;i<2;i++){
            assertTrue(0 < card.getPoints(), "The points of the card is wrong");
        }
        ArrayIndexOutOfBoundsException exception = assertThrows(ArrayIndexOutOfBoundsException.class, () ->{
            card.getPoints();
        });
        assertEquals("Index -1 out of bounds for length 4", exception.getMessage());
    }

    @Test
    public void gameOfFour(){
        try{
            card = new CommonObjCard(4, BASE_TYPE);
        } catch (Exception e){
            fail("CommonObjCard not create correctly");
        }
        for(int i=0;i<4;i++){
            assertTrue(0 < card.getPoints(), "The points of the card is wrong");
        }
        ArrayIndexOutOfBoundsException exception = assertThrows(ArrayIndexOutOfBoundsException.class, () ->{
            card.getPoints();
        });
        assertEquals("Index -1 out of bounds for length 4", exception.getMessage());
    }

    @Test
    public void invalidNumberOfPlayers(){
        Exception exception = assertThrows(Exception.class, () -> {
            card = new CommonObjCard(5, BASE_TYPE);
        });
        assertEquals("Error: number of players not allowed!", exception.getMessage());
    }

    @Test
    public void getPointsTest(){
        for(int i=2; i>=0;i--){
            assertEquals(4 + 2*i, card.getPoints(), "The points returned are wrong");
        }
    }

    @Test
    public void wrongCheckTest(){
        Shelf shelf = new Shelf();
        shelf.getShelfGrid()[0][0] = new Item(Category.BOOK);
        shelf.getShelfGrid()[0][4] = new Item(Category.BOOK);
        shelf.getShelfGrid()[5][0] = new Item(Category.BOOK);
        shelf.getShelfGrid()[5][4] = new Item(Category.CAT);
        player.setMyShelf(shelf);
        card.doCheck(player);
        assertEquals(0, player.getScore(), "The check method hints");
    }

    @Test
    public void rightCheckTest(){
        card.doCheck(player);
        assertTrue(0 < player.getScore(), "The check method is wrong");
    }
}

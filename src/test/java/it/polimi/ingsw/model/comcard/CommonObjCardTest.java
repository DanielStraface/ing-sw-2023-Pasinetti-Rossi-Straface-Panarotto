package it.polimi.ingsw.model.comcard;

import it.polimi.ingsw.exceptions.InvalidNumberOfPlayersException;
import it.polimi.ingsw.exceptions.InvalidPointerException;
import it.polimi.ingsw.exceptions.OutOfBoundsException;
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
        player = new Player();
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
    public void gameOfThreePlayersTest() throws InvalidPointerException {
        assertEquals(BASE_TYPE, card.getType(), "The type of the card is wrong");
        for(int i=0; i<NUMBER_OF_PLAYERS;i++){
            assertTrue(0 < card.getPoints(), "The points of the card is wrong");
        }
        OutOfBoundsException exception = assertThrows(OutOfBoundsException.class, () ->{
            card.getPoints();
        });
        assertEquals("Index -1 out of bounds for length 4", exception.getMessage());
    }

    @Test
    public void gameOfTwoTest() throws InvalidPointerException {
        try{
            card = new CommonObjCard(2, BASE_TYPE);
        } catch (Exception e){
            fail("CommonObjCard not create correctly");
        }
        for(int i=0;i<2;i++){
            assertTrue(0 < card.getPoints(), "The points of the card is wrong");
        }
        OutOfBoundsException exception = assertThrows(OutOfBoundsException.class, () ->{
            card.getPoints();
        });
        assertEquals("Index -1 out of bounds for length 4", exception.getMessage());
    }

    @Test
    public void gameOfFour() throws InvalidPointerException {
        try{
            card = new CommonObjCard(4, BASE_TYPE);
        } catch (Exception e){
            fail("CommonObjCard not create correctly");
        }
        for(int i=0;i<4;i++){
            assertTrue(0 < card.getPoints(), "The points of the card is wrong");
        }
        OutOfBoundsException exception = assertThrows(OutOfBoundsException.class, () ->{
            card.getPoints();
        });
        System.err.println(exception.getMessage());
        assertEquals(" ", exception);
    }

    @Test
    public void invalidNumberOfPlayers(){
        InvalidNumberOfPlayersException exception = assertThrows(InvalidNumberOfPlayersException.class, () -> {
            card = new CommonObjCard(5, BASE_TYPE);
        });
        assertEquals("Error: the number of players is not allowed!", exception.getMessage());
    }

    @Test
    public void getPointsTest() throws InvalidPointerException {
        for(int i=2; i>=0;i--){
            assertEquals(4 + 2*i, card.getPoints(), "The points returned are wrong");
        }
    }

    @Test
    public void wrongCheckTest() throws InvalidPointerException {
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
    public void rightCheckTest() throws InvalidPointerException {
        card.doCheck(player);
        assertTrue(0 < player.getScore(), "The check method is wrong");
    }
}

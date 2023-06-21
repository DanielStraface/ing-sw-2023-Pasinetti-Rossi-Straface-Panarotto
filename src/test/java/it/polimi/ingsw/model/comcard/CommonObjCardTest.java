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

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class CommonObjCardTest tests CommonObjCard class
 * @see CommonObjCard
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CommonObjCardTest {
    private CommonObjCard card;
    private Player player;
    private final int NUMBER_OF_PLAYERS = 3;
    private final int BASE_TYPE = 8;

    /**
     * Setup method for all classes
     */
    @BeforeEach
    public void setupPlayer(){
        player = new Player();
        Shelf shelf = new Shelf();
        shelf.getShelfGrid()[0][0] = new Item(Category.BOOK,1);
        shelf.getShelfGrid()[0][4] = new Item(Category.BOOK,1);
        shelf.getShelfGrid()[5][0] = new Item(Category.BOOK,1);
        shelf.getShelfGrid()[5][4] = new Item(Category.BOOK,1);
        player.setMyShelf(shelf);
    }

    /**
     * Setup method for all classes
     */
    @BeforeEach
    public void setup(){
        card = null;
        try{
            card = new CommonObjCard(NUMBER_OF_PLAYERS, BASE_TYPE);
        } catch (Exception e){
            fail("CommonObjCard not create correctly");
        }
    }

    /**
     * Tests if the points given are correct with 3 players in a match
     * @throws InvalidPointerException if the array length is zero
     */
    @Test
    public void gameOfThreePlayersTest() throws InvalidPointerException {
        assertEquals(BASE_TYPE, card.getType(), "The type of the card is wrong");
        try{
            for(int i=0; i<NUMBER_OF_PLAYERS;i++){
                assertTrue(0 < card.getPoints(), "The points of the card is wrong");
            }
        } catch (OutOfBoundsException e){
            System.err.println("Wrong test execution");
        }
        OutOfBoundsException exception = assertThrows(OutOfBoundsException.class, () ->{
            card.getPoints();
        });
        assertEquals("All the points for this card are taken", exception.getMessage());
    }

    /**
     * Tests if the points given are correct with 2 players in a match
     * @throws InvalidPointerException if the array length is zero
     */
    @Test
    public void gameOfTwoTest() throws InvalidPointerException {
        try{
            card = new CommonObjCard(2, BASE_TYPE);
        } catch (Exception e){
            fail("CommonObjCard not create correctly");
        }
        try{
            for(int i=0;i<2;i++){
                assertTrue(0 < card.getPoints(), "The points of the card is wrong");
            }
        } catch (OutOfBoundsException e){
            System.err.println("Wrong test execution");
        }
        OutOfBoundsException exception = assertThrows(OutOfBoundsException.class, () ->{
            card.getPoints();
        });
        assertEquals("All the points for this card are taken", exception.getMessage());
    }

    /**
     * Tests if the points given are correct with 4 players in a match
     * @throws InvalidPointerException if the array length is zero
     */
    @Test
    public void gameOfFour() throws InvalidPointerException {
        try{
            card = new CommonObjCard(4, BASE_TYPE);
        } catch (Exception e){
            fail("CommonObjCard not create correctly");
        }
        try{
            for(int i=0;i<4;i++){
                assertTrue(0 < card.getPoints(), "The points of the card is wrong");
            }
        } catch (OutOfBoundsException e){
            System.err.println("Wrong test execution");
        }
        OutOfBoundsException exception = assertThrows(OutOfBoundsException.class, () ->{
            card.getPoints();
        });
        System.err.println(exception.getMessage());
        assertEquals("All the points for this card are taken", exception.getMessage());
    }

    /**
     * Tests if the InvalidNumberOfPlayers exception is correctly thrown
     */
    @Test
    public void invalidNumberOfPlayers(){
        InvalidNumberOfPlayersException exception = assertThrows(InvalidNumberOfPlayersException.class, () -> {
            card = new CommonObjCard(5, BASE_TYPE);
        });
        assertEquals("Error: the number of players is not allowed!", exception.getMessage());
    }

    /**
     * getPoints get method test
     * @throws InvalidPointerException if the array length is zero
     */
    @Test
    public void getPointsTest() throws InvalidPointerException {
        try{
            for(int i=2; i>=0;i--){
                assertEquals(4 + 2*i, card.getPoints(), "The points returned are wrong");
            }
        } catch (OutOfBoundsException e){
            System.err.println("Wrong test execution");
        }
    }

    /**
     * Tests if no points are given under the correct circumstances
     * @throws InvalidPointerException if the array length is zero
     */
    @Test
    public void wrongCheckTest() throws InvalidPointerException {
        Shelf shelf = new Shelf();
        shelf.getShelfGrid()[0][0] = new Item(Category.BOOK,1);
        shelf.getShelfGrid()[0][4] = new Item(Category.BOOK,1);
        shelf.getShelfGrid()[5][0] = new Item(Category.BOOK,1);
        shelf.getShelfGrid()[5][4] = new Item(Category.CAT,1);
        player.setMyShelf(shelf);
        card.doCheck(player);
        assertEquals(0, player.getScore(), "The check method hints");
    }

    /**
     * Tests if no points are given under the correct circumstances
     * @throws InvalidPointerException if the array length is zero
     */
    @Test
    public void rightCheckTest() throws InvalidPointerException {
        card.doCheck(player);
        assertTrue(0 <= player.getScore(), "The check method is wrong");
    }

    /**
     * getDescription get method test
     */
    @Test
    public void getDescriptionTest(){
        final int SEVENTH_TYPE = 7 ;
        final String descriptionToCompare = "Four tiles of the same type in the four corners of the bookshelf.";
        CommonObjCardReader reader = new CommonObjCardReader();
        List<String> descriptions = new LinkedList<>(reader.readFromFile());
        try{
            card = new CommonObjCard(NUMBER_OF_PLAYERS, SEVENTH_TYPE, descriptions.remove(SEVENTH_TYPE));
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        assertEquals(descriptionToCompare, card.getDescription());
    }

    /**
     * Tests if the NullPointerException is correctly thrown when reading a commonObjCard from json
     */
    @Test
    public void exceptionCommonObjCardReader(){
        final String file = "kek.json";
        CommonObjCardReader reader = new CommonObjCardReader(file);
        NullPointerException exception = assertThrows(NullPointerException.class, () -> reader.readFromFile());
        assertEquals("in == null", exception.getMessage());
    }


    /**
     * Tests if the array is correctly copied
     * @throws InvalidNumberOfPlayersException when the number of players given is invalid
     * @throws InvalidPointerException if the array length is zero
     * @throws OutOfBoundsException when the array index is out of bounds
     */
    @Test
    public void CopyPointsArrayTest() throws InvalidNumberOfPlayersException, InvalidPointerException, OutOfBoundsException {
        int [] arrayToCopy;
        CommonObjCard commonObjCard = new CommonObjCard(4,1);
        arrayToCopy = commonObjCard.copyPointsArray();
        assertEquals(commonObjCard.getPoints(),arrayToCopy[3],"The array copied isn't the same");
    }


    /**
     * getNextPoints get method test
     * @throws InvalidNumberOfPlayersException
     */
    @Test
    public void getNextPointsTest() throws InvalidNumberOfPlayersException {
        CommonObjCard commonObjCard = new CommonObjCard(4,1);
        int nextPoints =  commonObjCard.getNextPoints();
        assertSame(commonObjCard.getNextPoints(),nextPoints,"The NextPoints int given isn't the same");
    }
}


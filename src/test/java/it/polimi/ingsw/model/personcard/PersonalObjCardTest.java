package it.polimi.ingsw.model.personcard;

import it.polimi.ingsw.exceptions.InvalidMatchesException;
import it.polimi.ingsw.exceptions.InvalidNumberOfPlayersException;
import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class PersonalObjCardTest tests PersonalObjCard class
 * @see PersonalObjCard
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PersonalObjCardTest {
    private PersonalObjCard card;
    private  PersonalCardReader reader;
    private Shelf shelf;
    private final int ROWS = 6;
    private final int COLS = 5;

    /**
     * Setup method for all tests
     */
    @BeforeAll
    public void setup(){
        shelf = new Shelf();
        for(int i=0;i<ROWS;i++){
            for(int j=0;j<COLS;j++){
                shelf.getShelfGrid()[i][j] = new Item(null,0);
            }
        }
        shelf.getShelfGrid()[0][0] = new Item(Category.PLANT,1);
        shelf.getShelfGrid()[0][2] = new Item(Category.FRAME,1);
        shelf.getShelfGrid()[1][4] = new Item(Category.CAT,1);
        shelf.getShelfGrid()[2][3] = new Item(Category.BOOK,1);
        shelf.getShelfGrid()[3][1] = new Item(Category.GAME,1);
        shelf.getShelfGrid()[5][2] = new Item(Category.TROPHY,1);
        reader = new PersonalCardReader();
    }

    /**
     * Setup method for all tests
     */
    @BeforeEach
    public void setupForEachTest(){
        card = null;
        card = new PersonalObjCard();
    }

    /**
     * Tests if the generated cards are null at the start of a match
     */
    @Test
    public void stringAndCardGridItemsNullAtStart(){
        assertNull(card.getPersonalObjCardDescription(), "The card is not null");
        for(int i=0;i<ROWS;i++){
            for(int j=0;j<COLS;j++){
                assertNull(card.getCardGrid()[i][j], "An item in the grid is not null");
            }
        }
    }

    /**
     * Tests if all the cards are correctly written in the json file
     * @throws InvalidNumberOfPlayersException when the number of players given is invalid
     * @throws RemoteException if the execution of a remote method call goes wrong
     */
    @Test
    public void existingItemCard() throws InvalidNumberOfPlayersException, RemoteException {
        final int NUM_OF_PLAYERS = 3;
        final int PLAYER_SELECTED = 0;
        int counter = 0;
        List<Category> categoryList = new LinkedList<>(Arrays.asList(Category.values()));
        Game game = new Game(NUM_OF_PLAYERS);
        card = game.getPlayers().get(PLAYER_SELECTED).getMyPersonalOBjCard();
        assertNotNull(card, "The player card is null");
        for(int i=0;i<ROWS;i++){
            for(int j=0;j<COLS;j++){
                if(card.getCardGrid()[i][j].getCategoryType() == null){
                    counter++;
                } else {
                    assertTrue(categoryList.contains(card.getCardGrid()[i][j].getCategoryType()),
                            "The item category type does not exists ");
                    categoryList.remove(card.getCardGrid()[i][j].getCategoryType());
                }
            }
        }
        assertEquals(24, counter, "The null item category type is more than 24");
    }

    /**
     * Tests if points are correctly added to the player after 0 to 6 matches done
     * @throws InvalidMatchesException if the matches made are above 6
     */
    @Test
    public void goalReachedPositiveTest() throws InvalidMatchesException {
        final int FIRST_CARD = 1;
        int counter = 0;
        List<PersonalObjCard> cardsList = new LinkedList<>(reader.readFromFile());
        Player player = new Player();
        card = cardsList.remove(FIRST_CARD);
        Item[][] completedObj = card.getCardGrid();
        for(int i=0; i<6; i++){
            for(int j=0; j<5; j++){
                if(completedObj[i][j].getCategoryType() != null){
                    player.getMyShelf().getShelfGrid()[i][j]= new Item(completedObj[i][j].getCategoryType(),1);
                    player.addPoints(card.shelfCheck(player.getMyShelf()));
                    counter++;
                }
                else player.getMyShelf().getShelfGrid()[i][j]= null;
                switch (counter){
                    case 1 ->{
                        assertEquals(1, player.getScore(), "The goal reach method is failed, must be true");
                    }
                    case 2 ->{
                        assertEquals(3, player.getScore(), "The goal reach method is failed, must be true");
                    }
                    case 3 ->{
                        assertEquals(7, player.getScore(), "The goal reach method is failed, must be true");
                    }
                    case 4 ->{
                        assertEquals(13, player.getScore(), "The goal reach method is failed, must be true");
                    }
                    case 5 ->{
                        assertEquals(22, player.getScore(), "The goal reach method is failed, must be true");
                    }
                    case 6 ->{
                        assertEquals(34, player.getScore(), "The goal reach method is failed, must be true");
                    }
                    default ->{
                        assertEquals(0,player.getScore(),"The goal reach method if failed, must be true");
                    }
                }
            }
        }
    }

}

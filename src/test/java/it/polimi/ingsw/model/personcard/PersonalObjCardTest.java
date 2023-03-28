package it.polimi.ingsw.model.personcard;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PersonalObjCardTest {
    private PersonalObjCard card;
    private  PersonalCardReader reader;
    private Shelf shelf;
    private final int ROWS = 6;
    private final int COLS = 5;

    @BeforeAll
    public void setup(){
        shelf = new Shelf();
        for(int i=0;i<ROWS;i++){
            for(int j=0;j<COLS;j++){
                shelf.getShelfGrid()[i][j] = new Item(null);
            }
        }
        shelf.getShelfGrid()[0][0] = new Item(Category.PLANT);
        shelf.getShelfGrid()[0][2] = new Item(Category.FRAME);
        shelf.getShelfGrid()[1][4] = new Item(Category.CAT);
        shelf.getShelfGrid()[2][3] = new Item(Category.BOOK);
        shelf.getShelfGrid()[3][1] = new Item(Category.GAME);
        shelf.getShelfGrid()[5][2] = new Item(Category.TROPHY);
        reader = new PersonalCardReader();
    }
    @BeforeEach
    public void setupForEachTest(){
        card = null;
        card = new PersonalObjCard();
    }

    @Test
    public void stringAndCardGridItemsNullAtStart(){
        assertNull(card.getPersonalObjCardDescription(), "The card is not null");
        for(int i=0;i<ROWS;i++){
            for(int j=0;j<COLS;j++){
                assertNull(card.getCardGrid()[i][j], "An item in the grid is not null");
            }
        }
    }

    @Test
    public void existingItemCard(){
        final int NUM_OF_PLAYERS = 3;
        final int PLAYER_SELECTED = 0;
        int counter = 0;
        List<Category> categoryList = new LinkedList<>(Arrays.asList(Category.values()));
        try{
            Game game = new Game(NUM_OF_PLAYERS);
            card = game.getPlayers().get(PLAYER_SELECTED).getMyPersonalOBjCard();
            assertNotNull(card, "The player card is null");
        } catch (Exception e){
            fail("Game creation mismatch");
        }
        for(int i=0;i<ROWS;i++){
            for(int j=0;j<COLS;j++){
                if(card.getCardGrid()[i][j].getCategoryType() == null){
                    counter++;
                } else {
                    assertTrue(categoryList.contains(card.getCardGrid()[i][j].getCategoryType()),
                            "The item category type does not exists ");
                    try{
                        categoryList.remove(card.getCardGrid()[i][j].getCategoryType());
                    } catch (Exception e) {
                        fail("Pop an item category type that is not in Category");
                    }
                }
            }
        }
        assertEquals(24, counter, "The null item category type is more than 24");
    }

    @Test
    public void goalReachedPositiveTest() throws Exception {
        final int FIRST_CARD = 0;
        List<PersonalObjCard> cardsList = new LinkedList<>(reader.readFromFile());
        Player player = new Player();
        card = cardsList.remove(FIRST_CARD);
        player.addPoints(card.shelfCheck(shelf));
        assertEquals(12, player.getScore(), "The goal reach method is failed, must be true");
    }

    @Test
    public void goalReachedNegativeTest() throws Exception {
        final int FIRST_CARD = 0;
        shelf.getShelfGrid()[0][0] = new Item(Category.BOOK);
        List<PersonalObjCard> cardsList = new LinkedList<>(reader.readFromFile());
        Player player = new Player();
        card = cardsList.remove(FIRST_CARD);
        player.addPoints(card.shelfCheck(shelf));
        assertEquals(9, player.getScore(), "The goal reach method is failed, must be 9 pts");
    }
}

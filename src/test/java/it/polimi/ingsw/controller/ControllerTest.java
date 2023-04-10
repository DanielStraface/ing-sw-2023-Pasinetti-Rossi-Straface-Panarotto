package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.TextualUI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ControllerTest {
    private static final int PLAYERS_NUMBER = 3;
    private Game game;
    private TextualUI view;
    private TurnHandler turnHandler;
    private Controller controller;
    @BeforeEach
    public void setup(){
        try{
            this.game = new Game(PLAYERS_NUMBER);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        this.view = new TextualUI();
        this.turnHandler = new TurnHandler(this.game);
        this.controller = new Controller(this.game, this.view);
    }

    @Test
    public void chooseFirstPlayerTest(){
        this.controller.chooseFirstPlayer();
        Player player = this.game.getCurrentPlayer();
        boolean isTrue = false;
        assertTrue(this.game.getPlayers().contains(player), "The first player is not in this game");
    }
}
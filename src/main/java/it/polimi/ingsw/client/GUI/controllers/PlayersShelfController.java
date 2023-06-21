package it.polimi.ingsw.client.GUI.controllers;

import it.polimi.ingsw.client.GUI.GUI;
import it.polimi.ingsw.model.Category;
import it.polimi.ingsw.modelview.ShelfView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

/**
 * This class represents a controller for the players' shelf in a GUI application.
 * It implements the GUIController interface.
 * The players' shelf is a grid of image views that display items belonging to the players.
 * ach player has their own shelf represented by a pane in the GUI.
 * The class provides methods to initialize the shelf, update the shelf for other players.
 */
public class PlayersShelfController implements GUIController {
    private static final int SHELF_ROWS = 6;
    private static final int SHELF_COLS = 5;
    @FXML
    private List<List<List<ImageView>>> playersShelfList;
    @FXML
    private Pane paneShelf1;
    @FXML
    private Pane paneShelf2;
    @FXML
    private Pane paneShelf3;
    @FXML
    private List<Label> nicknameLabels;
    private final Map<String, Integer> playersShelfMap = new HashMap<>();
    private final Map<Category, String> fromNameToPathItemTiles = Map.ofEntries(
            entry(Category.CAT, "/graphics/item_tiles/Gatti1."),
            entry(Category.BOOK, "/graphics/item_tiles/Libri1."),
            entry(Category.FRAME, "/graphics/item_tiles/Cornici1."),
            entry(Category.GAME, "/graphics/item_tiles/Giochi1."),
            entry(Category.TROPHY, "/graphics/item_tiles/Trofei1."),
            entry(Category.PLANT, "/graphics/item_tiles/Piante1.")
    );
    private int numOfPlayers;
    private GUI gui;

    /**
     * set method
     * @param numOfPlayer the number of players
     */
    public void setNumOfPlayer(int numOfPlayer){
        this.numOfPlayers = numOfPlayer;
    }

    /**
     * initializes the players' shelf map, associating to each nickname (key) a counter (value)
     * @param nicknames array with players' nickname
     */
    public void initializePlayersShelfMap(String[] nicknames){
        int counter = 0;
        if(numOfPlayers == 2) paneShelf1.setOpacity(1);
        if(numOfPlayers == 3){
            paneShelf2.setOpacity(1);
            paneShelf3.setOpacity(1);
            counter = 1;
        }
        if(numOfPlayers == 4) {
            paneShelf1.setOpacity(1);
            paneShelf2.setOpacity(1);
            paneShelf3.setOpacity(1);
        }
        for(String nickname : nicknames){
            playersShelfMap.put(nickname, counter);
            nicknameLabels.get(counter).setText(nickname);
            nicknameLabels.get(counter).setOpacity(1);
            counter++;
        }
    }

    /**
     * plays other players' shelf
     * @param nickname player's nickname
     * @param shelfView ShelfView
     */
    public void updateOtherPlayersShelf(String nickname, ShelfView shelfView){
        List<List<ImageView>> shelf = playersShelfList.get(playersShelfMap.get(nickname));
        for(int i=0;i<SHELF_ROWS;i++){
            for(int j=0;j<SHELF_COLS;j++){
                Category type = shelfView.getShelfGrid()[i][j].getCategoryType();
                int variant = shelfView.getShelfGrid()[i][j].getVariant();
                if(type != null){
                    shelf.get(j).get(i).setImage(
                            new Image(fromNameToPathItemTiles.get(type) + variant + ".png")
                    );
                }
            }
        }
    }

    /**
     * method to set gui for the application
     * @param gui GUI
     */
    @Override
    public void setGUI(GUI gui) {
        this.gui = gui;
    }

    /**
     * plays a sound effect from the specified file path.
     * @param filePath String
     */
    @Override
    public void playSound(String filePath) {

    }
}

package it.polimi.ingsw.client.GUI.controllers;

import it.polimi.ingsw.client.GUI.GUI;
import it.polimi.ingsw.model.Category;
import it.polimi.ingsw.modelview.ShelfView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

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
    private Map<String, Integer> playersShelfMap = new HashMap<>();
    private Map<Category, String> fromNameToPathItemTiles = Map.ofEntries(
            entry(Category.CAT, "/graphics/item_tiles/Gatti1.1.png"),
            entry(Category.BOOK, "/graphics/item_tiles/Libri1.1.png"),
            entry(Category.FRAME, "/graphics/item_tiles/Cornici1.1.png"),
            entry(Category.GAME, "/graphics/item_tiles/Giochi1.1.png"),
            entry(Category.TROPHY, "/graphics/item_tiles/Trofei1.1.png"),
            entry(Category.PLANT, "/graphics/item_tiles/Piante1.1.png")
    );
    private int numOfPlayers;
    private GUI gui;

    public void setNumOfPlayer(int numOfPlayer){
        this.numOfPlayers = numOfPlayer;
    }

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

    public void updateOtherPlayersShelf(String nickname, ShelfView shelfView){
        List<List<ImageView>> shelf = playersShelfList.get(playersShelfMap.get(nickname));
        for(int i=0;i<SHELF_ROWS;i++){
            for(int j=0;j<SHELF_COLS;j++){
                Category type = shelfView.getShelfGrid()[i][j].getCategoryType();
                if(type != null){
                    shelf.get(j).get(i).setImage(
                            new Image(fromNameToPathItemTiles.get(type))
                    );
                }
            }
        }
    }

    @Override
    public void setGUI(GUI gui) {
        this.gui = gui;
    }

    @Override
    public void playSound(String filePath) {

    }
}

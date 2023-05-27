package it.polimi.ingsw.client.GUI.controllers;

import it.polimi.ingsw.client.CLI.commands.Command;
import it.polimi.ingsw.client.CLI.commands.SelectColumnCommand;
import it.polimi.ingsw.client.CLI.commands.SelectItemsCommand;
import it.polimi.ingsw.client.GUI.GUI;
import it.polimi.ingsw.exceptions.FullColumnException;
import it.polimi.ingsw.exceptions.InvalidSelectionException;
import it.polimi.ingsw.model.Category;
import it.polimi.ingsw.modelview.GameBoardView;
import it.polimi.ingsw.modelview.ShelfView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.*;

public class MainGameController implements GUIController{
    @FXML
    private AnchorPane gameboardPane;
    @FXML
    private Button objectivesButton;
    @FXML
    private Label messageBox;
    @FXML
    private Label scoreLabel;
    @FXML
    private Label player1Label;
    @FXML
    private Label player2Label;
    @FXML
    private Label player3Label;
    @FXML
    private Label player4Label;
    @FXML
    private Label yourNameLabel;
    @FXML
    private ImageView firstPlayerChair;
    @FXML
    private ImageView activeShelf;
    @FXML
    private Pane shelfItemPane;
    @FXML
    private Button confirmSelection;
    @FXML
    private List<Label> ordinalSelectionFlag;
    @FXML
    private List<ImageView> imgsList;
    @FXML
    private List<BorderPane> wrappersList;
    private final Map<String, BorderPane> wrapperMap = new HashMap<>();
    @FXML
    private List<Button> itemsList;
    @FXML
    private List<Button> shelfColumnButtonList;
    private int numOfItems = 0;
    private boolean[][] gameboardItemMatrix = new boolean[9][9];
    private List<int[]> selectedCoords = new ArrayList<>();
    private List<Integer> columnReference = new ArrayList<>();
    private List<Command> commands = Arrays.asList(new SelectItemsCommand(this.selectedCoords),
            new SelectColumnCommand(this.columnReference));
    private GUI gui;
    private final List<Label> activeLabels = new ArrayList<>();
    private List<String> itemImgPath = new ArrayList<>();
    private List<BorderPane> borderPanes = new ArrayList<>();
    private int prevColSelected;
    private String warning = "sounds/Warning.mp3";
    private String MenuSelection = "sounds/MenuSelection.mp3";
    private String ItemSelect = "sounds/ItemSelect.mp3";
    private MediaPlayer mediaPlayer;
    private static final int SHELF_ROWS = 6;
    private static final int SHELF_COLS = 5;

    public void initialize(){
        int counter = 0;
        for(BorderPane bp : wrappersList){
            wrapperMap.put(itemsList.get(counter).toString(), bp);
            counter++;
        }
    }

    public void objectivesButtonAction(ActionEvent event){
        System.out.println("Objectives button pressed");
        playSound(MenuSelection);
        String scenePath = "Objectives.fxml";
        this.gui.openNewWindow(scenePath);
    }

    public void updateMessageBox(String msg){
        messageBox.setText(msg);
    }
    public void updateScoreLabel(int score){
        scoreLabel.setText(Integer.toString(score));
    }
    public void updateGameboard(GameBoardView gameBoardView){
        SelectItemsCommand sic = (SelectItemsCommand) this.commands.get(0);
        sic.setGameBoardView(gameBoardView);
    }
    public void updateShelf(ShelfView shelfView){
        //fare displayare la shelf
        for(int i=SHELF_ROWS;i>=0;i--){
            //if(shelfView.getShelfGrid()[i][this.prevColSelected].getCategoryType() != null)
        }
        for(int i=0;i<SHELF_ROWS;i++){
            for(int j=0;j<SHELF_COLS;j++){
                Category category = shelfView.getShelfGrid()[i][j].getCategoryType();
            }
        }
    }
    public void updateCurrentTurnLabel(String msg){
        if(player1Label.getText().equals("%DEFAULT%")){
            StringBuilder temp = new StringBuilder();
            temp.append(msg, 1, msg.length());
            player1Label.setVisible(true);
            Label[] playerNameLabels = {player1Label, player2Label, player3Label, player4Label};
            int counter = 0;
            while(temp.toString().contains("!")){
                playerNameLabels[counter].setVisible(true);
                playerNameLabels[counter].setText(temp.substring(0, temp.indexOf("!")));
                temp.delete(0, temp.indexOf("!") + 1);
                activeLabels.add(playerNameLabels[counter]);
                counter++;
            }
        }
        for(Label label : activeLabels){
            if(label.getText().equals(msg)){
                label.setTextFill(Color.RED);
                label.setStyle("-fx-font-size: 25px");
            } else {
                label.setTextFill(Color.BLACK);
                label.setStyle("-fx-font-size: 20px");
            }
        }
    }
    public void updateYourNameLabel(String name){
        yourNameLabel.setVisible(true);
        yourNameLabel.setText(name);
    }

    public void itemClick(ActionEvent event){
        this.messageBox.setText("");
        System.out.println("Item click");
        playSound(ItemSelect);
        System.out.println(event.getSource().toString());
        String temp = event.getSource().toString();
        String[] stringArray = temp.split("_");
        int row = Integer.parseInt(stringArray[1]);
        int col = Integer.parseInt(stringArray[2]);
        int[] coords = {row, col};
        if(!gameboardItemMatrix[row][col]){
            if(numOfItems >= 3) return;
            //da aggiungere
            gameboardItemMatrix[row][col] = true;
            numOfItems++;
            selectedCoords.add(coords);
            BorderPane bp = wrapperMap.get(event.getSource().toString());
            bp.getStyleClass().add("image-view-wrapper");
            double x = bp.localToScene(bp.getBoundsInLocal()).getMaxX();
            double y = bp.localToScene(bp.getBoundsInLocal()).getMinY();
            Label toDisplay = ordinalSelectionFlag.get(numOfItems - 1);
            toDisplay.relocate(x - 10, y);
            toDisplay.setVisible(true);
            if(numOfItems == 1) {
                confirmSelection.setOpacity(1);
                confirmSelection.setDisable(false);
            }
        } else {
            if(numOfItems >= 0){
                //SISTEMARE NUMERINI
                int index = selectedCoords.indexOf(selectedCoords.stream()
                        .filter(i -> i[0] == row && i[1] == col).findFirst().get());
                //selectedCoords.removeIf(c -> c[0] == row && c[1] == col);
                selectedCoords.remove(index);
                gameboardItemMatrix[row][col] = false;
                BorderPane bp = wrapperMap.get(event.getSource().toString());
                bp.getStyleClass().clear();
                Label toDisplay = ordinalSelectionFlag.get(index);
                toDisplay.setVisible(false);
                toDisplay.relocate(1, 1);
                numOfItems--;
                Map<Integer, List<Double>> labelCoords = new HashMap<>();
                int counter = 0;
                for(Label l : ordinalSelectionFlag){
                    List<Double> labelXandY = Arrays.asList(l.getLayoutX(), l.getLayoutY());
                    labelCoords.put(counter, labelXandY);
                    counter++;
                }
               for(int i=0;i<numOfItems;i++){
                   ordinalSelectionFlag.get(i).relocate(labelCoords.get(i).get(0), labelCoords.get(i).get(1));
               }
                if(numOfItems == 0) {
                    confirmSelection.setOpacity(0.6);
                    confirmSelection.setDisable(true);
                }
            }
        }
    }

    public void confirmSelectionAction(ActionEvent event){
        try{
            this.commands.get(0).check();
            this.gameboardPane.setDisable(true);
            for(Button b : shelfColumnButtonList){
                b.setVisible(true);
                b.setDisable(false);
            }
            confirmSelection.setOpacity(0.6);
            ordinalSelectionFlag.forEach(osf -> {
                osf.relocate(1, 1);
                osf.setVisible(false);
            });
            List<BorderPane> bps = wrappersList.stream()
                    .filter(borderPane -> borderPane.getStyleClass().size() == 1).toList();
            this.borderPanes.addAll(bps);
            this.itemImgPath.addAll(bps.stream()
                    .map(borderPane -> ((ImageView) borderPane.getCenter()).getImage().getUrl()).toList());
            bps.forEach(borderPane -> borderPane.getStyleClass().clear());
        } catch (InvalidSelectionException e) {
            playSound(warning);
            this.updateMessageBox("Invalid items selection:\n" + e.getMessage());
        } catch (FullColumnException ignored) {
        }
    }

    public void shelfColumnButtonAction(ActionEvent event){
        String[] cols = {"1", "2", "3", "4", "5"};
        String eventString = event.getSource().toString();
        for(String col : cols)
            if(eventString.contains(col))
                this.columnReference.add(Integer.parseInt(col));
        try{
            this.commands.get(1).check();
            for(Button b : shelfColumnButtonList){
                b.setDisable(true);
                b.setVisible(false);
            }
            borderPanes.forEach(borderPane -> ((ImageView) borderPane.getCenter()).setImage(null));
            System.out.println("BEFORE NOTIFY, := " + this.columnReference.get(0));
            this.activeShelf.setOpacity(0.7);
            this.shelfItemPane.setOpacity(0.7);
            this.prevColSelected = this.columnReference.get(0);
            this.gui.setChangedAndNotifyListener(this.selectedCoords, this.columnReference.get(0));
        } catch (InvalidSelectionException ignored) {
        } catch (FullColumnException e) {
            playSound(warning);
            this.columnReference.clear();
            this.updateMessageBox("Wrong column selection: " + e.getMessage());
        }

    }

    public void activateShelf(ShelfView shelfView){
        this.activeShelf.setOpacity(1);
        this.shelfItemPane.setOpacity(1);
        ((SelectColumnCommand) this.commands.get(1)).setShelfView(shelfView);
    }

    public void activateFirstPlayerChair(){
        this.firstPlayerChair.setVisible(true);
    }

    @Override
    public void setGUI(GUI gui) {
        this.gui = gui;
    }

    @Override
    public void playSound(String filePath) {
        Media pick = new Media(Objects.requireNonNull(getClass().getClassLoader()
                .getResource(filePath)).toExternalForm());
        mediaPlayer = new MediaPlayer(pick);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setVolume(25);
        mediaPlayer.setOnEndOfMedia(() -> {
            mediaPlayer.seek(Duration.ZERO);
            mediaPlayer.stop();
        });
    }
}

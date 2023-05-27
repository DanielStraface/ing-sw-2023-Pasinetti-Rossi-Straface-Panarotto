package it.polimi.ingsw.client.GUI.controllers;

import it.polimi.ingsw.client.CLI.commands.Command;
import it.polimi.ingsw.client.CLI.commands.SelectColumnCommand;
import it.polimi.ingsw.client.CLI.commands.SelectItemsCommand;
import it.polimi.ingsw.client.GUI.GUI;
import it.polimi.ingsw.exceptions.FullColumnException;
import it.polimi.ingsw.exceptions.InvalidSelectionException;
import it.polimi.ingsw.modelview.GameBoardView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.*;

public class MainGameController implements GUIController{
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
    private Button confirmSelection;
    @FXML
    private List<ImageView> imgsList;
    @FXML
    private List<BorderPane> wrappersList;
    private final Map<String, BorderPane> wrapperMap = new HashMap<>();
    @FXML
    private List<Button> itemsList;
    private int numOfItems = 0;
    private boolean[][] gameboardItemMatrix = new boolean[9][9];
    private List<int[]> selectedCoords = new ArrayList<>();
    private List<Integer> columnReference = new ArrayList<>();
    private List<Command> commands = Arrays.asList(new SelectItemsCommand(this.selectedCoords),
            new SelectColumnCommand(this.columnReference));
    private GUI gui;
    private final List<Label> activeLabels = new ArrayList<>();
    private String warning = "sounds/Warning.mp3";
    private String MenuSelection = "sounds/MenuSelection.mp3";
    private String ItemSelect = "sounds/ItemSelect.mp3";
    private MediaPlayer mediaPlayer;

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
    public void updateShelf(){

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
            wrapperMap.get(event.getSource().toString()).getStyleClass().add("image-view-wrapper");
            if(numOfItems == 1) {
                confirmSelection.setOpacity(1);
                confirmSelection.setDisable(false);
            }
        }
        else {
            if(numOfItems >= 0){
                selectedCoords.removeIf(c -> c[0] == row && c[1] == col);
                gameboardItemMatrix[row][col] = false;
                numOfItems--;
                wrapperMap.get(event.getSource().toString()).getStyleClass().clear();
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
            System.out.println("Sono stati selezionati: ");
            for (int[] selectedCoord : selectedCoords)
                System.out.println(selectedCoord[0] + "  " + selectedCoord[1] + ", ");
        } catch (InvalidSelectionException e) {
            playSound(warning);
            this.updateMessageBox("Invalid items selection:\n" + e.getMessage());
        } catch (FullColumnException ignored) {
        }
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

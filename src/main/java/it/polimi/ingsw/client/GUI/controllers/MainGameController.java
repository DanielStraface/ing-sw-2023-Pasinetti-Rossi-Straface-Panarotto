package it.polimi.ingsw.client.GUI.controllers;

import it.polimi.ingsw.client.GUI.GUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private GUI gui;
    private List<Label> activeLabels = new ArrayList<>();
    private String MenuSelection = "sounds/MenuSelection.mp3";
    private MediaPlayer mediaPlayer;

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
    public void updateGameboard(){

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
            //xX_warLord_Xx
        }
    }
    public void updateYourNameLabel(String name){
        yourNameLabel.setVisible(true);
        yourNameLabel.setText(name);
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

package it.polimi.ingsw.client.GUI.controllers;

import it.polimi.ingsw.client.GUI.GUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class MatchChoicesController implements GUIController, Initializable {
    @FXML
    private ChoiceBox<String > typeOfMatchBox;
    @FXML
    private ChoiceBox<String> numOfPlayersBox;
    @FXML
    private TextField textField;
    @FXML
    private Label nicknameLabel;
    @FXML
    private Label numOfItemsLabel;
    @FXML
    private Label typeOfMatchLabel;
    private final String[] typeOfMatch = {"", "Create a new match", "Join an existing match"};
    private final String[] numOfPlayers = {"", "2 players", "3 players", "4 players"};
    private GUI gui;
    @Override
    public void setGUI(GUI gui) {
        this.gui = gui;
    }
    private enum State {NICKNAME, TYPE_OF_MATCH, NUM_OF_PLAYER}
    private State flag = State.NICKNAME;
    private String userNickname;
    private String typeOfMatchChoice;
    private MediaPlayer mediaPlayer;

    public void confirmButtonAction(ActionEvent event){
        System.out.println("Confirm button press");
        switch (flag){
            case NICKNAME -> {
                playSelectionSound();
                String nicknameValue = textField.getText();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                if(nicknameValue.length() > 1){
                    this.userNickname = nicknameValue;
                    typeOfMatchLabel.setOpacity(1);
                    typeOfMatchBox.setOpacity(1);
                    typeOfMatchBox.setDisable(false);
                    nicknameLabel.setOpacity(0.5);
                    textField.setOpacity(0.5);
                    textField.setDisable(true);
                    flag = State.TYPE_OF_MATCH;
                    return;
                }
            }
            case TYPE_OF_MATCH -> {
                playSelectionSound();
                String typeOfMatchValue = typeOfMatchBox.getValue();
                if(typeOfMatchValue.equals("")) return;
                if(typeOfMatchValue.equals("Create a new match")){
                    this.typeOfMatchChoice = typeOfMatchValue;
                    numOfItemsLabel.setOpacity(1);
                    numOfPlayersBox.setDisable(false);
                    numOfPlayersBox.setOpacity(1);
                    typeOfMatchBox.setOpacity(0.5);
                    typeOfMatchBox.setDisable(true);
                    typeOfMatchLabel.setOpacity(0.5);
                    flag = State.NUM_OF_PLAYER;
                    return;
                }
                System.out.println(typeOfMatchValue);
            }
            case NUM_OF_PLAYER -> {
                playSelectionSound();
                String numOfPlayersBoxValue = numOfPlayersBox.getValue();
                if(numOfPlayersBoxValue.equals("")) return;
                System.out.println(numOfPlayersBoxValue);
            }
        }

        /*type
        if(typeOfMatchBoxValue.equals("Create a new match")){
            numOfItemsLabel.setOpacity(1);
            numOfPlayersBox.setDisable(false);
            numOfPlayersBox.setOpacity(1);
        }
        String numOfPlayersBoxValue = numOfPlayersBox.getValue();
        if(numOfPlayersBoxValue != null && typeOfMatchBoxValue.equals("Join an existing match")){
            alert.setTitle("ERROR");
            alert.setHeaderText("Invalid selection!");
            alert.setContentText("You insert to join an existing match with a numOfPlayer selected!");
            alert.showAndWait();
            return;
        }
        System.out.println("Ready to execute the user request");
        alert.setTitle("ERROR");
        alert.setHeaderText("Invalid nickname!");
        alert.setContentText("Your forgot to enter your name!");*/

    }

    public void prevButtonAction(ActionEvent event){
        playSelectionSound();
        if(flag == State.NICKNAME){
            this.userNickname = null;
            textField.setText("");
            return;
        }
        if(flag == State.TYPE_OF_MATCH){
            flag = State.NICKNAME;
            this.userNickname = null;
            typeOfMatchBox.setValue("");
            typeOfMatchLabel.setOpacity(0.5);
            typeOfMatchBox.setOpacity(0.5);
            typeOfMatchBox.setDisable(true);
            nicknameLabel.setOpacity(1);
            textField.setOpacity(1);
            textField.setDisable(false);
            textField.setText("");
            return;
        }
        if(flag == State.NUM_OF_PLAYER){
            flag = State.TYPE_OF_MATCH;
            typeOfMatchChoice = null;
            numOfItemsLabel.setOpacity(0.5);
            numOfPlayersBox.setOpacity(0.5);
            numOfPlayersBox.setValue("");
            numOfPlayersBox.setDisable(true);
            typeOfMatchLabel.setOpacity(1);
            typeOfMatchBox.setOpacity(1);
            typeOfMatchBox.setDisable(false);
            typeOfMatchBox.setValue("");
        }
    }

    public void backButtonAction(ActionEvent event){
        playSelectionSound();
        System.out.println("Back button pressed, go back to main menu");
        this.userNickname = null;
        this.typeOfMatchChoice = null;
        nicknameLabel.setOpacity(1);
        textField.setOpacity(1);
        textField.setText("");
        textField.setDisable(false);
        typeOfMatchLabel.setOpacity(0.5);
        typeOfMatchBox.setOpacity(0.5);
        typeOfMatchBox.setValue("");
        typeOfMatchBox.setDisable(true);
        numOfItemsLabel.setOpacity(0.5);
        numOfPlayersBox.setOpacity(0.5);
        numOfPlayersBox.setValue("");
        numOfPlayersBox.setDisable(true);
        flag = State.NICKNAME;
        gui.changeScene("MainMenu.fxml");
    }

    private void playSelectionSound(){
        Media pick = new Media(Objects.requireNonNull(getClass().getClassLoader()
                .getResource("sounds/MenuSelection.mp3")).toExternalForm());
        mediaPlayer = new MediaPlayer(pick);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setVolume(25);
        mediaPlayer.setOnEndOfMedia(() -> {
            mediaPlayer.seek(Duration.ZERO);
            mediaPlayer.stop();
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        typeOfMatchBox.getItems().addAll(typeOfMatch);
        numOfPlayersBox.getItems().addAll(numOfPlayers);
    }
}

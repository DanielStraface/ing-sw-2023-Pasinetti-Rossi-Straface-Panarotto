package it.polimi.ingsw.client.GUI.controllers;

import it.polimi.ingsw.client.GUI.GUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ChoicesMenuController implements GUIController, Initializable {
    @FXML
    private Label networkLabel;
    @FXML
    private ChoiceBox<String> networkChoices;
    @FXML
    private Label serverAddressLabel;
    @FXML
    private TextField serverAddressTextField;
    private Stage stage;
    private Scene scene;
    private String[] networks = {"", "RMI", "SOCKET"};
    private GUI gui;
    private String MenuSelection = "sounds/MenuSelection.mp3";
    private MediaPlayer mediaPlayer;


    public void setGUI(GUI gui){this.gui = gui;}



    public void backButtonAction(ActionEvent event) {
        playSound(MenuSelection);
        System.out.println("Back button pressed, go back to main menu");
        networkChoices.setValue("");
        serverAddressTextField.setText("");
        gui.changeScene("MainMenu.fxml");
    }

    public void confirmButtonAction(ActionEvent event){
        playSound(MenuSelection);
        String netChoice = networkChoices.getValue();
        String address = serverAddressTextField.getText();
        if(netChoice == null || netChoice.equals("") || address == null || address.equals("")){
            System.out.println("Wrong selection");
            return;
        }
        System.out.println("The choice is " + netChoice + ", address " + address);
        this.gui.imposeTheTypeOfConnection(netChoice, address);

        networkChoices.setValue("");
        serverAddressTextField.setText("");
        gui.changeScene("MatchChoices.fxml");

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        networkChoices.getItems().addAll(networks);
    }
}

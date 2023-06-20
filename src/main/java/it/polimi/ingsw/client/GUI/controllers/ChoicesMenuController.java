package it.polimi.ingsw.client.GUI.controllers;

import it.polimi.ingsw.client.CLI.AppClient;
import it.polimi.ingsw.client.GUI.GUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ChoicesMenuController implements GUIController, Initializable {
    @FXML
    private ChoiceBox<String> networkChoices;
    @FXML
    private Label invalidIpLabel;
    @FXML
    private TextField serverAddressTextField;
    private final String[] networks = {"", "RMI", "SOCKET"};
    private GUI gui;
    private final String MenuSelection = "sounds/MenuSelection.wav";
    private MediaPlayer mediaPlayer;

    /**
     * method to set gui for the application
     * @param gui -> GUI
     */
    public void setGUI(GUI gui){this.gui = gui;}

    /**
     * backButtonAction handles the press of the back button that returns to the main menu
     * @param event the event to be managed
     */
    public void backButtonAction(ActionEvent event) {
        playSound(MenuSelection);
        System.out.println("Back button pressed, go back to main menu");
        networkChoices.setValue("");
        serverAddressTextField.setText("");
        gui.changeScene("MainMenu.fxml");
    }

    /**
     * confirmButtonAction handles the press of the confirm button
     * @param event event to be managed
     */
    public void confirmButtonAction(ActionEvent event){
        playSound(MenuSelection);
        invalidIpLabel.setText("");
        String netChoice = networkChoices.getValue();
        String address = serverAddressTextField.getText();
        if(netChoice == null || netChoice.equals("") || address == null || address.equals("")){
            System.out.println("Wrong selection");
            return;
        }
        if(!AppClient.checkIp(address)){
            System.out.println("Invalid ip address");
            invalidIpLabel.setText("The ip address is not valid,\nplease insert a valid ip address!");
            return;
        }
        System.out.println("The choice is " + netChoice + ", address " + address);
        this.gui.imposeTheTypeOfConnection(netChoice, address);

        networkChoices.setValue("");
        serverAddressTextField.setText("");
        gui.changeScene("MatchChoices.fxml");

    }

    /**
     * Plays a sound given its path String
     * @param filePath String with the file's path
     */
    @Override
    public void playSound(String filePath) {
        Media pick = new Media(Objects.requireNonNull(getClass().getClassLoader()
                .getResource(filePath)).toExternalForm());
        mediaPlayer = new MediaPlayer(pick);
        mediaPlayer.setVolume(0.1);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setOnEndOfMedia(() -> {
            mediaPlayer.seek(Duration.ZERO);
            mediaPlayer.stop();
        });
    }

    /**
     * initializes the choice box of connection with /RMI/SOCKET
     * @param url URL
     * @param resourceBundle ResourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        networkChoices.getItems().addAll(networks);
    }
}

package it.polimi.ingsw.client.GUI.controllers;

import it.polimi.ingsw.client.CLI.AppClientRMI;
import it.polimi.ingsw.client.CLI.AppClientSocket;
import it.polimi.ingsw.client.GUI.GUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Objects;
import java.util.ResourceBundle;

public class ChoicesMenuController implements GUIController, Initializable {
    @FXML
    private Label networkLabel;
    @FXML
    private ChoiceBox<String> networkChoices;
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
        gui.changeScene("MainMenu.fxml");
    }

    public void confirmButtonAction(ActionEvent event){
        playSound(MenuSelection);
        String choice = networkChoices.getValue();
        if(choice == null || choice.equals("")){
            System.out.println("Wrong selection");
            return;
        }
        System.out.println("The choice is " + choice);
        this.gui.imposeTheTypeOfConnection(choice);

        networkChoices.setValue("");
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

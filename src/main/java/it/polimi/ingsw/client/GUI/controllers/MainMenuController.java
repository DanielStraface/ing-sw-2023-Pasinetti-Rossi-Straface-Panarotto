package it.polimi.ingsw.client.GUI.controllers;

import it.polimi.ingsw.client.GUI.GUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class MainMenuController implements GUIController{
    @FXML
    private AnchorPane scenePane;
    private Stage stage;
    private Scene scene;
    private GUI gui;
    private MediaPlayer mediaPlayer;

    public void playButtonAction(ActionEvent event) throws IOException {
        System.out.println("Play button pressed, application start");
        playSelectionSound();
        //Parent root = FXMLLoader.load(getClass().getResource("/fxml/ChoicesMenu.fxml"));
        //stage = (Stage) (((Node) event.getSource()).getScene().getWindow());
        gui.changeScene("ChoicesMenu.fxml");
        //scene = new Scene(root);
        //stage.setScene(scene);
        //String css = this.getClass().getResource("/css/MainMenu.css").toExternalForm();
        //scene.getStylesheets().add(css);
        //stage.show();
    }

    public void quitButtonAction(ActionEvent event){
        playSelectionSound();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Close application");
        alert.setHeaderText("You're about to close MyShelfie");
        alert.setContentText("Are you sure you want to quit?");
        if(alert.showAndWait().get() == ButtonType.OK){
            stage = (Stage) scenePane.getScene().getWindow();
            System.out.println("You're successfully logged out");
            stage.close();
        }
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
    public void setGUI(GUI gui) {
        this.gui = gui;
    }
}

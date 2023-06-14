package it.polimi.ingsw.client.GUI.controllers;

import it.polimi.ingsw.client.GUI.GUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Objects;

public class MainMenuController implements GUIController{
    @FXML
    private AnchorPane scenePane;
    private Stage stage;
    private Scene scene;
    private GUI gui;
    private String MenuSelection = "sounds/MenuSelection.wav";
    private MediaPlayer mediaPlayer;

    /**
     * handles the action of press "play" button in this way the application start.
     * @param event event to be managed
     */
    public void playButtonAction(ActionEvent event) throws IOException {
        System.out.println("Play button pressed, application start");
        playSound(MenuSelection);
        //Parent root = FXMLLoader.load(getClass().getResource("/fxml/ChoicesMenu.fxml"));
        //stage = (Stage) (((Node) event.getSource()).getScene().getWindow());
        gui.changeScene("ChoicesMenu.fxml");
        //scene = new Scene(root);
        //stage.setScene(scene);
        //String css = this.getClass().getResource("/css/MainMenu.css").toExternalForm();
        //scene.getStylesheets().add(css);
        //stage.show();
    }

    /**
     * handles the event of press "quit" button, in this way the application is closed and the
     * player is logged out.
     * @param event event to be managed
     */
    public void quitButtonAction(ActionEvent event){
        playSound(MenuSelection);
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

    /**
     * plays a sound effect from the specified file path.
     * @param filePath String
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
     * set method for the GUI.
     * @param gui GUI
     */
    @Override
    public void setGUI(GUI gui) {
        this.gui = gui;
    }


}

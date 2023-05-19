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
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController implements GUIController{
    @FXML
    private AnchorPane scenePane;
    private Stage stage;
    private Scene scene;
    private GUI gui;
    public void playButtonAction(ActionEvent event) throws IOException {
        System.out.println("Play button pressed, application start");
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

    @Override
    public void setGUI(GUI gui) {
        this.gui = gui;
    }
}

package it.polimi.ingsw.view.GUI.controllers;

import it.polimi.ingsw.client.AppClientRMI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

public class ChoicesMenuController implements Initializable {
    @FXML
    private Label networkLabel;
    @FXML
    private ChoiceBox<String> networkChoices;
    private Stage stage;
    private Scene scene;
    private String[] networks = {"RMI", "SOCKET"};

    public void backButtonAction(ActionEvent event) throws IOException {
        System.out.println("Back button pressed, go back to main menu");
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainMenu.fxml"));
        stage = (Stage) (((Node) event.getSource()).getScene().getWindow());
        scene = new Scene(root);
        stage.setScene(scene);
        String css = this.getClass().getResource("/css/MainMenu.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.show();
    }

    public void confirmButtonAction(ActionEvent event){
        String choice = networkChoices.getValue();
        if(choice == null){
            System.out.println("Wrong selection");
            return;
        }
        /*if(choice.equals("RMI")) {
            try {
                //AppClientRMI.main(null);
            } catch (RemoteException e) {
                System.err.println("ERROR");
            } catch (NotBoundException e) {
                throw new RuntimeException(e);
            }
        }*/
        System.out.println("The choice is " + choice);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        networkChoices.getItems().addAll(networks);
    }
}

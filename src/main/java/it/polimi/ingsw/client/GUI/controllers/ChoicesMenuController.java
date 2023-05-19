package it.polimi.ingsw.client.GUI.controllers;

import it.polimi.ingsw.client.CLI.AppClientRMI;
import it.polimi.ingsw.client.CLI.AppClientSocket;
import it.polimi.ingsw.client.GUI.GUI;
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

public class ChoicesMenuController implements GUIController, Initializable {
    @FXML
    private Label networkLabel;
    @FXML
    private ChoiceBox<String> networkChoices;
    private Stage stage;
    private Scene scene;
    private String[] networks = {"", "RMI", "SOCKET"};
    private GUI gui;

    public void setGUI(GUI gui){this.gui = gui;}

    public void backButtonAction(ActionEvent event) {
        System.out.println("Back button pressed, go back to main menu");
        networkChoices.setValue("");
        gui.changeScene("MainMenu.fxml");
    }

    public void confirmButtonAction(ActionEvent event){
        String choice = networkChoices.getValue();
        if(choice == null || choice.equals("")){
            System.out.println("Wrong selection");
            return;
        }
        System.out.println("The choice is " + choice);
        String[] mainArgs = {"GUI"};
        if(choice.equals("RMI")) {
            new Thread(() -> {
                try {
                    AppClientRMI.main(mainArgs);
                } catch (RemoteException e) {
                    System.err.println("Cannot launch AppRMI");
                } catch (NotBoundException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
        if(choice.equals("SOCKET")){
            new Thread(() -> {
                try {
                    AppClientSocket.main(mainArgs);
                } catch (RemoteException e) {
                    System.err.println("Cannot launch AppRMI");
                }
            }).start();
        }
        networkChoices.setValue("");
        gui.changeScene("MatchChoices.fxml");
        /*if(choice.equals("RMI")) {
            try {
                //AppClientRMI.main(null);
            } catch (RemoteException e) {
                System.err.println("ERROR");
            } catch (NotBoundException e) {
                throw new RuntimeException(e);
            }
        }*/

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        networkChoices.getItems().addAll(networks);
    }
}

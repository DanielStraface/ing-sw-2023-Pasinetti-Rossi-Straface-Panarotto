package it.polimi.ingsw.client.GUI.controllers;

import it.polimi.ingsw.client.CLI.AppClientRMI;
import it.polimi.ingsw.client.CLI.AppClientSocket;
import it.polimi.ingsw.client.GUI.GUI;
import it.polimi.ingsw.client.UI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * The MatchChoicesController class is responsible for controlling the match choices window in the GUI.
 * It implements the GUIController and Initializable interfaces.
 * The class controls the choices menu screen, which allows the user to select the nickname and the type of match.
 * It displays appropriate messages and options based on the user's choices.
 */
public class MatchChoicesController implements GUIController, Initializable {
    private boolean done;
    private boolean oldMatchFlag;

    @FXML
    private Button confirmButton;
    @FXML
    private Button prevButton;
    @FXML
    private Pane loadingPane;
    @FXML
    private ProgressBar progressBar;
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
    @FXML
    private Label textLabelDisplay;
    @FXML
    private ImageView loadingImage;
    @FXML
    private Label notifyLabel;
    private final String[] typeOfMatch = {"", "Create/Continue a match", "Join an existing match"};
    private final String[] numOfPlayers = {"", "2 players", "3 players", "4 players"};
    private GUI gui;


    private enum State {NICKNAME, TYPE_OF_MATCH, NUM_OF_PLAYER, CHOICE_OBTAIN, WAIT_IN_LOBBY, MATCH_START}
    private State flag = State.NICKNAME;
    private String userNickname;
    private String typeOfMatchChoice;
    private String MenuSelection = "sounds/MenuSelection.wav";
    private MediaPlayer mediaPlayer;
    private String typeOfConnection;
    private String address;
    double denominator;
    double connectedPlayers;

    /**
     * sets the type of connection (SOCKET or RMI) and the address for the connection.
     * @param typeOfConnection RMI/Socket
     * @param address IP address string
     */
    public void setConnectionType(String typeOfConnection, String address){
        this.typeOfConnection = typeOfConnection;
        this.address = address;
    }

    /**
     * handles the event of press "confirm" button based on the choice made, for:
     * -nickname (makes error messages for invalid name selection)
     * -type of match (new game or existing game)
     * -number of player (2,3 or 4 players)
     * notifies the choices made.
     * @param event event to be managed
     */
    public void confirmButtonAction(ActionEvent event){
        if(done) return;
        System.out.println("Confirm button press");
        String numOfPlayersBoxValue = null;
        switch (flag){
            case NICKNAME -> {
                playSound(MenuSelection);
                String nicknameValue = textField.getText();
                if(UI.nicknameController(nicknameValue)){
                    this.userNickname = nicknameValue;
                    typeOfMatchLabel.setOpacity(1);
                    typeOfMatchBox.setOpacity(1);
                    typeOfMatchBox.setDisable(false);
                    nicknameLabel.setOpacity(0.5);
                    textField.setOpacity(0.5);
                    textField.setDisable(true);
                    notifyLabel.setVisible(false);
                    flag = State.TYPE_OF_MATCH;
                    return;
                } else {
                    notifyLabel.setText("""
                    This nickname is not allowed, please try again.""");
                    notifyLabel.setVisible(true);
                    return;
                }
            }
            case TYPE_OF_MATCH -> {
                playSound(MenuSelection);
                String typeOfMatchValue = typeOfMatchBox.getValue();
                if(typeOfMatchValue == null || typeOfMatchValue.equals("")) return;
                if(typeOfMatchValue.equals("Create/Continue a match")){
                    this.typeOfMatchChoice = typeOfMatchValue;
                    numOfItemsLabel.setOpacity(1);
                    numOfPlayersBox.setDisable(false);
                    numOfPlayersBox.setOpacity(1);
                    typeOfMatchBox.setOpacity(0.5);
                    typeOfMatchBox.setDisable(true);
                    typeOfMatchLabel.setOpacity(0.5);
                    flag = State.NUM_OF_PLAYER;
                    return;
                } else {
                    this.typeOfMatchChoice = typeOfMatchValue;
                    typeOfMatchBox.setOpacity(0.5);
                    typeOfMatchBox.setDisable(true);
                    typeOfMatchLabel.setOpacity(0.5);
                    flag = State.CHOICE_OBTAIN;
                }
            }
            case NUM_OF_PLAYER -> {
                playSound(MenuSelection);
                numOfPlayersBoxValue = numOfPlayersBox.getValue();
                if(numOfPlayersBoxValue == null || numOfPlayersBoxValue.equals("")) return;
                flag = State.CHOICE_OBTAIN;
                System.out.println(numOfPlayersBoxValue);
            }
        }
        if(flag == State.CHOICE_OBTAIN){
            System.out.print("\nThe user choice is : \n\tnickname := " + userNickname +
                    ", typeOFMatch := " + typeOfMatchChoice);
            if(typeOfMatchChoice.equals("Create a new match"))
                System.out.print(", numOfPlayers := " + numOfPlayersBoxValue + "\n");
            Object[] parameters = {"GUI", address, userNickname, typeOfMatchChoice, numOfPlayersBoxValue, this.gui};
            System.out.println("n := " + numOfPlayersBoxValue);
            if(typeOfConnection.equals("RMI")){
                new Thread(() -> {
                    try {
                        AppClientRMI.launchClient(parameters);
                    } catch (RemoteException e) {
                        System.err.println("Error while running the AppClientRMI");
                        System.exit(-2);
                    } catch (NotBoundException e) {
                        System.err.println("Error on registry");
                    }
                }).start();
            }
            if(typeOfConnection.equals("SOCKET")){
                new Thread(() -> {
                    try {
                        AppClientSocket.launchClient(parameters);
                    } catch (RemoteException e) {
                        System.err.println("Error while running the AppClientSocket");
                        System.exit(-2);
                    }
                }).start();
            }
            done = true;
        }
    }

    /**
     * returns to the previous choice.
     * from TYPE OF MATCH to NICKNAME
     * from TYPE OF MATCH to NUMBER OF PLAYERS
     * @param event event to be managed
     */
    public void prevButtonAction(ActionEvent event){
        playSound(MenuSelection);
        if(flag == State.NICKNAME){
            this.userNickname = null;
            textField.setText("");
            done = false;
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
            done = false;
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
            done = false;
        }
    }

    /**
     * returns to main menu
     * @param event event to be managed
     */
    public void backButtonAction(ActionEvent event){
        playSound(MenuSelection);
        System.out.println("Back button pressed, go back to main menu");
        this.userNickname = null;
        this.typeOfMatchChoice = null;
        done = false;
        resetSceneAtBeginning();
        flag = State.NICKNAME;
        gui.changeScene("MainMenu.fxml");
    }

    /**
     * if the message is "searching" this method manages the process of waiting in the lobby for finding other players;
     * If the message is "correct", if the current state is WAIT_IN_LOBBY, it moves to the MATCH_START state, otherwise
     * it displays a loading pane; then sets the main game window as the next window.
     * If the message isn't "joining a lobby..." the method updates the notification label with the message.
     * @param msg information message displayed
     */
    public void displayMsgInfo(String msg){
        if(msg.contains("disconnected!")){
            this.gui.anotherUserDisconnection(msg);
        }
        if(msg.contains("searching")){
            int remainNumOfPlayers = 0;
            char[] chars = msg.toCharArray();
            StringBuilder sb = new StringBuilder();
            for(char c : chars){
                if(Character.isDigit(c))
                    sb.append(c);
            }
            if(sb.toString().equals("1") || sb.toString().equals("2") || sb.toString().equals("3"))
                remainNumOfPlayers = Integer.parseInt(sb.toString());
            textLabelDisplay.setText(msg);
            if(flag != State.WAIT_IN_LOBBY){
                loadingPane.setVisible(true);
                loadingImage.setVisible(true);
                if(msg.contains("TWO")) this.denominator = 2;
                if(msg.contains("THREE")) this.denominator = 3;
                if(msg.contains("FOUR")) this.denominator = 4;
                if(this.denominator - 1 == remainNumOfPlayers) this.connectedPlayers = 1;
                else this.connectedPlayers = this.denominator - remainNumOfPlayers;
                flag = State.WAIT_IN_LOBBY;
            } else this.connectedPlayers += 1;
            double loadingPercentage = connectedPlayers / this.denominator;
            progressBar.setProgress(loadingPercentage);
        } else if(msg.contains("unfinished") || msg.contains("Correct")){
            if(msg.contains("unfinished")) oldMatchFlag = true;
            if(flag == State.WAIT_IN_LOBBY) flag = State.MATCH_START;
            else {
                loadingPane.setVisible(true);
                loadingImage.setVisible(true);
            }
            textLabelDisplay.setText(msg);
            progressBar.setProgress(1);
            loadingPane.setVisible(false);
            loadingImage.setVisible(false);
            textField.setText("");
            textField.setDisable(false);
            textField.setOpacity(1);
            nicknameLabel.setOpacity(1);
            typeOfMatchBox.setValue("");;
            numOfPlayersBox.setValue("");
            numOfPlayersBox.setDisable(true);
            numOfPlayersBox.setOpacity(0.5);
            numOfItemsLabel.setOpacity(0.5);
            done = false;
            flag = State.NICKNAME;
            gui.changeScene("MainGame.fxml");
        } else if(!msg.equals("Joining a lobby...")) {
            notifyLabel.setText(msg);
            notifyLabel.setVisible(true);
            confirmButton.setDisable(true);
            confirmButton.setOpacity(0.5);
            prevButton.setDisable(true);
            prevButton.setOpacity(0.5);
        }
    }

    /**
     * when the nickname is already used wrongNickname method resets the scene to the beginning state and displays
     * a notification message
     */
    public void wrongNickname(){
        flag = State.NICKNAME;
        done = false;
        resetSceneAtBeginning();
        notifyLabel.setText("This nickname is already used!\nPlease choose another one");
        notifyLabel.setVisible(true);
    }

    /**
     * reset the scene to its initial state at the beginning.
     */
    private void resetSceneAtBeginning(){
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
        notifyLabel.setVisible(false);
        confirmButton.setOpacity(1);
        confirmButton.setDisable(false);
        prevButton.setOpacity(1);
        prevButton.setDisable(false);
    }

    /**
     * Get method for a previous match found boolean flag
     * @return oldMatchFlag boolean
     */
    public boolean getOldMatch(){ return this.oldMatchFlag; }

    /**
     * Set method for a previous match found boolean flag to false (after all the respective methods have been called)
     */
    public void setOldMatchFalse(){ this.oldMatchFlag = false; }

    /**
     * method to set gui for the application
     * @param gui GUI
     */
    @Override
    public void setGUI(GUI gui) {
        this.gui = gui;
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
     * in the match choice window, it fills the boxes that represents the options that the player can choose about
     * the type of game and the number of players.
     * @param url URL
     * @param resourceBundle ResourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        typeOfMatchBox.getItems().addAll(typeOfMatch);
        numOfPlayersBox.getItems().addAll(numOfPlayers);
    }
}

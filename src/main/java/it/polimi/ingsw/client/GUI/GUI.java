package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.GUI.controllers.*;
import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.distributed.Server;
import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.modelview.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class GUI extends Application implements UI {
    private static final String MAIN_MENU = "MainMenu.fxml";
    private static final String SETUP_CHOICES = "ChoicesMenu.fxml";
    private static final String MATCH_CHOICES = "MatchChoices.fxml";
    private static final String MAIN_GAME = "MainGame.fxml";
    private static final String OBJECTIVES = "Objectives.fxml";
    private static final String PLAYERS_SHELF = "PlayersShelf.fxml";
    private final HashMap<String, Scene> scenes = new HashMap<>();
    private final HashMap<String, GUIController> guiControllers = new HashMap<String, GUIController>();
    private Stage stage;
    private GUIController currentController;
    private MainGameController mainGameController;
    private MatchChoicesController matchChoicesController;
    private final String css = this.getClass().getResource("/css/MainMenu.css").toExternalForm();
    private boolean objectivesWinIsOpen = false;
    private boolean shelfWinIsOpen = false;
    private Client refClient;
    private enum State {SETUP, IN_GAME}
    private State state = State.SETUP;
    private boolean areCardsSet;
    private boolean otherPlayersShelfSetupFlag;
    private boolean firstPlayerChairFlag;
    private int prevNumOfItemOnGameBoard;
    private boolean isRefilledFlag = true;
    private String winner = null;
    private List<CommonObjCardView> commonObjCardViewList;
    private String TurnChange = "sounds/TurnChange.wav";
    private String GameWin = "sounds/GameWin.wav";
    private String GameLose = "sounds/GameLose.wav";
    private final String PointsGet = "sounds/PointsGet.wav";
    private static final int DIM_GAMEBOARD = 9;
    private static final int OCCUPIED = 2;
    private boolean changed;
    private final Vector<Server> observers = new Vector<>();

    /**
     * Sets a type of connection and the IP address to connect to
     * @param connectionType RMI/Socket
     * @param address IP address String
     */
    public void imposeTheTypeOfConnection(String connectionType, String address){
        ((MatchChoicesController) currentController).setConnectionType(connectionType, address);
    }

    /**
     * Checks if the inserted nickname is valid or not
     */
    public void askNicknameManager(){
        MatchChoicesController ctrl = ((MatchChoicesController) guiControllers.get("MatchChoices.fxml"));
        Platform.runLater(ctrl::wrongNickname);
    }

    /**
     * Changes from a scene to another one
     * @param nextScene the scene that the current one needs to switch into
     */
    public void changeScene(String nextScene){
        if(stage.isIconified()) stage.setIconified(false);
        Scene currentScene = scenes.get(nextScene);
        currentScene.getStylesheets().add(css);
        stage.setScene(currentScene);
        if(nextScene.equals("MainGame.fxml")){
            Platform.runLater(() -> {
                try {
                    mainGameController.updateYourNameLabel(this.refClient.getNickname());
                } catch (RemoteException e) {
                    System.err.println("Cannot read refClient value");
                }
            });
            stage.setOnCloseRequest(event -> {
                event.consume();
                quitActionInMainGame(stage);
            });
        } else {
            stage.setOnCloseRequest(event -> {
                event.consume();
                quitButtonAction(stage);
            });
        }
        stage.show();
        centerWindow(stage);
    }

    /**
     * Starts the first Scene (Main menu)
     * @param stage Main menu scene
     */
    @Override
    public void start(Stage stage){
        this.stage = stage;
        setup();
        try {
            stage.setTitle("MyShelfie");
            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                    "/graphics/Publisher_material/Icon_50x50px.png"))));
            stage.setResizable(false);
            stage.setOnCloseRequest(event -> {
                event.consume();
                quitButtonAction(stage);
            });

            changeScene("MainMenu.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Main method for GUI
     * @param args String[]
     */
    public static void main(String[] args){ launch(args);}

    /**
     * Manages the alertBox that pops up when closing the application in the Main Menu
     * @param stage the stage from where the user closes the application
     */
    public void quitButtonAction(Stage stage){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Close application");
        alert.setHeaderText("You're about to close MyShelfie");
        alert.setContentText("Are you sure you want to quit?");
        if(alert.showAndWait().get() == ButtonType.OK){
            System.out.println("You're successfully logged out");
            stage.close();
        }
    }

    /**
     * Manages the alertBox that pops up when closing the application during a match
     * @param stage  the stage from where the user closes the application
     */
    public void quitActionInMainGame(Stage stage){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Close application");
        alert.setHeaderText("You're about to close MyShelfie");
        alert.setContentText("Are you sure you want to quit?");
        ButtonType option1 = new ButtonType("Quit from MyShelfie");
        ButtonType option2 = new ButtonType("Go to Main Menu");
        ButtonType option3 = new ButtonType("Cancel");
        alert.getButtonTypes().setAll(option1, option2, option3);
        ButtonType response = alert.showAndWait().get();
        if(response == option1){
            System.out.println("You're successfully quit");
            stage.close();
            try {
                List<String> notificationList = new ArrayList<>();
                notificationList.add(this.refClient.getNickname());
                setChanged();
                this.notifyDisconnection(notificationList);
            } catch (RemoteException e) {
                System.err.println("Cannot notify quit in quitActionInMainGame: " + e.getMessage());
            }
        } else if(response == option2){
            System.out.println("You're successfully go back to main menu");
            List<String> notificationList = new ArrayList<>();
            try {
                notificationList.add(this.refClient.getNickname());
                notificationList.add("BACK-TO-MAIN-MENU");
                areCardsSet = false;
                this.state = State.SETUP;
                setChanged();
                this.notifyDisconnection(notificationList);
            } catch (RemoteException e) {
                System.err.println("Cannot notify back to Main Menu in quitActionInMainGame: " + e.getMessage());
            }
            changeScene(MAIN_MENU);
        } else if(response == option3){
            System.out.println("Cancel");
        }
    }

    /**
     * Loads all css and fxml files needed for the styling of all the scenes
     */
    private void setup(){
        String css = Objects.requireNonNull(this.getClass().getResource("/css/MainMenu.css")).toExternalForm();
        Font.loadFont(getClass().getResourceAsStream("/fonts/Accio_Dollaro.ttf"), 14);
        Scene loadScene;
        List<String> fxlms = new ArrayList<>(
                Arrays.asList(MAIN_MENU, SETUP_CHOICES, MATCH_CHOICES, MAIN_GAME, OBJECTIVES, PLAYERS_SHELF));
        try{
            for(String fxml : fxlms){
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/" + fxml));
                loadScene = new Scene(fxmlLoader.load());
                loadScene.getStylesheets().add(css);
                scenes.put(fxml, loadScene);
                GUIController ctrl = fxmlLoader.getController();
                ctrl.setGUI(this);
                guiControllers.put(fxml, ctrl);
            }
            currentController = guiControllers.get("MatchChoices.fxml");
            matchChoicesController = (MatchChoicesController) guiControllers.get("MatchChoices.fxml");
            mainGameController = (MainGameController) guiControllers.get("MainGame.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Opens a new stage (separated from the current open scene), only one can be opened at a time
     * @param newPath the title of the stage opened
     */
    public void openNewWindow(String newPath){
        if(newPath.equals(OBJECTIVES)){
            if(objectivesWinIsOpen) return;
            else objectivesWinIsOpen = true;
        }
        if(newPath.equals(PLAYERS_SHELF)){
            if(shelfWinIsOpen) return;
            else shelfWinIsOpen = true;
        }
        Stage newStage = new Stage();
        Scene newWindowScene = scenes.get(newPath);
        newStage.setTitle(newPath.substring(0, newPath.indexOf(".")));
        newStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                "/graphics/Publisher_material/Icon_50x50px.png"))));
        String css = this.getClass().getResource("/css/MainMenu.css").toExternalForm();
        newWindowScene.getStylesheets().add(css);
        newStage.setScene(newWindowScene);
        newStage.setResizable(false);
        newStage.setOnCloseRequest(event -> {
            event.consume();
            newWindowClose(newStage, newPath);
        });
        newStage.show();
    }

    /**
     * Makes it so only one stage can be opened at a time
     * @param stage the stage opened
     * @param path its title String
     */
    private void newWindowClose(Stage stage, String path){
        if(path.equals(OBJECTIVES)) objectivesWinIsOpen = false;
        if(path.equals(PLAYERS_SHELF)) shelfWinIsOpen = false;
        stage.close();
    }

    /**
     * Makes all opened stages center to the screen
     * @param stage the stage opened
     */
    private void centerWindow(Stage stage){
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
    }

    /**
     * Restores a player's Main Match stage to the point where the previous match ended
     * @param gameView to get all the info needed to restore
     */
    private void restoreGUI(GameView gameView){
        try {
            String refClientNickname = this.refClient.getNickname();
            boolean isFirstPlayer = gameView.getPlayers().stream()
                    .filter(p -> p.getNickname().equals(refClientNickname)).toList().get(0).getIsFirstPlayer();
            firstPlayerChairFlag = false;
            Platform.runLater(()-> {
                if (isFirstPlayer && !firstPlayerChairFlag) {
                    mainGameController.activateFirstPlayerChair();
                    firstPlayerChairFlag = true;
                }
            });
        } catch (RemoteException e) {
            System.err.println("Cannot read player's name: " + e.getMessage());
        }
        List<String> players = gameView.getPlayers().stream().map(PlayerView::getNickname).toList();
        PlayerView refClientPlayer = gameView.getPlayers().stream().filter(p -> {
            try {
                return p.getNickname().equals(this.refClient.getNickname());
            } catch (RemoteException e) {
                System.err.println("Cannot read player's name: " + e.getMessage());
            }
            return false;
        }).toList().get(0);
        Platform.runLater(()-> {
            mainGameController.restorePlayerLabels(players,gameView.getCurrentPlayer().getNickname());
            mainGameController.restoreShelf(refClientPlayer.getMyShelf());
        });
        matchChoicesController.setOldMatchFalse();
    }

    /**
     * Updates with the GameBoard changes happening during a turn
     * @param gb the GameBoardView to be updated
     */
    @Override
    public void update(GameBoardView gb) {
        int counter = 0;
        for(int i=0;i<DIM_GAMEBOARD;i++){
            for(int j=0;j<DIM_GAMEBOARD;j++){
                if(gb.getValidGrid()[i][j] == OCCUPIED)
                    counter++;
            }
        }
        isRefilledFlag = counter > prevNumOfItemOnGameBoard;
        prevNumOfItemOnGameBoard = counter;
        Platform.runLater(() -> mainGameController.updateGameboard(gb));
    }

    /**
     * update method
     * @param game the GameView to be updated
     */
    @Override
    public void update(GameView game) {

    }

    /**
     * update method
     * @param gameGrid the game grid to be updated
     */
    @Override
    public void update(Item[][] gameGrid) {

    }

    /**
     * Updates with all the changes made to a player's shelf during a turn
     * @param shelf the player's shelfView to be updated
     */
    @Override
    public void update(ShelfView shelf) {
        Platform.runLater(() -> mainGameController.updateShelf(shelf));
    }

    /**
     * Does different actions depending on the message String received:
     *                - "Enjoy!" : switches to the Main Game scene
     *                - "is playing" : updated all the labels with the current turn's information
     *                - "Common Objective Card" : notifies every player that one of them reached a CommonObjCard goal
     *                - "Your turn is finished!" : notifies a player that the turn is over
     *                - "BYE" : calls a method to make an AlertBox appear with the match's ending stats (including the winner)
     *                - "disconnected": makes all players close the application if one of them disconnects during a match
     *                - "the bag is empty": makes all players close the application if the bag is out of item tiles
     * @param msg the message String received
     */
    @Override
    public void update(String msg) {
        if(matchChoicesController.getOldMatch()){
            state = State.IN_GAME;
            mainGameController = (MainGameController) guiControllers.get("MainGame.fxml");
            Platform.runLater(() -> mainGameController.updateMessageBox("Old unfinished match found!\n" +
                    "The game will resume at that point. ",true));
        }
        if(msg.contains("Enjoy!")){
            state = State.IN_GAME;
            mainGameController = (MainGameController) guiControllers.get("MainGame.fxml");
        }
        switch (state){
            case SETUP -> {
                MatchChoicesController controller = ((MatchChoicesController) guiControllers.get("MatchChoices.fxml"));
                Platform.runLater(() -> controller.displayMsgInfo(msg));
            }
            case IN_GAME -> {
                if(msg.contains("is playing")){
                    String  playerName = msg.substring(0, msg.indexOf(" is playing"));
                    Platform.runLater(() -> mainGameController.updateCurrentTurnLabel(playerName));
                } else {
                    if(msg.contains("%") && msg.contains("$")){
                        int startPlayersName = msg.indexOf("%");
                        int endPlayersName = msg.indexOf("$");
                        String substring = msg.substring(startPlayersName, endPlayersName);
                        int thePlayersPos = msg.indexOf("The players'");
                        String finalMsg1 = msg.substring(0, thePlayersPos) +
                                msg.substring(endPlayersName + 1);
                        Platform.runLater(() -> {
                            mainGameController.updateCurrentTurnLabel(substring);
                            mainGameController.updateMessageBox(finalMsg1, false);
                        });
                    } else if(msg.contains("Common Objective Card")){
                        Platform.runLater(() -> {
                            mainGameController.playSound(PointsGet);
                            mainGameController.updateMessageBox(msg, false);
                            ((ObjectivesController) guiControllers.get(OBJECTIVES))
                                    .updateCommonObjCardsPoints(commonObjCardViewList);
                        });
                    } else if(msg.contains("Your turn is finished!")) {
                        Platform.runLater(() -> mainGameController.updateMessageBox(msg, false));
                    } else if(msg.contains("BYE")) {
                        Platform.runLater(() -> {
                            mainGameController.matchLogInfo(msg, this.stage);
                        });
                    } else if(msg.contains("disconnected")){
                        System.out.println(msg);
                        Platform.runLater(() -> mainGameController.disconnectionAlert(msg, this.stage));
                    } else if (msg.contains("the bag is empty")) {
                        System.out.println(msg);
                        Platform.runLater(() -> mainGameController.emptyBagAlert(msg, this.stage));
                    }
                    else Platform.runLater(() -> mainGameController.updateMessageBox(msg, false));
                }
            }
        }
    }

    /**
     * Sets up all players' Main Game GUI given info on every one of them (who's the first player, etc.), sets up
     * the GameBoard Pane for the first player who has to start a turn
     * @param gameView to get all info needed
     */
    @Override
    public void run(GameView gameView) {
        try {
            String finalNickname = this.refClient.getNickname();
            boolean isFirstPlayer = gameView.getPlayers().stream()
                    .filter(p -> p.getNickname().equals(finalNickname)).toList().get(0).getIsFirstPlayer();
            Platform.runLater(() -> {
                if (gameView.getCurrentPlayer().getNickname().equals(finalNickname)) {
                    if (isFirstPlayer && !firstPlayerChairFlag) {
                        mainGameController.activateFirstPlayerChair();
                        firstPlayerChairFlag = true;
                    }
                    mainGameController.updateMessageBox("", false);
                    mainGameController.activateShelf();
                    mainGameController.switchGameBoardPaneStatus();
                }
            });
        } catch (RemoteException e) {
            System.err.println("Cannot read player's name: " + e.getMessage());
        }
        mainGameController.setCurrentShelf(gameView.getCurrentPlayer().getMyShelf());
        Platform.runLater(() -> {
            String currentTurnPlayerMsg = "It's your turn.\nPlease, choose from 1 to 3 items.";
            mainGameController.playSound(TurnChange);
            mainGameController.updateMessageBox(currentTurnPlayerMsg, true);
            mainGameController.setValidGridForItemSelection(gameView.getGameBoard().getValidGrid());
            mainGameController.updateCurrentTurnLabel(gameView.getCurrentPlayer().getNickname());
            ObjectivesController objCtrl = (ObjectivesController) guiControllers.get("Objectives.fxml");
            this.updateOtherPlayersShelf(gameView);
            if (!areCardsSet) {
                objCtrl.updateComObjCards(gameView.getCommonObjCard());
                objCtrl.updatePersonalObjCard(gameView.getCurrentPlayer().getMyPersonalOBjCard());
                areCardsSet = true;
            }
            objCtrl.updateCommonObjCardsPoints(gameView.getCommonObjCard());
        });
        commonObjCardViewList = gameView.getCommonObjCard();
        this.update(gameView.getGameBoard());
        if(matchChoicesController.getOldMatch()){
            restoreGUI(gameView);
        }
        else this.update(gameView.getCurrentPlayer().getMyShelf());
    }

    /**
     * Manages a player's turn switch and checks if any objective has been reached
     * @param gameView to get all the info needed on turns and objectives
     * @param playerView the player's GUI to be updated
     */
    @Override
    public void displayInfo(GameView gameView, PlayerView playerView) {
        if(matchChoicesController.getOldMatch()){
            restoreGUI(gameView);
        }
        Platform.runLater(() -> {
            mainGameController.playSound(TurnChange);
            mainGameController.updateCurrentTurnLabel(gameView.getCurrentPlayer().getNickname());
            mainGameController.updateScoreLabel(playerView.getScore());
            String msg = "It's " + gameView.getCurrentPlayer().getNickname() + "'s turn.";
            mainGameController.updateMessageBox(msg, false);
        });
        ObjectivesController ctrl = (ObjectivesController) guiControllers.get("Objectives.fxml");
        if(!areCardsSet){
            Platform.runLater(() -> {
                ctrl.updateComObjCards(gameView.getCommonObjCard());
                ctrl.updatePersonalObjCard(playerView.getMyPersonalOBjCard());
            });
            areCardsSet = true;
        }
        Platform.runLater(() -> ctrl.updateCommonObjCardsPoints(gameView.getCommonObjCard()));
        commonObjCardViewList = gameView.getCommonObjCard();
        Platform.runLater(() -> {this.updateOtherPlayersShelf(gameView);});
    }

    /**
     * Sets the current turn's player as the reference client
     * @param client reference client
     */
    @Override
    public void setReferenceClient(Client client) {
        this.refClient = client;
    }

    /**
     * Notifies every player that it's the last turn cycle and takes away the end match point token
     * @param game to get all the info
     * @param playerNickname the nickname String of the player that has filled the shelf
     */
    @Override
    public void gameOverPointTokenHandler(GameView game, String playerNickname) {
        Platform.runLater(() -> {
            mainGameController.playSound(PointsGet);
            mainGameController.updateMessageBox("Player " + playerNickname +
                    " gets a point by\ncompletely filling the shelf!\nThis is the last turn cycle!", false);
            mainGameController.takeFinalPointToken();
        });
    }

    /**
     * Method that adds a listener
     * @param o client to be added as listener
     */
    @Override
    public void addListener(Server o) {
        if (o == null)
            throw new NullPointerException();
        if (!observers.contains(o)) {
            observers.addElement(o);
        }
    }

    /**
     * Sets the "changed" flag to true and notifies listeners with all the choices made by the player
     * @param coords the taken item tiles' coordinates
     * @param column the shelf's column chosen
     */
    public void setChangedAndNotifyListener(List<int[]> coords, Integer column){
        try {
            this.setChanged();
            this.notifyObservers(this.refClient, coords, column);
        } catch (RemoteException e) {
            String msg = "Remote exception occurred: " + e.getMessage();
            System.err.println(msg);
            Platform.runLater(() -> mainGameController.updateMessageBox(msg, true));
        }
    }

    /**
     * Notifies listeners with all the choices made by the player
     * @param o the client making the choices
     * @param arg1 the item's coordinates chosen
     * @param arg2 the shelf's column choice
     * @throws RemoteException if the execution of the update method call goes wrong
     */
    @Override
    public void notifyObservers(Client o, List<int[]> arg1, Integer arg2) throws RemoteException {
        Object[] arrLocal;
        synchronized (this){
            if (!changed)
                return;
            arrLocal = observers.toArray();
            clearChanged();
        }
        for (int i = arrLocal.length-1; i>=0; i--){
            Server vl = (Server) arrLocal[i];
            vl.update(o, arg1, arg2);
        }
    }

    /**
     * Sends a notification update to all other players in case of a player's disconnection
     * @param notificationList a String List with all notifications
     */
    @Override
    public void notifyDisconnection(List<String> notificationList) {
        Object[] arrLocal;
        synchronized (this){
            if (!changed)
                return;
            arrLocal = observers.toArray();
            clearChanged();
        }
        new Thread(() -> {
            for (int i = arrLocal.length-1; i>=0; i--){
                Server vl = (Server) arrLocal[i];
                try {
                    vl.update(notificationList);
                    if(notificationList.size() == 1) System.exit(-5);
                } catch (RemoteException e) {
                    System.err.println("Cannot reached the server in notifyDisconnection: " + e.getMessage());
                }
            }
        }).start();
    }

    /**
     * Updates the "Players' shelf" tab GUI with all the tiles chosen by the other players
     * @param gameView to get all the players' info
     */
    public void updateOtherPlayersShelf(GameView gameView){
        PlayersShelfController playersShelfController =
                (PlayersShelfController) guiControllers.get("PlayersShelf.fxml");
        int numOfPlayers = gameView.getPlayers().size();
        String[] nicknames = new String[numOfPlayers - 1];
        gameView.getPlayers().stream()
                .map(PlayerView::getNickname)
                .filter(name -> {
                    try {
                        System.out.println(this.refClient.getNickname());
                        return !name.equals(this.refClient.getNickname());
                    } catch (RemoteException e) {
                        System.err.println("Cannot obtain refClient name: " + e.getMessage());
                    }
                    return false;
                })
                .toList()
                .toArray(nicknames);
        if(!otherPlayersShelfSetupFlag){
            playersShelfController.setNumOfPlayer(numOfPlayers);
            playersShelfController.initializePlayersShelfMap(nicknames);
        }
        PlayerView thisClientPlayer = gameView.getPlayers().stream()
                .filter(playerView -> {
                    try {
                        return playerView.getNickname().equals(this.refClient.getNickname());
                    } catch (RemoteException e) {
                        System.err.println("Cannot obtain refClient name: " + e.getMessage());
                    }
                    return false;
                })
                .findFirst()
                .get();
        int counter=0;
        for(PlayerView playerView : gameView.getPlayers()){
            if(!playerView.getNickname().equals(thisClientPlayer.getNickname())){
                playersShelfController.updateOtherPlayersShelf(
                        nicknames[counter], playerView.getMyShelf());
                counter++;
            }
        }
    }

    /**
     * Updates the score label in Main Game scene for the final time, sets a player's nickname to a "winner" status and
     * plays a sound according if a player wins or loses
     * @param gameView to get all the players' score info
     */
    public void adjustFinalScore(GameView gameView){
        List<PlayerView> players = gameView.getPlayers();
        int max=0;
        for(int i=0; i<players.size(); i++){
            if(players.get(i).getScore()>max){
                winner = players.get(i).getNickname();
                max = players.get(i).getScore();
            }
        }
        int score = gameView.getPlayers().stream()
                        .filter(playerView -> {
                            try {
                                return playerView.getNickname().equals(this.refClient.getNickname());
                            } catch (RemoteException e) {
                                System.err.println("Cannot obtain refClient nickname: " + e.getMessage());
                            }
                            return false;
                        })
                .map(PlayerView::getScore).toList().get(0);
        Platform.runLater(() -> mainGameController.updateScoreLabel(score));
        Platform.runLater(() -> {
            String refClientNickname = null;
            try {
                refClientNickname = this.refClient.getNickname();
            } catch (RemoteException e) {
                System.err.println("Cannot obtain refClient nickname: " + e.getMessage());
            }
            if (refClientNickname.equals(winner)) {
                mainGameController.playSound(GameWin);
            } else mainGameController.playSound(GameLose);
        });
    }

    /**
     * Invokes a method to display a disconnection alert dialog box
     * @param msg the message String to be displayed
     */
    public void anotherUserDisconnection(String msg){
        Platform.runLater(() -> mainGameController.disconnectionAlert(msg, this.stage));
    }

    /**
     * Sets the "changed" flag to true
     */
    public void setChanged() {
        changed = true;
    }

    /**
     * Sets the "changed" flag to false
     */
    public void clearChanged() {
        changed = false;
    }

    /**
     * Get method for the "isRefilled" flag (true if the gameBoard has just been refilled)
     * @return the "isRefilledFlag" boolean
     */
    public boolean getIsRefilledFlag(){return this.isRefilledFlag;}
}

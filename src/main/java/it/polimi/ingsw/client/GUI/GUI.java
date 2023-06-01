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
import java.util.stream.Collectors;


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
    private List<CommonObjCardView> commonObjCardViewList;
    private String TurnChange = "sounds/TurnChange.wav";
    private static final int DIM_GAMEBOARD = 9;
    private static final int OCCUPIED = 2;

    public void imposeTheTypeOfConnection(String connectionType, String address){
        ((MatchChoicesController) currentController).setConnectionType(connectionType, address);
    }

    public void askNicknameManager(){
        MatchChoicesController ctrl = ((MatchChoicesController) guiControllers.get("MatchChoices.fxml"));
        Platform.runLater(ctrl::wrongNickname);
    }

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
    private boolean changed;
    private final Vector<Server> observers = new Vector<>();

    @Override
    public void start(Stage stage) throws Exception {
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

    public static void main(String[] args){ launch(args);}

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
            // update per finire partita
            stage.close();
        } else if(response == option2){
            System.out.println("You're successfully go back to main menu");
            // update per finire partita
            changeScene(MAIN_MENU);
        } else if(response == option3){
            System.out.println("Cancel");
        }
    }

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
            mainGameController = (MainGameController) guiControllers.get("MainGame.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

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

    private void newWindowClose(Stage stage, String path){
        if(path.equals(OBJECTIVES)) objectivesWinIsOpen = false;
        if(path.equals(PLAYERS_SHELF)) shelfWinIsOpen = false;
        stage.close();
    }

    private void centerWindow(Stage stage){
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
    }


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

    @Override
    public void update(GameView game) {

    }

    @Override
    public void update(Item[][] gameGrid) {

    }

    @Override
    public void update(ShelfView shelf) {
        Platform.runLater(() -> mainGameController.updateShelf(shelf));
    }

    @Override
    public void update(String msg) {
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
                    } else Platform.runLater(() -> mainGameController.updateMessageBox(msg, false));
                }
            }
        }
    }

    @Override
    public void run(GameView gameView) {
        try {
            String finalNickname = this.refClient.getNickname();
            boolean isFirstPlayer = gameView.getPlayers().stream()
                    .filter(p -> p.getNickname().equals(finalNickname)).toList().get(0).getIsFirstPlayer();
            Platform.runLater(() -> {
                if(gameView.getCurrentPlayer().getNickname().equals(finalNickname)){
                    if(isFirstPlayer && !firstPlayerChairFlag) {
                        mainGameController.activateFirstPlayerChair();
                        firstPlayerChairFlag = true;
                    }
                    mainGameController.updateMessageBox("", false);
                    mainGameController.activateShelf(gameView.getCurrentPlayer().getMyShelf());
                    mainGameController.switchGameBoardPaneStatus();
                }
            });
        } catch (RemoteException e) {
            System.err.println("Cannot read player's name: " + e.getMessage());
        }
        Platform.runLater(() -> {
            String currentTurnPlayerMsg = "It's your turn.\nPlease, choose from 1 to 3 items.";
            mainGameController.playSound(TurnChange);
            mainGameController.updateMessageBox(currentTurnPlayerMsg, true);
            mainGameController.setValidGridForItemSelection(gameView.getGameBoard().getValidGrid());
            mainGameController.updateCurrentTurnLabel(gameView.getCurrentPlayer().getNickname());
            ObjectivesController objCtrl = (ObjectivesController) guiControllers.get("Objectives.fxml");
            this.updateOtherPlayersShelf(gameView);
            if(!areCardsSet){
                objCtrl.updateComObjCards(gameView.getCommonObjCard());
                objCtrl.updatePersonalObjCard(gameView.getCurrentPlayer().getMyPersonalOBjCard());
                areCardsSet = true;
            }
            objCtrl.updateCommonObjCardsPoints(gameView.getCommonObjCard());
        });
        commonObjCardViewList = gameView.getCommonObjCard();
        this.update(gameView.getGameBoard());
        this.update(gameView.getCurrentPlayer().getMyShelf());
    }

    @Override
    public void displayInfo(GameView gameView, PlayerView playerView) {
        Platform.runLater(() -> {
            mainGameController.playSound(TurnChange);
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

    @Override
    public void setReferenceClient(Client client) {
        this.refClient = client;
    }

    @Override
    public void addListener(Server o) {
        if (o == null)
            throw new NullPointerException();
        if (!observers.contains(o)) {
            observers.addElement(o);
        }
    }

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
        /*playersShelfController.setNumOfPlayer(numOfPlayers);
        playersShelfController.initializePlayersShelfMap(nicknames);*/
        int counter=0;
        for(PlayerView playerView : gameView.getPlayers()){
            if(!playerView.getNickname().equals(thisClientPlayer.getNickname())){
                playersShelfController.updateOtherPlayersShelf(
                        nicknames[counter], playerView.getMyShelf());
                counter++;
            }
        }
    }

    public void adjustFinalScore(GameView gameView){
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
    }

    public void setChanged() {
        changed = true;
    }

    public void clearChanged() {
        changed = false;
    }

    public boolean getIsRefilledFlag(){return this.isRefilledFlag;}
}

package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.GUI.controllers.*;
import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.distributed.Server;
import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.modelview.GameBoardView;
import it.polimi.ingsw.modelview.GameView;
import it.polimi.ingsw.modelview.PlayerView;
import it.polimi.ingsw.modelview.ShelfView;
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


public class GUI extends Application implements UI {
    private static final String MAIN_MENU = "MainMenu.fxml";
    private static final String SETUP_CHOICES = "ChoicesMenu.fxml";
    private static final String MATCH_CHOICES = "MatchChoices.fxml";
    private static final String MAIN_GAME = "MainGame.fxml";
    private static final String OBJECTIVES = "Objectives.fxml";
    private final HashMap<String, Scene> scenes = new HashMap<>();
    private final HashMap<String, GUIController> guiControllers = new HashMap<String, GUIController>();
    private Stage stage;
    private GUIController currentController;
    private MainGameController mainGameController;
    private final String css = this.getClass().getResource("/css/MainMenu.css").toExternalForm();
    private boolean objectivesWinIsOpen = false;
    private Client refClient;
    private enum State {SETUP, IN_GAME}
    private State state = State.SETUP;
    private boolean areCardsSet;
    private boolean firstPlayerChairFlag;

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

    private void setup(){
        String css = Objects.requireNonNull(this.getClass().getResource("/css/MainMenu.css")).toExternalForm();
        Font.loadFont(getClass().getResourceAsStream("/fonts/Accio_Dollaro.ttf"), 14);
        Scene loadScene;
        List<String> fxlms = new ArrayList<>(
                Arrays.asList(MAIN_MENU, SETUP_CHOICES, MATCH_CHOICES, MAIN_GAME, OBJECTIVES));
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void openNewWindow(String newPath){
        if(newPath.equals(OBJECTIVES)){
            if(objectivesWinIsOpen) return;
            else objectivesWinIsOpen = true;
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
            newWindowClose(newStage);
        });
        newStage.show();
    }

    private void newWindowClose(Stage stage){
        objectivesWinIsOpen = false;
        stage.close();
    }

    private void centerWindow(Stage stage){
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
    }


    @Override
    public void update(GameBoardView gb) {
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
            //currentController = guiControllers.get("MainGame.fxml");
            mainGameController = (MainGameController) guiControllers.get("MainGame.fxml");
        }
        String finalMsg = msg;
        switch (state){
            case SETUP -> {
                //Platform.runLater(() -> currentController.executeRequest(msg));
                //currentController.executeRequest(msg);
                MatchChoicesController controller = ((MatchChoicesController) guiControllers.get("MatchChoices.fxml"));
                Platform.runLater(() -> controller.displayMsgInfo(finalMsg));
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
                            mainGameController.updateMessageBox(finalMsg1);
                        });
                    } else Platform.runLater(() -> mainGameController.updateMessageBox(finalMsg));
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
                    mainGameController.updateMessageBox("");
                    mainGameController.activateShelf(gameView.getCurrentPlayer().getMyShelf());
                }
            });
        } catch (RemoteException e) {
            System.err.println("Cannot read player's name: " + e.getMessage());
        }
        Platform.runLater(() -> {
            mainGameController.updateCurrentTurnLabel(gameView.getCurrentPlayer().getNickname());
            if(!areCardsSet){
                ObjectivesController ctrl = (ObjectivesController) guiControllers.get("Objectives.fxml");                    ctrl.updateComObjCards(gameView.getCommonObjCard());
                    ctrl.updatePersonalObjCard(gameView.getCurrentPlayer().getMyPersonalOBjCard());
                areCardsSet = true;
            }
        });
        this.update(gameView.getGameBoard());
        this.update(gameView.getCurrentPlayer().getMyShelf());
    }

    @Override
    public void displayInfo(GameView gameView, PlayerView playerView) {
        Platform.runLater(() -> mainGameController.updateScoreLabel(playerView.getScore()));
        if(!areCardsSet){
            ObjectivesController ctrl = (ObjectivesController) guiControllers.get("Objectives.fxml");
            Platform.runLater(() -> {
                ctrl.updateComObjCards(gameView.getCommonObjCard());
                ctrl.updatePersonalObjCard(playerView.getMyPersonalOBjCard());
            });
            areCardsSet = true;
        }
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
        this.setChanged();
        try {
            this.notifyObservers(this.refClient, coords, column);
        } catch (RemoteException e) {
            String msg = "Remote exception occurred: " + e.getMessage();
            System.err.println(msg);
            Platform.runLater(() -> mainGameController.updateMessageBox(msg));
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
    public void setChanged() {
        changed = true;
    }

    public void clearChanged() {
        changed = false;
    }
}

package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.App;
import it.polimi.ingsw.client.GUI.controllers.GUIController;
import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.distributed.Server;
import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.modelview.GameBoardView;
import it.polimi.ingsw.modelview.GameView;
import it.polimi.ingsw.modelview.PlayerView;
import it.polimi.ingsw.modelview.ShelfView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.*;


public class GUI extends Application implements UI {
    private static final String MAIN_MENU = "MainMenu.fxml";
    private static final String SETUP_CHOICES = "ChoicesMenu.fxml";
    private static final String MATCH_CHOICES = "MatchChoices.fxml";
    private final HashMap<String, Scene> scenes = new HashMap<>();
    private final HashMap<String, GUIController> guiControllers = new HashMap<String, GUIController>();
    private Stage stage;
    private final String css = this.getClass().getResource("/css/MainMenu.css").toExternalForm();

    public void changeScene(String nextScene){
        Scene currentScene = scenes.get(nextScene);
        currentScene.getStylesheets().add(css);
        stage.setScene(currentScene);
        stage.show();
    }
    private boolean changed;
    private final Vector<Server> observers = new Vector<>();

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        setup();
        try {
            Scene scene = scenes.get("MainMenu.fxml");
            /*Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainMenu.fxml"));
            Scene scene = new Scene(root);*/
            Font.loadFont(getClass().getResourceAsStream("/fonts/Accio_Dollaro.ttf"), 14);
            stage.setTitle("MyShelfie");
            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                    "/graphics/Publisher material/Icon 50x50px.png"))));
            stage.setResizable(false);
            stage.setScene(scene);

            String css = this.getClass().getResource("/css/MainMenu.css").toExternalForm();
            scene.getStylesheets().add(css);

            stage.setOnCloseRequest(event -> {
                event.consume();
                quitButtonAction(stage);
            });

            stage.show();
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
        List<String> fxlms = new ArrayList<>(Arrays.asList(MAIN_MENU, SETUP_CHOICES, MATCH_CHOICES));
        try{
            for(String fxml : fxlms){
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/" + fxml));
                scenes.put(fxml, new Scene(fxmlLoader.load()));
                GUIController ctrl = fxmlLoader.getController();
                ctrl.setGUI(this);
                System.out.println();
                guiControllers.put(fxml, ctrl);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void update(GameBoardView gb) {

    }

    @Override
    public void update(GameView game) {

    }

    @Override
    public void update(Item[][] gameGrid) {

    }

    @Override
    public void update(ShelfView shelf) {

    }

    @Override
    public void update(String msg) {

    }

    @Override
    public void run(GameView gameView) {

    }

    @Override
    public void displayInfo(GameView gameView, PlayerView playerView) {

    }

    @Override
    public void setReferenceClient(Client client) {

    }

    @Override
    public void addListener(Server o) {
        if (o == null)
            throw new NullPointerException();
        if (!observers.contains(o)) {
            observers.addElement(o);
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

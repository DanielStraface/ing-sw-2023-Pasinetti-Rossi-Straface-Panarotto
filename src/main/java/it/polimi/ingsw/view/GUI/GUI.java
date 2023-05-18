package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.modelview.GameBoardView;
import it.polimi.ingsw.modelview.GameView;
import it.polimi.ingsw.modelview.PlayerView;
import it.polimi.ingsw.modelview.ShelfView;
import it.polimi.ingsw.view.UI;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.util.Objects;


public class GUI extends UI {

    public static class MainFrame extends Application{

        @Override
        public void start(Stage stage) throws Exception {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainMenu.fxml"));
                Scene scene = new Scene(root);
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
}

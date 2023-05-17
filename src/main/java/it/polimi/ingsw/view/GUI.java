package it.polimi.ingsw.view;

import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.modelview.GameBoardView;
import it.polimi.ingsw.modelview.GameView;
import it.polimi.ingsw.modelview.PlayerView;
import it.polimi.ingsw.modelview.ShelfView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class GUI extends UI {

    public static class MainFrame extends Application{

        @Override
        public void start(Stage stage) throws Exception {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/Prova.fxml"));
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static void main(String[] args){ launch(args);}
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

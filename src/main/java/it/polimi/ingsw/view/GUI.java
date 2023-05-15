package it.polimi.ingsw.view;

import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.modelview.GameBoardView;
import it.polimi.ingsw.modelview.GameView;
import it.polimi.ingsw.modelview.PlayerView;
import it.polimi.ingsw.modelview.ShelfView;

import javax.swing.*;
import java.awt.event.ActionListener;


public class GUI extends UI {

    public GUI(){
        frame.setVisible(true);
    }

    private static class MainFrame extends JFrame {
        public MainFrame(){
            super("MyShelfie");
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setSize(1280, 720);
        }
    }

    private final MainFrame frame = new MainFrame();

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

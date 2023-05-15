package it.polimi.ingsw.view;

import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.modelview.GameBoardView;
import it.polimi.ingsw.modelview.GameView;
import it.polimi.ingsw.modelview.PlayerView;
import it.polimi.ingsw.modelview.ShelfView;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class GUI extends UI {

    private static BufferedImage shelfImg;

    static {
        try {
            shelfImg = ImageIO.read(new File("src/main/resources/graphics/item tiles/Trofei1.1.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public GUI(){
        /* Gameboard section */
        JPanel gameBoardZone = new JPanel();
        gameBoardZone.setBounds(0, 0, 705, 480);
        gameBoardZone.setBorder(new LineBorder(new Color(0, 0, 0)));
        gameBoardZone.setBackground(new Color(213, 225, 144));
        gameBoardZone.setOpaque(true);
        /* Shelf section */
        JPanel shelfZone = new JPanel();
        shelfZone.setBounds(706, 0, 384, 480); // 790
        shelfZone.setBorder(new LineBorder(new Color(0, 0, 0)));
        shelfZone.setBackground(new Color(213, 225, 144));
        shelfZone.setOpaque(true);
        /* Info-box section */
        JPanel infoBox = new JPanel();
        infoBox.setBounds(791, 0, 489, 240);
        infoBox.setBorder(new LineBorder(new Color(0, 0, 0)));
        infoBox.setLayout(new BoxLayout(infoBox, BoxLayout.Y_AXIS));
        JLabel yourName = new JLabel("Your Name");
        JLabel yourScore = new JLabel("Your score");
        JLabel currentPlayer = new JLabel("Current Turn Player");
        infoBox.add(yourName);
        infoBox.add(yourScore);
        infoBox.add(currentPlayer);
        infoBox.setBackground(new Color(213, 225, 144));
        infoBox.setOpaque(true);
        /* Message box */
        JPanel messageBox = new JPanel();
        messageBox.setBounds(791, 241, 489, 239);
        messageBox.setBorder(new LineBorder(new Color(0, 0, 0)));
        messageBox.setBackground(new Color(213, 225, 144));
        messageBox.setOpaque(true);
        /* Common ObjCards section */
        JPanel comObjCardsZone = new JPanel();
        comObjCardsZone.setBounds(0, 481, 705, 239);
        comObjCardsZone.setBorder(new LineBorder(new Color(0, 0, 0)));
        comObjCardsZone.setBackground(new Color(213, 225, 144));
        comObjCardsZone.setOpaque(true);
        /* Personal ObjCard section */
        JPanel perObjCardZone = new JPanel();
        perObjCardZone.setBounds(706, 481, 384, 239);
        perObjCardZone.setBorder(new LineBorder(new Color(0, 0, 0)));
        perObjCardZone.setBackground(new Color(213, 225, 144));
        perObjCardZone.setOpaque(true);
        frame.getContentPane().add(gameBoardZone);
        frame.getContentPane().add(shelfZone);
        frame.getContentPane().add(infoBox);
        frame.getContentPane().add(messageBox);
        frame.getContentPane().add(comObjCardsZone);
        frame.getContentPane().add(perObjCardZone);
        frame.setVisible(true);
    }

    private static class MainFrame extends JFrame {
        public MainFrame(){
            super("MyShelfie");
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setSize(1280, 720);
            setLayout(null);
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

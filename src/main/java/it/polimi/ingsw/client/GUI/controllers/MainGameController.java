package it.polimi.ingsw.client.GUI.controllers;

import it.polimi.ingsw.client.CLI.commands.Command;
import it.polimi.ingsw.client.CLI.commands.SelectColumnCommand;
import it.polimi.ingsw.client.CLI.commands.SelectItemsCommand;
import it.polimi.ingsw.client.GUI.GUI;
import it.polimi.ingsw.exceptions.FullColumnException;
import it.polimi.ingsw.exceptions.InvalidSelectionException;
import it.polimi.ingsw.model.Category;
import it.polimi.ingsw.modelview.GameBoardView;
import it.polimi.ingsw.modelview.ShelfView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class MainGameController implements GUIController {
    private static final int OCCUPIED = 2;
    private static final int PLAYABLE = 1;
    @FXML
    private AnchorPane gameboardPane;
    @FXML
    private Button objectivesButton;
    @FXML
    private Label messageBox;
    @FXML
    private Label scoreLabel;
    @FXML
    private Label player1Label;
    @FXML
    private Label player2Label;
    @FXML
    private Label player3Label;
    @FXML
    private Label player4Label;
    @FXML
    private Label yourNameLabel;
    @FXML
    private ImageView firstPlayerChair;
    @FXML
    private ImageView activeShelf;
    @FXML
    private Pane shelfItemPane;
    @FXML
    private Button confirmSelection;
    @FXML
    private List<Label> ordinalSelectionFlag;
    @FXML
    private ImageView openedBag;
    @FXML
    private ImageView closedBag;
    @FXML
    private List<ImageView> imgsList;
    @FXML
    private List<BorderPane> wrappersList;
    private final Map<String, BorderPane> wrapperMap = new HashMap<>();
    @FXML
    private List<Button> itemsList;
    @FXML
    private List<ImageView> shelfColumnOne;
    @FXML
    private List<ImageView> shelfColumnTwo;
    @FXML
    private List<ImageView> shelfColumnThree;
    @FXML
    private List<ImageView> shelfColumnFour;
    @FXML
    private List<ImageView> shelfColumnFive;
    private List<List<ImageView>> shelfColumns = new ArrayList<>();
    @FXML
    private List<Button> shelfColumnButtonList;
    private int numOfItems = 0;
    private BorderPane[][] gameboardItemSlotMatrix = new BorderPane[DIM_GAMEBOARD][DIM_GAMEBOARD];
    private boolean[][] gameboardItemMatrix = new boolean[DIM_GAMEBOARD][DIM_GAMEBOARD];
    private List<int[]> selectedCoords = new ArrayList<>();
    private List<Integer> columnReference = new ArrayList<>();
    private List<Command> commands = Arrays.asList(new SelectItemsCommand(this.selectedCoords),
            new SelectColumnCommand(this.columnReference));
    private GUI gui;
    private final List<Label> activeLabels = new ArrayList<>();
    private List<String> itemImgPath = new ArrayList<>();
    private List<BorderPane> borderPanes = new ArrayList<>();
    private List<BorderPane> ordinalBorderPanes = new ArrayList<>();
    private int prevColSelected;
    private boolean confirmButtonFlag = false;
    private String warning = "sounds/Warning.mp3";
    private String MenuSelection = "sounds/MenuSelection.mp3";
    private String ItemSelect = "sounds/ItemSelect.mp3";
    private String GameBoardRefill = "sounds/GBRefill.wav";
    private MediaPlayer mediaPlayer;
    private static final int SHELF_ROWS = 6;
    private static final int SHELF_COLS = 5;
    private static final int DIM_GAMEBOARD = 9;

    public void initialize(){
        int counter = 0;
        for(BorderPane bp : wrappersList){
            wrapperMap.put(itemsList.get(counter).toString(), bp);
            counter++;
        }
        shelfColumns.add(shelfColumnOne);
        shelfColumns.add(shelfColumnTwo);
        shelfColumns.add(shelfColumnThree);
        shelfColumns.add(shelfColumnFour);
        shelfColumns.add(shelfColumnFive);
        for(int i=0;i<itemsList.size();i++){
            String[] stringArray = itemsList.get(i).getId().split("_");
            int row = Integer.parseInt(stringArray[1]);
            int col = Integer.parseInt(stringArray[2]);
            if(wrappersList.get(i) != null) {
                gameboardItemSlotMatrix[row][col] = wrappersList.get(i);
            }
        }
    }

    public void objectivesButtonAction(ActionEvent event){
        System.out.println("Objectives button pressed");
        playSound(MenuSelection);
        String scenePath = "Objectives.fxml";
        this.gui.openNewWindow(scenePath);
    }

    public void updateMessageBox(String msg, boolean Exception) {
        messageBox.setText(msg);
        if (Exception) {
            messageBox.setBackground(Background.fill(Color.LIGHTSALMON));
            messageBox.setTextFill(Color.RED);
            messageBox.setStyle("-fx-font-size: 25px");
        }
        else {
            messageBox.setBackground(Background.fill(Color.TRANSPARENT));
            messageBox.setTextFill(Color.BLACK);
            messageBox.setStyle("-fx-font-size: 25px");

            }
    }
    public void updateScoreLabel(int score){
        scoreLabel.setText(Integer.toString(score));
    }
    public void updateGameboard(GameBoardView gameBoardView){
        SelectItemsCommand sic = (SelectItemsCommand) this.commands.get(0);
        sic.setGameBoardView(gameBoardView);
        refillImgGameBoard(gameBoardView);
    }
    public void updateShelf(ShelfView shelfView){
        if(this.itemImgPath.size() == 0) return;
        int firstNullPosition = SHELF_ROWS - 1;
        for(int i=SHELF_ROWS - 1;i>=0;i--){
            if(shelfView.getShelfGrid()[i][this.prevColSelected - 1].getCategoryType() != null &&
                    shelfColumns.get(this.prevColSelected - 1).get(i).getImage() == null){
                firstNullPosition = i;
                break;
            }
        }
        for(String imgPath : this.itemImgPath){
            shelfColumns.get(this.prevColSelected - 1).get(firstNullPosition).setImage(new Image(imgPath));
            firstNullPosition--;
        }
        itemImgPath.clear();
    }
    public void updateCurrentTurnLabel(String msg){
        if(player1Label.getText().equals("%DEFAULT%")){
            StringBuilder temp = new StringBuilder();
            temp.append(msg, 1, msg.length());
            player1Label.setVisible(true);
            Label[] playerNameLabels = {player1Label, player2Label, player3Label, player4Label};
            int counter = 0;
            while(temp.toString().contains("!")){
                playerNameLabels[counter].setVisible(true);
                playerNameLabels[counter].setText(temp.substring(0, temp.indexOf("!")));
                temp.delete(0, temp.indexOf("!") + 1);
                activeLabels.add(playerNameLabels[counter]);
                counter++;
            }
        }
        for(Label label : activeLabels){
            if(label.getText().equals(msg)){
                label.setTextFill(Color.RED);
                label.setStyle("-fx-font-size: 25px");
            } else {
                label.setTextFill(Color.BLACK);
                label.setStyle("-fx-font-size: 20px");
            }
        }
    }
    public void updateYourNameLabel(String name){
        yourNameLabel.setVisible(true);
        yourNameLabel.setText(name);
    }

    private void refillImgGameBoard(GameBoardView gameBoardView){
        Random random = new Random();
        Category category;
        int[][] validGrid = gameBoardView.getValidGrid();
        if(this.gui.getIsRefilledFlag()){
            playSound(GameBoardRefill);
            closedBag.setVisible(false);
            openedBag.setVisible(true);
            for(int i=0;i<DIM_GAMEBOARD;i++){
                for(int j=0;j<DIM_GAMEBOARD;j++){
                    if(validGrid[i][j] == OCCUPIED && gameboardItemSlotMatrix[i][j] != null){
                        int variant = random.nextInt(3) + 1;
                        ImageView imageView = ((ImageView) gameboardItemSlotMatrix[i][j].getCenter());
                        category = gameBoardView.getGameGrid()[i][j].getCategoryType();
                        String partialPath = "/graphics/item_tiles/";
                        switch (category){
                            case CAT -> imageView.setImage(new Image(partialPath + "Gatti1." + variant + ".png"));
                            case BOOK -> imageView.setImage(new Image(partialPath + "Libri1." + variant + ".png"));
                            case GAME -> imageView.setImage(new Image(partialPath + "Giochi1." + variant + ".png"));
                            case FRAME -> imageView.setImage(new Image(partialPath + "Cornici1." + variant + ".png"));
                            case TROPHY -> imageView.setImage(new Image(partialPath + "Trofei1." + variant + ".png"));
                            case PLANT -> imageView.setImage(new Image(partialPath + "Piante1." + variant + ".png"));
                        }
                    }
                }
            }
            new Thread(() -> {
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    System.err.println("Cannot sleep in refillImgGameBoard: " + e.getMessage());
                }
                openedBag.setVisible(false);
                closedBag.setVisible(true);
            }).start();
        } else {
            for(int i=0;i<DIM_GAMEBOARD;i++)
                for(int j=0;j<DIM_GAMEBOARD;j++)
                    if(validGrid[i][j] == PLAYABLE && gameboardItemSlotMatrix[i][j] != null)
                        ((ImageView) gameboardItemSlotMatrix[i][j].getCenter()).setImage(null);
        }
    }

    public void itemClick(ActionEvent event){
        this.messageBox.setText("");
        System.out.println("Item click");
        playSound(ItemSelect);
        System.out.println(event.getSource().toString());
        String temp = event.getSource().toString();
        String[] stringArray = temp.split("_");
        int row = Integer.parseInt(stringArray[1]);
        int col = Integer.parseInt(stringArray[2]);
        int[] coords = {row, col};
        if(!gameboardItemMatrix[row][col]){
            if(numOfItems >= 3) return;
            //da aggiungere
            gameboardItemMatrix[row][col] = true;
            numOfItems++;
            selectedCoords.add(coords);
            BorderPane bp = wrapperMap.get(event.getSource().toString());
            bp.getStyleClass().add("image-view-wrapper");
            ordinalBorderPanes.add(bp);
            double x = bp.localToScene(bp.getBoundsInLocal()).getMaxX();
            double y = bp.localToScene(bp.getBoundsInLocal()).getMinY();
            Label toDisplay = ordinalSelectionFlag.get(numOfItems - 1);
            toDisplay.relocate(x - 10, y);
            toDisplay.setVisible(true);
            if(numOfItems == 1) {
                confirmSelection.setOpacity(1);
                confirmSelection.setDisable(false);
            }
        } else {
            if(numOfItems >= 0){
                messageBox.setBackground(Background.fill(Color.TRANSPARENT));
                int index = selectedCoords.indexOf(selectedCoords.stream()
                        .filter(i -> i[0] == row && i[1] == col).findFirst().get());
                selectedCoords.remove(index);
                gameboardItemMatrix[row][col] = false;
                BorderPane bp = wrapperMap.get(event.getSource().toString());
                bp.getStyleClass().clear();
                ordinalBorderPanes.remove(bp);
                Label toDisplay = ordinalSelectionFlag.get(index);
                toDisplay.setVisible(false);
                toDisplay.relocate(1, 1);
                numOfItems--;
                Map<Integer, List<Double>> labelCoords = new HashMap<>();
                int counter = 0;
                for(Label l : ordinalSelectionFlag){
                    if(l.getLayoutX() != 1 && l.getLayoutY() != 1){
                        List<Double> labelXandY = Arrays.asList(l.getLayoutX(), l.getLayoutY());
                        labelCoords.put(counter, labelXandY);
                        counter++;
                    }
                    l.setVisible(false);
                    l.relocate(1, 1);
                }
               for(int i=0;i<labelCoords.size();i++){
                   System.out.println(ordinalSelectionFlag.get(i).getText());
                   ordinalSelectionFlag.get(i).relocate(labelCoords.get(i).get(0), labelCoords.get(i).get(1));
                   ordinalSelectionFlag.get(i).setVisible(true);
               }
                if(numOfItems == 0) {
                    confirmSelection.setOpacity(0.6);
                    confirmSelection.setDisable(true);
                }
            }
        }
    }

    public void confirmSelectionAction(ActionEvent event){
        confirmButtonFlag = true;
        try{
            this.commands.get(0).check();
            switchGameBoardPaneStatus();
            for(Button b : shelfColumnButtonList){
                b.setVisible(true);
                b.setDisable(false);
            }
            confirmSelection.setOpacity(0.6);
            ordinalSelectionFlag.forEach(osf -> {
                osf.relocate(1, 1);
                osf.setVisible(false);
            });
            /*List<BorderPane> bps = wrappersList.stream()
                    .filter(borderPane -> borderPane.getStyleClass().size() == 1).toList();
            this.borderPanes.addAll(bps);*/
            this.itemImgPath.addAll(ordinalBorderPanes.stream()
                    .map(borderPane -> ((ImageView) borderPane.getCenter()).getImage().getUrl()).toList());
            ordinalBorderPanes.forEach(borderPane -> borderPane.getStyleClass().clear());
            numOfItems = 0;
        } catch (InvalidSelectionException e) {
            playSound(warning);
            this.updateMessageBox("Invalid items selection:\n" + e.getMessage(), true);
        } catch (FullColumnException ignored) {
        }
    }

    public void shelfColumnButtonAction(ActionEvent event){
        String[] cols = {"1", "2", "3", "4", "5"};
        String eventString = event.getSource().toString();
        for(String col : cols)
            if(eventString.contains(col))
                this.columnReference.add(Integer.parseInt(col));
        try{
            this.commands.get(1).check();
            for(Button b : shelfColumnButtonList){
                b.setDisable(true);
                b.setVisible(false);
            }
            ordinalBorderPanes.forEach(borderPane -> ((ImageView) borderPane.getCenter()).setImage(null));
            System.out.println("BEFORE NOTIFY, := " + this.columnReference.get(0));
            this.activeShelf.setOpacity(0.7);
            this.shelfItemPane.setOpacity(0.7);
            this.prevColSelected = this.columnReference.get(0);
            System.out.println("*****\n" + this.selectedCoords.get(0)[0] + ", " + this.selectedCoords.get(0)[1]);
            List<int[]> toSend = new ArrayList<>();
            for(int[] coords : selectedCoords){
                toSend.add(new int[]{coords[0], coords[1]});
            }
            new Thread(() ->
                    this.gui.setChangedAndNotifyListener(toSend, this.prevColSelected - 1)).start();
            ordinalBorderPanes.clear();
            this.selectedCoords.clear();
            this.columnReference.clear();
        } catch (InvalidSelectionException ignored) {
        } catch (FullColumnException e) {
            playSound(warning);
            this.columnReference.clear();
            this.updateMessageBox("Wrong column selection: " + e.getMessage(), true);
        }

    }

    public void playersShelfButtonAction(){
        System.out.println("Players' Shelf button pressed");
        playSound(MenuSelection);
        String scenePath = "PlayersShelf.fxml";
        this.gui.openNewWindow(scenePath);
    }

    public void activateShelf(ShelfView shelfView){
        this.activeShelf.setOpacity(1);
        this.shelfItemPane.setOpacity(1);
        ((SelectColumnCommand) this.commands.get(1)).setShelfView(shelfView);
    }

    public void activateFirstPlayerChair(){
        this.firstPlayerChair.setVisible(true);
    }
    public void switchGameBoardPaneStatus(){
        if(this.gameboardPane.isDisable()) this.gameboardPane.setDisable(false);
        else if(confirmButtonFlag){
            this.gameboardPane.setDisable(true);
            confirmButtonFlag = false;
        }
    }

    @Override
    public void setGUI(GUI gui) {
        this.gui = gui;
    }

    @Override
    public void playSound(String filePath) {
        Media pick = new Media(Objects.requireNonNull(getClass().getClassLoader()
                .getResource(filePath)).toExternalForm());
        mediaPlayer = new MediaPlayer(pick);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setVolume(25);
        mediaPlayer.setOnEndOfMedia(() -> {
            mediaPlayer.seek(Duration.ZERO);
            mediaPlayer.stop();
        });
    }
}

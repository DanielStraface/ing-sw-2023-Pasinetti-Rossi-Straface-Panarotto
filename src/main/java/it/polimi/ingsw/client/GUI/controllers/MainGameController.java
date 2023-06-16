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
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class MainGameController implements GUIController {
    private static final int OCCUPIED = 2;
    private static final int PLAYABLE = 1;
    private static final int INVALID = 0;
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
    @FXML
    private ImageView finalPointToken;
    private int numOfItems = 0;
    private BorderPane[][] gameboardItemSlotMatrix = new BorderPane[DIM_GAMEBOARD][DIM_GAMEBOARD];
    private boolean[][] gameboardItemMatrix;
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
    private int[][] validGridForItemSelection;
    private String warning = "sounds/Warning.wav";
    private String MenuSelection = "sounds/MenuSelection.wav";
    private String ItemSelect = "sounds/ItemSelect.wav";
    private String GameBoardRefill = "sounds/GBRefill.wav";
    private MediaPlayer mediaPlayer;
    private static final int SHELF_ROWS = 6;
    private static final int SHELF_COLS = 5;
    private static final int DIM_GAMEBOARD = 9;

    /**
     * initialization method
     */
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
        resetGameBoardItemMatrix();
    }

    /**
     * objectivesButtonAction handles the action event due to the press of the "Objectives" button. It reproduces the specific sound
     * and opens a new window using the scene path.
     * @param event, the action event generated by clicking the "Objectives" button
     */
    public void objectivesButtonAction(ActionEvent event){
        System.out.println("Objectives button pressed");
        playSound(MenuSelection);
        String scenePath = "Objectives.fxml";
        this.gui.openNewWindow(scenePath);
    }

    /**
     * updateMessageBox manages the viewing of messages during the game. Messages appear written normally if they are
     * information messages for the player, with a red box if they are error messages.
     * @param msg messages during the game based on players' action
     * @param Exception, true if msg is a message of error
     */
    public void updateMessageBox(String msg, boolean Exception) {
        messageBox.setText(msg);
        if (Exception) {
            messageBox.setBackground(new Background(new BackgroundFill(
                    Color.LIGHTSALMON, CornerRadii.EMPTY, Insets.EMPTY)));
            messageBox.setTextFill(Color.RED);
            messageBox.setStyle("-fx-font-size: 25px");
        }
        else {
            messageBox.setBackground(new Background(new BackgroundFill(
                    Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
            messageBox.setTextFill(Color.BLACK);
            messageBox.setStyle("-fx-font-size: 25px");

            }
    }

    /**
     * update the score label with the score of the player.
     * @param score player's score
     */
    public void updateScoreLabel(int score){
        scoreLabel.setText(Integer.toString(score));
    }

    /**
     * update method passing a GameBoardView, sets the GameBoardView, refills the image of the game board.
     * @param gameBoardView gameBoardView
     */
    public void updateGameboard(GameBoardView gameBoardView){
        SelectItemsCommand sic = (SelectItemsCommand) this.commands.get(0);
        sic.setGameBoardView(gameBoardView);
        refillImgGameBoard(gameBoardView);
    }

    /**
     * update method passing a ShelfView
      * @param shelfView ShelfView
     */
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

    /**
     * Graphically restores a player's shelf with all the previous match's info
     * @param shelfView to get the Shelf grid's info
     */
    public void restoreShelf(ShelfView shelfView){
        for(int i=0; i<SHELF_COLS; i++){
            for(int j=0; j<SHELF_ROWS; j++){
                Category category = shelfView.getShelfGrid()[j][i].getCategoryType();
                int variant =  shelfView.getShelfGrid()[j][i].getVariant();
                String partialPath = "/graphics/item_tiles/";
                ImageView imageView = shelfColumns.get(i).get(j);
                if(category == null){
                    imageView.setImage(null);
                }
                else switch(category){
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



    /**
     * update method passing String. It shows the names of the players who are playing. The name of the current player
     * turns red, the others are black.
     * @param msg  name of the player
     */
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

    /**
     * Restores all the players' name indicator and highlights the current player in red
     * @param players a list of players' nicknames
     * @param firstPlayer the first player's nickname
     */
    public void restorePlayerLabels(List<String> players, String firstPlayer){
        activeLabels.clear();
        Label[] playerNameLabels = {player1Label, player2Label, player3Label, player4Label};
        int i=0;
        for(String s: players){
            playerNameLabels[i].setVisible(true);
            playerNameLabels[i].setText(s);
            if(s.equals(firstPlayer)){
                playerNameLabels[i].setTextFill(Color.RED);
                playerNameLabels[i].setStyle("-fx-font-size: 25px");
            } else{
                playerNameLabels[i].setTextFill(Color.BLACK);
                playerNameLabels[i].setStyle("-fx-font-size: 20px");
            }
            activeLabels.add(playerNameLabels[i]);
            i++;
        }
    }

    /**
     * update method passing String. Makes the player's name visible.
     * @param name the name of the player
     */
    public void updateYourNameLabel(String name){
        yourNameLabel.setVisible(true);
        yourNameLabel.setText(name);
    }

    /**
     * shows the image of the opened bag, refills the images on the game board with a specific sound,
     * shows the image of the closed bag.
     * @param gameBoardView GameBoardView
     */
    private void refillImgGameBoard(GameBoardView gameBoardView){
        resetGameBoardItemMatrix();
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
                        ImageView imageView = ((ImageView) gameboardItemSlotMatrix[i][j].getCenter());
                        category = gameBoardView.getGameGrid()[i][j].getCategoryType();
                        int variant = gameBoardView.getGameGrid()[i][j].getVariant();
                        String partialPath = "/graphics/item_tiles/";
                        switch (category){
                            case CAT -> imageView.setImage(new Image(partialPath + "Gatti1." + variant + ".png"));
                            case BOOK -> imageView.setImage(new Image(partialPath + "Libri1." + variant + ".png"));
                            case GAME -> imageView.setImage(new Image(partialPath + "Giochi1." + variant + ".png"));
                            case FRAME -> imageView.setImage(new Image(partialPath + "Cornici1." + variant + ".png"));
                            case TROPHY -> imageView.setImage(new Image(partialPath + "Trofei1." + variant + ".png"));
                            case PLANT -> imageView.setImage(new Image(partialPath + "Piante1." + variant + ".png"));
                        }
                    } else if(validGrid[i][j] == INVALID) {
                        if(gameboardItemSlotMatrix[i][j] != null && gameboardItemSlotMatrix[i][j].getParent()!=null)
                            gameboardItemSlotMatrix[i][j].getParent().setDisable(true);
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

    /**
     * handles the action event when an item is clicked and the logic for adding or removing the clicked item
     * from the selection, numbering the item from one to three according to the player's choice. Checks if the clicked
     * item is playable or not. Play a specific sound to indicate item selection. When an item is selected, its border
     * turns blue.
     * @param event the event to be managed
     */
    public void itemClick(ActionEvent event){
        this.updateMessageBox("", false);
        System.out.println("Item click");
        System.out.println(event.getSource().toString());
        String temp = event.getSource().toString();
        String[] stringArray = temp.split("_");
        int row = Integer.parseInt(stringArray[1]);
        int col = Integer.parseInt(stringArray[2]);
        int[] coords = {row, col};
        if(validGridForItemSelection[row][col] == PLAYABLE) return;
        playSound(ItemSelect);
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
                messageBox.setBackground(new Background(new BackgroundFill(
                        Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
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

    /**
     * handles the action event when the confirm selection button is clicked. Checks the validity of the selected items.
     * If the selection is not corrected shows an error message, otherwise the message consists of asking in which column
     * of the library put the items.
     * @param event te event to be managed
     */
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
            this.itemImgPath.addAll(ordinalBorderPanes.stream()
                    .map(borderPane -> ((ImageView) borderPane.getCenter()).getImage().getUrl()).toList());
            ordinalBorderPanes.forEach(borderPane -> borderPane.getStyleClass().clear());
            this.setMaxNumOfItems(numOfItems);
            numOfItems = 0;
            this.updateMessageBox("Where do you want\nto put these items?", false);
        } catch (InvalidSelectionException e) {
            playSound(warning);
            this.updateMessageBox("You can't pick these item tiles!\nPlease choose 1 to 3 items again.", true);
        } catch (FullColumnException ignored) {
        }
    }

    /**
     * handles the action event when a shelf column button is clicked. Sets the selected column based on the button
     * clicked, checks the selected column for validity and notifies the GUI. If the column is full shows en error
     * message and plays a specific sound.
     * @param event the event to be managed
     */
    public void shelfColumnButtonAction(ActionEvent event){
        String[] cols = {"1", "2", "3", "4", "5"};
        String eventString = event.getSource().toString();
        for(String col : cols)
            if(eventString.contains(col))
                this.columnReference.add(Integer.parseInt(col));
        try{
            ((SelectColumnCommand) this.commands.get(1)).setColumn(this.columnReference.get(0));
            this.commands.get(1).check();
            for(Button b : shelfColumnButtonList){
                b.setDisable(true);
                b.setVisible(false);
            }
            ordinalBorderPanes.forEach(borderPane -> ((ImageView) borderPane.getCenter()).setImage(null));
            this.activeShelf.setOpacity(0.7);
            this.shelfItemPane.setOpacity(0.7);
            this.prevColSelected = this.columnReference.get(0);
            List<int[]> toSend = new ArrayList<>();
            for(int[] coords : selectedCoords){
                toSend.add(new int[]{coords[0], coords[1]});
            }
            new Thread(() ->
                    this.gui.setChangedAndNotifyListener(toSend, this.prevColSelected - 1)).start();
            ordinalBorderPanes.clear();
            this.selectedCoords.clear();
            this.columnReference.clear();
            this.updateMessageBox("", false);
        } catch (InvalidSelectionException ignored) {
        } catch (FullColumnException e) {
            playSound(warning);
            confirmButtonFlag = false;
            for(Button b : shelfColumnButtonList){
                b.setDisable(true);
                b.setVisible(false);
            }
            this.activeShelf.setOpacity(0.7);
            this.shelfItemPane.setOpacity(0.7);
            this.columnReference.clear();
            this.updateMessageBox("The column you selected is full!\nPlease choose 1 to 3 items again!", true);
            this.ordinalBorderPanes.clear();
            for(int[] c: selectedCoords){
                int row = c[0];
                int col = c[1];
                gameboardItemMatrix[row][col] = false;
            }
            this.itemImgPath.clear();
            this.selectedCoords.clear();
            this.numOfItems = 0;
            this.switchGameBoardPaneStatus();
        }
    }

    /**
     * handles the action of the players' Shelf button. Plays a specific sound for the click and open a new window to
     * display the players' shelf  of the other players who are playing.
     */
    public void playersShelfButtonAction(){
        System.out.println("Players' Shelf button pressed");
        playSound(MenuSelection);
        String scenePath = "PlayersShelf.fxml";
        this.gui.openNewWindow(scenePath);
    }

    /**
     * activate the Shelf by setting the opacity of the active shelf and item pane.
     */
    public void activateShelf(){
        this.activeShelf.setOpacity(1);
        this.shelfItemPane.setOpacity(1);
    }

    /**
     * set methods
     * @param shelfView ShelfView
     */
    public void setCurrentShelf(ShelfView shelfView) {
        ((SelectColumnCommand) this.commands.get(1)).setShelfView(shelfView);
    }

    /**
     * makes the chair of the first player visible in the game window.
     */
    public void activateFirstPlayerChair(){
        this.firstPlayerChair.setVisible(true);
    }

    /**
     * switches the status of the game board pane
     */
    public void switchGameBoardPaneStatus(){
        if(this.gameboardPane.isDisable()) this.gameboardPane.setDisable(false);
        else if(confirmButtonFlag){
            this.gameboardPane.setDisable(true);
            confirmButtonFlag = false;
        }
    }

    /**
     * reset the game board Item Matrix
     */
    public void resetGameBoardItemMatrix(){this.gameboardItemMatrix = new boolean[DIM_GAMEBOARD][DIM_GAMEBOARD];}

    /**
     * set method for valid grid for item selection
     * @param validGrid -> int[][]
     */
    public void setValidGridForItemSelection(int[][] validGrid){this.validGridForItemSelection = validGrid;}

    /**
     * shows information dialog box about the match and closes the provided stage upon confirmation
     * @param msg message that is displayed
     * @param stage the stage to be closed upon confirmation
     */
    public void matchLogInfo(String msg, Stage stage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("MyShelfie Match Log");
        alert.setHeaderText("Dear MyShelfie player");
        alert.setContentText(msg);
        ButtonType confirmation = new ButtonType("Confirm");
        alert.getButtonTypes().setAll(confirmation);
        alert.setOnCloseRequest(event -> {
            event.consume();
            System.out.println("X button pressed");
            stage.close();
            System.out.println("Closing...");
            System.exit(0);
        });
        alert.showAndWait().ifPresent(response -> {
            System.out.println("Confirm button pressed");
            stage.close();
            System.out.println("Closing...");
            System.exit(0);
        });
    }

    /**
     * displays a disconnection alert dialog box.
     * @param msg message to be displayed
     * @param stage stage to be closed upon confirmation
     */
    public void disconnectionAlert(String msg, Stage stage){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("MyShelfie User Disconnection");
        alert.setHeaderText("A disconnection occurred!");
        alert.setContentText(msg);
        ButtonType confirmation = new ButtonType("Confirm");
        alert.getButtonTypes().setAll(confirmation);
        alert.setOnCloseRequest(event -> {
            event.consume();
            System.out.println("X button pressed");
            stage.close();
            System.out.println("Closing...");
            System.exit(-5);
        });
        alert.showAndWait().ifPresent(response -> {
            System.out.println("Confirm button pressed");
            stage.close();
            System.out.println("Closing...");
            System.exit(-5);
        });
    }

    /**
     * display a dialog box if the bag is empty and closes the provided stage upon confirmation
     * @param msg message that is displayed
     * @param stage the stage to be closed upon confirmation
     */
    public void emptyBagAlert(String msg, Stage stage){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("MyShelfie Server Error Notification");
        alert.setHeaderText("The Bag is empty");
        alert.setContentText(msg + "\nThe game ends here...");
        ButtonType confirmation = new ButtonType("Confirm");
        alert.getButtonTypes().setAll(confirmation);
        alert.setOnCloseRequest(event -> {
            event.consume();
            System.out.println("X button pressed");
            stage.close();
            System.out.println("Closing...");
            System.exit(-6);
        });
        alert.showAndWait().ifPresent(response -> {
            System.out.println("Confirm button pressed");
            stage.close();
            System.out.println("Closing...");
            System.exit(-6);
        });
    }

    /**
     * set method for the maximum number of items.
     * @param numOfItems int
     */
    private void setMaxNumOfItems(int numOfItems){
        ((SelectColumnCommand) this.commands.get(1)).setMaxNumOfItems(numOfItems);
    }

    /**
     * make the final point invisible
     */
    public void takeFinalPointToken(){
        finalPointToken.setVisible(false);
    }

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
}

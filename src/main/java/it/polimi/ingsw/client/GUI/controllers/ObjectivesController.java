package it.polimi.ingsw.client.GUI.controllers;

import it.polimi.ingsw.client.GUI.GUI;
import it.polimi.ingsw.modelview.CommonObjCardView;
import it.polimi.ingsw.modelview.PersonalObjCardView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.util.*;

import static java.util.Map.entry;

/**
 * The ObjectivesController class implements the GUIController interface
 * and controls the objective cards' window in the application.
 * It manages the display and updates of the common objective cards and the personal objective card.
 */
public class ObjectivesController implements GUIController {
    private static final Integer MAX_POINTS = 8;
    @FXML
    private ImageView comObjCard1;
    @FXML
    private ImageView comObjCard2;
    @FXML
    private Label comObjCardDesc1;
    @FXML
    private Label comObjCardDesc2;
    @FXML
    private ImageView personalObjCard;
    @FXML
    private List<ImageView> topScoringTokens;
    @FXML
    private List<ImageView> bottomScoringTokens;
    private final Map<Integer, List<ImageView>> cardsReference = new HashMap<>();
    private static final Map<Integer, Integer> intToInt= Map.ofEntries(
            entry(2, 0),
            entry(4, 1),
            entry(6, 2),
            entry(8, 3)
    );
    private List<Integer> prevKeyPoints = new ArrayList<>();
    private GUI gui;
    private MediaPlayer mediaPlayer;

    /**
     * it assigns the corresponding images and descriptions to the two objective cards of the match in the objective
     * cards' window
     * @param comObjCards list of comObjCards to be updated
     */
    public void updateComObjCards(List<CommonObjCardView> comObjCards){
        int cardType;
        Object[] imgs = {comObjCard1, comObjCard2, comObjCardDesc1, comObjCardDesc2};
        List<List<ImageView>> listOfList = new ArrayList<>();
        listOfList.add(topScoringTokens);
        listOfList.add(bottomScoringTokens);
        for(int i=0;i<comObjCards.size();i++){
            cardType = comObjCards.get(i).getType();
            ((ImageView) imgs[i]).setImage(new Image("/graphics/common_goal_cards/" + cardType + ".jpg"));
            ((Label) imgs[i+2]).setText(comObjCards.get(i).getDescription());
            cardsReference.put(cardType, listOfList.get(i));
            prevKeyPoints.add(MAX_POINTS);
        }
    }

    /**
     * it assigns the corresponding image to the personal objective card of the player in the objective cards' window
     * @param perObjCard PersonalObjCardView
     */
    public void updatePersonalObjCard(PersonalObjCardView perObjCard){
        personalObjCard.setImage(new Image("/graphics/personal_goal_cards/" + perObjCard.getDescription() + ".png"));
    }

    /**
     * updates common objective cards point in the objective cards' window
     * @param commonObjCardViews List
     */
    public void updateCommonObjCardsPoints(List<CommonObjCardView> commonObjCardViews){
        for(CommonObjCardView cocv : commonObjCardViews)
            cardsReference.get(cocv.getType()).forEach(imageView -> imageView.setVisible(false));
        int counterPosition = 0;
        for(CommonObjCardView commonObjCardView : commonObjCardViews){
            if(commonObjCardView.getNextPoints() != -1){
                int cardType = commonObjCardView.getType();
                int actualPoints = commonObjCardView.getPoints()[commonObjCardView.getNextPoints()];
                cardsReference.get(cardType).get(intToInt.get(actualPoints)).setVisible(true);
                prevKeyPoints.remove(counterPosition);
                prevKeyPoints.add(counterPosition, actualPoints);
                counterPosition++;
            }
        }
    }

    /**
     * method to set gui for the application.
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

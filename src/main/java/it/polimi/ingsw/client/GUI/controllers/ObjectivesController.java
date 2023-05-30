package it.polimi.ingsw.client.GUI.controllers;

import it.polimi.ingsw.client.GUI.GUI;
import it.polimi.ingsw.modelview.CommonObjCardView;
import it.polimi.ingsw.modelview.PersonalObjCardView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

public class ObjectivesController implements GUIController {
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
    private GUI gui;

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
        }
    }

    public void updatePersonalObjCard(PersonalObjCardView perObjCard){
        personalObjCard.setImage(new Image("/graphics/personal_goal_cards/" + perObjCard.getDescription() + ".png"));
    }

    public void updateCommonObjCardsPoints(List<CommonObjCardView> commonObjCardViews){
        for(CommonObjCardView cocv : commonObjCardViews)
            cardsReference.get(cocv.getType()).forEach(imageView -> imageView.setVisible(false));
        for(CommonObjCardView commonObjCardView : commonObjCardViews){
            int cardType = commonObjCardView.getType();
            int key = commonObjCardView.getPoints()[commonObjCardView.getNextPoints()];
            cardsReference.get(cardType).get(intToInt.get(key)).setVisible(true);
        }
    }

    @Override
    public void setGUI(GUI gui) {
        this.gui = gui;
    }

    @Override
    public void playSound(String filePath) {

    }
}

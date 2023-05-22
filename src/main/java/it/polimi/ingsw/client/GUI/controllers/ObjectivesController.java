package it.polimi.ingsw.client.GUI.controllers;

import it.polimi.ingsw.client.GUI.GUI;
import it.polimi.ingsw.modelview.CommonObjCardView;
import it.polimi.ingsw.modelview.PersonalObjCardView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.List;

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
    private GUI gui;

    public void updateComObjCards(List<CommonObjCardView> comObjCards){
        int cardType;
        Object[] imgs = {comObjCard1, comObjCard2, comObjCardDesc1, comObjCardDesc2};
        for(int i=0;i<comObjCards.size();i++){
            cardType = comObjCards.get(i).getType();
            ((ImageView) imgs[i]).setImage(new Image("/graphics/common goal cards/" + cardType + ".jpg"));
            ((Label) imgs[i+2]).setText(comObjCards.get(i).getDescription());
        }
    }

    public void updatePersonalObjCard(PersonalObjCardView perObjCard){
        personalObjCard.setImage(new Image("/graphics/personal goal cards/" + perObjCard.getDescription() + ".png"));
    }

    @Override
    public void setGUI(GUI gui) {
        this.gui = gui;
    }

    @Override
    public void playSound(String filePath) {

    }
}

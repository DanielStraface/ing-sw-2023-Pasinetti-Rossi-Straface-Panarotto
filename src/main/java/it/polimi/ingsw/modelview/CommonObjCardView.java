package it.polimi.ingsw.modelview;

import it.polimi.ingsw.model.comcard.CommonObjCard;

public class CommonObjCardView {
    private final CommonObjCard card;

    public CommonObjCardView(CommonObjCard card) {
        this.card = card;
    }
    public int getType(){
        return this.card.getType();
    }

    public String getDescription(){
        return this.card.getDescription();
    }
}

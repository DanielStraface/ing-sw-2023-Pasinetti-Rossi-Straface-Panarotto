package it.polimi.ingsw.modelview;

import it.polimi.ingsw.model.comcard.CommonObjCard;

import java.io.Serial;
import java.io.Serializable;

public class CommonObjCardView implements Serializable {
    @Serial
    private static final long serialVersionUID = 2L;
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

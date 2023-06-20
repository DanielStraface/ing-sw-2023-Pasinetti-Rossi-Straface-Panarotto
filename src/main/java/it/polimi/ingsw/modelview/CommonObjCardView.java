package it.polimi.ingsw.modelview;

import it.polimi.ingsw.model.comcard.CommonObjCard;

import java.io.Serial;
import java.io.Serializable;

/**
 * The immutable view of CommonObjCard
 */
public class CommonObjCardView implements Serializable {
    @Serial
    private static final long serialVersionUID = 2L;
    private final CommonObjCard card;
    private final int[] points;
    private final int nextPoints;

    /**
     * Constructor method
     * @param card CommonObjCard
     */
    public CommonObjCardView(CommonObjCard card) {
        this.card = card;
        this.points = card.copyPointsArray();
        this.nextPoints = card.getNextPoints();
    }

    /**
     * get method
     * @return the category type of the card
     */
    public int getType(){
        return this.card.getType();
    }

    /**
     * get method
     * @return the description of the card
     */
    public String getDescription(){
        return this.card.getDescription();
    }

    /**
     * get method
     * @return int[] -> points
     */
    public int[] getPoints(){return this.points;}
    /**
     * get method
     * @return int -> nextPoints
     */
    public int getNextPoints(){return this.nextPoints;}
}

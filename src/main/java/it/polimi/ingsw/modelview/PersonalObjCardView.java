package it.polimi.ingsw.modelview;

import it.polimi.ingsw.model.Item;

import java.io.Serial;
import java.io.Serializable;

public class PersonalObjCardView implements Serializable {
    @Serial
    private static final long serialVersionUID = 2L;
    private final Item[][] cardGrid;
    private final String description;

    /**
     * constructor method
     * @param cardGrid
     * @param desc
     */
    public PersonalObjCardView(Item[][] cardGrid, String desc) {
        this.description = desc;
        this.cardGrid = new Item[cardGrid.length][cardGrid[0].length];
        for(int i=0;i<cardGrid.length;i++){
            for(int j=0;j<cardGrid[0].length;j++){
                this.cardGrid[i][j] = new Item(cardGrid[i][j].getCategoryType());
            }
        }
    }

    /**
     * get method
     * @return Item[][] -> cardGrid, the representation of the card
     */
    public Item[][] getCardGrid(){
        return this.cardGrid;
    }

    /**
     * get method
     * @return String -> description, the description of the card
     */
    public String getDescription(){return this.description;}
}

package it.polimi.ingsw.modelview;

import it.polimi.ingsw.model.Item;

import java.io.Serial;
import java.io.Serializable;

public class PersonalObjCardView implements Serializable {
    @Serial
    private static final long serialVersionUID = 2L;
    private final Item[][] cardGrid;

    public PersonalObjCardView(Item[][] cardGrid) {
        this.cardGrid = new Item[cardGrid.length][cardGrid[0].length];
        for(int i=0;i<cardGrid.length;i++){
            for(int j=0;j<cardGrid[0].length;j++){
                this.cardGrid[i][j] = new Item(cardGrid[i][j].getCategoryType());
            }
        }
    }

    public Item[][] getCardGrid(){
        return this.cardGrid;
    }
}

package it.polimi.ingsw.model.modelview;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.personcard.PersonalObjCard;

public class PersonalObjCardView {
    private static final int ROWS = 6;
    private static final int COLS = 5;
    private final String personalObjCardDescription;
    private final Item[][] cardGrid;

    public PersonalObjCardView(PersonalObjCard personalObjCard) {
        this.cardGrid = new Item[ROWS][COLS];
        this.personalObjCardDescription = personalObjCard.getPersonalObjCardDescription();
        for(int i=0;i<ROWS;i++){
            for(int j=0;j<COLS;j++){
                this.cardGrid[i][j] = new Item(personalObjCard.getCardGrid()[i][j].getCategoryType());
            }
        }
    }

    public String getPersonalObjCardDescription() {
        return personalObjCardDescription;
    }

    public Item[][] getCardGrid() {
        return cardGrid;
    }
}

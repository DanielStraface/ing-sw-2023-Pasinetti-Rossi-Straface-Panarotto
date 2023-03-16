package it.polimi.ingsw.model;

import java.util.List;
import java.util.Random;

class Bag {
    private List<Item> itemCards;

    /** drawItem method picks randomly an Item in the bag and removes it, used
     * for filling the GameBoard */
    public Item drawItem(){
        Item temp;
        if(itemCards.size()>0){
            Random random = new Random();
            int randomNumber = random.nextInt(itemCards.size());
            temp = itemCards.get(randomNumber);
            itemCards.remove(randomNumber);
            return temp;
        }
    }

}

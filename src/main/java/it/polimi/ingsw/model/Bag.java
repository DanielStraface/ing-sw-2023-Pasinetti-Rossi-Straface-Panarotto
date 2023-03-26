package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Bag {
    private List<Item> itemCards;

    public Bag(){
        this.itemCards = new ArrayList<Item>();
    }


    // setter and getter methods
    public void setItemCards(Item item){
        this.itemCards.add(item);
    }
    public List<Item> getItemCards(){ return this.itemCards; }


    /** drawItem method picks randomly an Item in the bag and removes it, used
     * for filling the GameBoard */
    public Item drawItem() throws Exception{
        if(itemCards.size()>0){
            Random random = new Random();
            int randomNumber = random.nextInt(itemCards.size());
            return itemCards.remove(randomNumber);
        }
        else if(itemCards.size()==0){
            throw new Exception("Cannot draw Item,the bag is empty!");
        }
        return new Item(null);
    }

}

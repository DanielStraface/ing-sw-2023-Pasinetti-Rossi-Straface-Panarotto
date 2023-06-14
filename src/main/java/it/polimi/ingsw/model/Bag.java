package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.NoElementException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class represent the bag of the game. It has got only one attribute, the list of the item cards, and three
 * methods: a setter, a getter and drawItem that pop an item from the bag list
 * @method setItemCards(Item), getItemCards(), drawItem()
 */
public class Bag implements Serializable {
    /* ATTRIBUTES SECTION */
    private List<Item> itemCards;

    /*METHODS SECTION */
    /**  constructor
     */
    public Bag(){
        this.itemCards = new ArrayList<>();
    }

    // setter and getter methods

    /**
     * @param item - the item to be added
     */
    public void setItemCards(Item item){
        this.itemCards.add(item);
    }

    /**
     * @return the list of the item
     */
    public List<Item> getItemCards(){ return this.itemCards; }

    /*- logic methods -*/
    /** drawItem method picks randomly an Item in the bag and removes it, used
     * to fill the GameBoard, throws a NoElementException if the bag is empty
     * @return Item item : (cardsList.size() + 1 == \old(cardsList.size())) &&
     *                      (forall item i; !i.equals(item); cardsList.contains(i) <==> \old(cardsList.contains(i))) &&
     *                      !cardsList.contains(item) ==> \old(cardsList.contains(item)
     * @throws NoElementException if the bag is empty
     * */
    public Item drawItem() throws NoElementException {
        if(itemCards.size()>0){
            Random random = new Random();
            int randomNumber = random.nextInt(itemCards.size());
            return itemCards.remove(randomNumber);
        }
        else if(itemCards.size()==0){
            throw new NoElementException("Cannot draw Item,the bag is empty!");
        }
        return new Item(null, 0);
    }
}

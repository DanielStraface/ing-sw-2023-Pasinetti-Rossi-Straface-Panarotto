package it.polimi.ingsw.model;

import java.io.Serializable;

/**
 * Item class represents the object cards of the game.Their positions are important for the conduct of the game.
 * They are moved between the game board and the shelf of the player.
 */
public class Item implements Serializable {
    private final Category category;
    private final int variant;

    /** constructor
     * @param category - the category of the item
     * @param variant - the variant type of the item
     */
    public Item(Category category, int variant){
        this.variant = variant;
        this.category = category;
    }

    /**
     * get method
     * @return category
     */
    public Category getCategoryType(){return this.category;}

    /**
     * get method
     * @return int -> variant
     */
    public int getVariant(){return this.variant;}
}

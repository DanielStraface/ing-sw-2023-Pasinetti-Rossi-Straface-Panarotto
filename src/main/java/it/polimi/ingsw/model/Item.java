package it.polimi.ingsw.model;

import java.io.Serializable;

/**
 * Item class represents the object cards of the game.Their positions are important for the conduct of the game.
 * They are moved between the game board and the shelf of the player.
 */
public class Item implements Serializable {
    private final Category category;

    /** constructor
     * @param category
     */
    public Item(Category category){
        this.category=category;
    }

    /**
     * get method
     * @return category
     */
    public Category getCategoryType(){return category;}
}

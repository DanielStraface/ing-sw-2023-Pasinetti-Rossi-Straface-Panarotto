package it.polimi.ingsw.model;

import java.io.Serializable;

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

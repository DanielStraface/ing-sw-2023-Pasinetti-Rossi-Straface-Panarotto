package it.polimi.ingsw.model;

import java.io.Serializable;

public class Item implements Serializable {
    private final Category category;
    public Item(Category category){
        this.category=category;
    }
    public Category getCategoryType(){return category;}
}

package it.polimi.ingsw.model;

public class Item {
    private final Category category;
    public Item(Category category){
        this.category=category;
    }
    public Category getCategoryType(){return category;}
}

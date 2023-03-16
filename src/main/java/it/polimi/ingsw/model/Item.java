package it.polimi.ingsw.model;

class Item {
    private Category category;
    public Item(Category category){
        this.category=category;
    }
    public Category getCategoryType(){
        return category;
    }

}

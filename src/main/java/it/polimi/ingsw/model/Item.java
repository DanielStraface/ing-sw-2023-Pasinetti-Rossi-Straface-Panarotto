package it.polimi.ingsw.model;

class Item {
    private Category category;
    public Item(Category category){
        this.category=category;
    }
    private Category getCategoryType(){
        return category;
    }

}

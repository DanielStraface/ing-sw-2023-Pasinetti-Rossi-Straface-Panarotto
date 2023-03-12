package it.polimi.ingsw.model;

class Item {
    Category category;
    Item(String type){
        switch(type){
            case "CAT" -> this.category = Category.CAT;
            case "BOOK" -> this.category = Category.BOOK;
            case "GAME" -> this.category = Category.GAME;
            case "FRAME" -> this.category = Category.FRAME;
            case "TROPHY" -> this.category = Category.TROPHY;
            case "PLANT" -> this.category = Category.PLANT;
        }
    }
    public void printCategoryType(){
        System.out.println("This is a " + category);
    }
}

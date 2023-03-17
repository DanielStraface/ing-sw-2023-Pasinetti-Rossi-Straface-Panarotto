package it.polimi.ingsw.model;

public class PersonalObjCard {

    /* ATTRIBUTES SECTIONS */
    private String personalObjCardDescription; // The name of the personalObjCard, initially store in JSON file
    private Item[][] cardGrid; // This provides the position of item needed to score points, initially store in JSON file


    /* METHODS SECTION */

    /* -- get methods -- */
    /**
     * Method getPersonalObjCardDescription returns a string with the description of this card.
     * @return this.personalObjCardDescription
     */
    public String getPersonalObjCardDescription() {
        return this.personalObjCardDescription;
    }

    /**
     * Method getCardGrid returns a reference to cardGrid.
     * @return this.cardGrid
     */
    public Item[][] getCardGrid(){
        return this.cardGrid;
    }

    /* -- logic methods -- */
    /**
     * Method goalReached returns true if there is a match between the item position in the player shelf and in the
     * template grid of the card. This grid was loaded from a Json file.
     * @return TRUE <==> (forall Item I1 in the (x,y) position in player's myShelf
     *                      exists Item I2 in the (x,y) position in cardGrid template) &&
     *                      I1.getCategoryType() == I2.getCategoryType()
     */
    public boolean goalReached(Shelf shelf) {
        /* Two for-cycle to analyse both matrix in player shelf and template grid */
        for (int i = 0; i < cardGrid.length; i++) {
            for (int j = 0; j < cardGrid[i].length; j++) {
                /* if the position in the template grid and player's shelf is not null there is an item type */
                if (cardGrid[i][j].getCategoryType() != null && shelf.GetShelfGrid()[i][j].getCategoryType() != null) {
                    /* If there is at least one type of item that is not equal to the item in the player shelf
                     *  the goal is not reached. The method returns false */
                    /** ACCESSORY CODE HERE **/
                    System.out.println(cardGrid[i][j].getCategoryType() + " =?= " +
                                    shelf.GetShelfGrid()[i][j].getCategoryType()
                            );
                    /*************************/
                    if(cardGrid[i][j].getCategoryType() != shelf.GetShelfGrid()[i][j].getCategoryType()){
                        return false;
                    }
                }
            }
        }
        /* If during the cardGrid scanning the condition of @IF1 is always false it means that all the matches
        *  between the two item grid are true. The method must return true */
        return true;
    }

    /** ACESSORY METHOD HERE **/
    public void show(){
        System.out.println(this.getPersonalObjCardDescription());
        System.out.println("This card has a " + cardGrid.length + " card grid height");
        System.out.println("This card has a " + cardGrid[0].length + " card grid width");
        for(int i=0;i<this.cardGrid.length;i++){
            for(int j=0;j<this.cardGrid[0].length;j++){
                System.out.print(this.cardGrid[i][j].getCategoryType() + " ");
            }
            System.out.println();
        }
    }
}

package it.polimi.ingsw.model;

class PersonalObjCard {

    private String personalObjCardDescription; // See JSON reading from file with Java
    private Item[][] cardGrid; // See later

    /* METHODS SECTION */
    /**
     * Method getPersonalObjCardDescription returns a string with the description of this card.
     * -- Maybe this method must be edit in function of JSON loading strings
     */
    public String getPersonalObjCardDescription() {
        return personalObjCardDescription;
    }
    public boolean goalReached(Shelf shelf){
        for(int i=0;i<cardGrid.length;i++){
            for(int j=0;j<cardGrid[i].length;j++){
                if(cardGrid[i][j] != null){
                    if(cardGrid[i][i].getCategoryType() != shelf.GetShelfGrid()[i][j].getCategoryType()){
                        return false;
                    }
                }
            }
        }
        return true;
    }
}

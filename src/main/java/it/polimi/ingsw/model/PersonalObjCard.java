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
}

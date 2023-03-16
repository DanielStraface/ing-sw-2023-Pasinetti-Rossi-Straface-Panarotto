package it.polimi.ingsw.model;

class CommonObjCard {
    private static final int DIM = 4;
    private int[] objPoints;
    private int nextPoints;
    private String commonObjCardDescription; // See JSON reading from file with Java

    /**
     * Constructor for CommonObjCard
     */
    public CommonObjCard(int numberOfPlayers){
        switch(numberOfPlayers){
            case 2 : {
                int[] temp = {4, 8, 0, 0};
                this.nextPoints = 1;
                this.objPoints = temp;
            }
            case 3 : {
                int[] temp = {4, 6, 8, 0};
                this.nextPoints = 2;
                this.objPoints = temp;
            }
            case 4 : {
                int[] temp = {2, 4, 6, 8};
                this.nextPoints = 3;
                this.objPoints = temp;
            }
        }
    }

    /* METHOD SECTION */

    /**
     * Method getCommonObjCardDescription returns a string with the description of this card
     * -- Maybe this method must be edit in function of JSON loading strings
     */
    public String getCommonObjCardDescription() {
        return commonObjCardDescription;
    }

    /**
     * Method getPoint returns the points for this card based on what order the players has reached the goal
     */
    public int getPoint() {
        int tempPoint = objPoints[objPoints.length - 1];
        nextPoints = nextPoints - 1;
        return tempPoint;
    }
}

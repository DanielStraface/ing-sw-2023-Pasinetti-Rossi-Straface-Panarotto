package it.polimi.ingsw.model;

/**
 * STATUS DESCRIPTION FOR RowsColumnsCard
 * 6 --> Row checker
 * 8 --> Column checker
 */

/**
 * STATUS DESCRIPTION FOR GroupCards
 *  1 ->
 *  5 ->
 *  7 ->
 *  11 ->
 */

/** STATUS DESCRIPTION FOR ThreeDifferentTypes
 *
 * 4 --> RowsDifferentTypes
 * 9 --> ColumnsDifferentTypes
 */

/**
 * STATUS DESCRIPTION FOR CornerDiagonals
 * 2 -->
 * 3 -->
 * 12 -->
 */

/**
 * STATUS DESCRIPTION FOR XCard
 * 10 -->
 */

public class CommonObjCard {
    private static final int DIM = 4;
    private int[] objPoints;
    private int nextPoints;
    private String commonObjCardDescription; // See JSON reading from file with Java

    protected int status;

    /**
     * Constructor for CommonObjCard. it is based on how many players will play the game because the points provides by
     * CommonObjCard instance is different.
     */
    public CommonObjCard(int numberOfPlayers){
        switch(numberOfPlayers){
            case 2 -> {
                int[] temp = {4, 8, 0, 0};
                this.nextPoints = 1;
                this.objPoints = temp;
            }
            case 3 -> {
                int[] temp = {4, 6, 8, 0};
                this.nextPoints = 2;
                this.objPoints = temp;
            }
            case 4 -> {
                int[] temp = {2, 4, 6, 8};
                this.nextPoints = 3;
                this.objPoints = temp;
            }
            default -> {
                this.nextPoints = 0;
                this.objPoints = null;
            }
        }
    }

    /* METHOD SECTION */

    /* -- get methods */
    /**
     * Method getCommonObjCardDescription returns a string with the description of this card
     * -- Maybe this method must be edited in function of JSON loading strings
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

    /* -- logic methods */
    public boolean check(Player player){return false;}
}



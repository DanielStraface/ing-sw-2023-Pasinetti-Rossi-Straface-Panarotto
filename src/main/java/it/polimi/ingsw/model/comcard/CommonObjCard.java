package it.polimi.ingsw.model.comcard;

import it.polimi.ingsw.model.Player;


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

    /* ATTRIBUTES SECTION */
    private final int[] objPoints;
    private int nextPoints;
    private StrategyCheck strategyCheck;
    private final int type;

    /* METHODS SECTION */

    /* -- constructor */

    /**
     * Constructor for CommonObjCard. It is based on how many players will play the game because the points provides by
     * CommonObjCard instance is different.
     */
    public CommonObjCard(int numberOfPlayers, int type){
        /*The switch case are chosen by the number of players of the game.
        * For each case statement, the objPoints array assume different points configuration.
        * The nextPoints variables refers to the next points that will be distributed to the player who claim it.
        */
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
        this.type = type;
    }

    /* -- get methods */
    /**
     * Method getCommonObjCardDescription returns a string with the description of this card
     * -- Maybe this method must be edited in function of JSON loading strings
     * @return result <==> commonObjCardDescription
     */
    public int getType(){
        return this.type;
    }

    /**
     * Method getPoint returns the points for this card based on what order the players has reached the goal.
     * @return result <==> objPoints[objLength - 1]
     */
    public int getPoint() throws NullPointerException {
        if (objPoints.length - 1 < 0) throw new NullPointerException("The array length is zero");
        else {
            int tempPoint = objPoints[objPoints.length - 1];
            nextPoints = nextPoints - 1;
            return tempPoint;
        }
    }

    /* -- logic methods */

    /**
     * doCheck method controls if the condition for distributes points subsist.
     * @return true <==> conditions of the commonObjCard subsists for the parameter player
     */
    public void doCheck(Player player){
        strategyCheck.check(player.getMyShelf().getShelfGrid(), this.type);
    }
}



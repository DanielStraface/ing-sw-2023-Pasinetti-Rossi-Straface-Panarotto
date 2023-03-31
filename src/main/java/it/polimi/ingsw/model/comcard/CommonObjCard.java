package it.polimi.ingsw.model.comcard;

import it.polimi.ingsw.exceptions.InvalidNumberOfPlayersException;
import it.polimi.ingsw.model.Player;

import java.util.ArrayList;
import java.util.List;


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
    private List<Player> playersDone;

    /* METHODS SECTION */

    /* -- constructor */

    /**
     * Constructor for CommonObjCard. It is based on how many players will play the game because the points provides by
     * CommonObjCard instance is different.
     */
    public CommonObjCard(int numberOfPlayers, int type) throws InvalidNumberOfPlayersException {
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
                throw new InvalidNumberOfPlayersException();
            }
        }
        defineStrategy(type);
        playersDone = new ArrayList<Player>();
    }

    /* -- get methods */
    /**
     * Method getCommonObjCardDescription returns a string with the description of this card
     * -- Maybe this method must be edited in function of JSON loading strings
     * @return result <==> commonObjCardDescription
     */
    public int getType(){return this.strategyCheck.getType();}

    /**
     * Method getPoint returns the points for this card based on what order the players has reached the goal.
     * @return result <==> objPoints[objLength - 1]
     */
    public int getPoints() throws NullPointerException {
        if (objPoints.length - 1 < 0) throw new NullPointerException("The array length is zero");
        else {
            int tempPoints = objPoints[nextPoints];
            nextPoints = nextPoints - 1;
            return tempPoints;
        }
    }

    /* -- logic methods */

    private void defineStrategy(int type){
        if(type == 2 || type == 3 || type == 12) strategyCheck = new CornerDiagonals(type);
        if(type == 1 || type == 5 || type == 7 || type == 11) strategyCheck = new GroupCards(type);
        if(type == 6 || type == 8) strategyCheck = new RowsColumnsCard(type);
        if(type == 4 || type == 9) strategyCheck = new ThreeDifferentTypes(type);
        if(type == 12) strategyCheck = new XCards(type);
    }

    /**
     * doCheck method controls if the condition for distributes points subsist.
     * @return true <==> conditions of the commonObjCard subsists for the parameter player
     */
    public void doCheck(Player player){
        if(!this.playersDone.contains(player)){
            boolean isTrue = strategyCheck.check(player.getMyShelf().getShelfGrid());
            if(isTrue){
                player.addPoints(this.getPoints());
                playersDone.add(player);
            }
        }
    }
}



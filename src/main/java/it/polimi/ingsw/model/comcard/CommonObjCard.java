package it.polimi.ingsw.model.comcard;

import it.polimi.ingsw.exceptions.InvalidNumberOfPlayersException;
import it.polimi.ingsw.exceptions.InvalidPointerException;
import it.polimi.ingsw.exceptions.OutOfBoundsException;
import it.polimi.ingsw.model.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// ONE --> Quadrati2x2
// TWO --> Columns6Element
// THREE --> nostra 5
// FOUR --> nostra 1
// FIVE

/**
 * CommonObjCard class represents the common objective cards of the game.
 */
public class CommonObjCard implements Serializable {

    /* ATTRIBUTES SECTION */
    private final String description;
    private final int[] objPoints;
    private int nextPoints;
    private StrategyCheck strategyCheck;
    private List<Player> playersDone;

    /* METHODS SECTION */

    /* -- constructor */

    /**
     * Constructor for CommonObjCard. It is based on how many players will play the game because the points provides by
     * CommonObjCard instance is different.
     * @param numberOfPlayers number of players in a match
     * @param type the type of CommonObjectiveCard
     * @throws InvalidNumberOfPlayersException
     */
    public CommonObjCard(int numberOfPlayers, int type) throws InvalidNumberOfPlayersException {
        /*The switch case are chosen by the number of players of the game.
        * For each case statement, the objPoints array assume different points configuration.
        * The nextPoints variables refers to the next points that will be distributed to the player who claim it.
        */
        this.description = null;
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

    /**
     * Constructor for CommonObjCard. It is based on how many players will play the game because the points provides by
     * CommonObjCard instance is different.
     * @param numberOfPlayers number of players in a match
     * @param type the type of CommonObjectiveCard
     * @param description the card's description taken from the json file
     * @throws InvalidNumberOfPlayersException
     */
    public CommonObjCard(int numberOfPlayers, int type, String description) throws InvalidNumberOfPlayersException {
        /*The switch case are chosen by the number of players of the game.
         * For each case statement, the objPoints array assume different points configuration.
         * The nextPoints variables refers to the next points that will be distributed to the player who claim it.
         */
        this.description = description;
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
    public int getPoints() throws InvalidPointerException, OutOfBoundsException {
        if (objPoints.length - 1 < 0) throw new InvalidPointerException("The array length is zero");
        if(nextPoints < 0) throw new OutOfBoundsException("All the points for this card are taken");
            int tempPoints = objPoints[nextPoints];
            nextPoints = nextPoints - 1;
            return tempPoints;
    }

    public String getDescription() {
        return description;
    }
    /* -- logic methods */

    /**
     * defines all the check methods depending on the type of card
     * @param type
     */
    private void defineStrategy(int type){
        if(type == 2 || type == 6) strategyCheck = new RowsColumnsCard(type);
        if(type == 7 || type == 5) strategyCheck = new ThreeDifferentTypes(type);
        if(type == 11 || type == 8 || type == 12) strategyCheck = new CornerDiagonals(type);
        if(type == 4 || type == 3 || type == 1 || type == 9) strategyCheck = new GroupCards(type);
        if(type == 10) strategyCheck = new XCards(type);
    }

    /**
     * doCheck method controls if the condition for distributes points subsist.
     * @param player
     * @return true <==> conditions of the commonObjCard subsists for the parameter player
     */
    public int doCheck(Player player) throws InvalidPointerException {
        if(!this.playersDone.contains(player)){
            boolean isTrue = strategyCheck.check(player.getMyShelf().getShelfGrid());
            if(isTrue){
                try{
                    /*player.addPointsByCommonObjCard(numOfPoints, "Common Objective Card " +
                            this.strategyCheck.type + " goal reach!\n" +
                            "Obtain +" + numOfPoints + " points!");*/
                    playersDone.add(player);
                    return this.getPoints();
                } catch (OutOfBoundsException e){
                    System.err.println("Throw an OutOfBoundException in " + this + ".getPoints()");
                }
            }
        }
        return -1;
    }

    /**
     * copyPointsArray copy the points configuration of objPoints array in temp array. the configuration is chosen
     * by the number of players of the game.
     * @return temp
     */
    public int[] copyPointsArray(){
        int[] temp = new int[4];
        System.arraycopy(this.objPoints, 0, temp, 0, this.objPoints.length);
        return temp;
    }

    /**
     * get method
     * @return int -> nextPoints
     */
    public int getNextPoints(){return this.nextPoints;}

}



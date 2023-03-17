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
 * 12 -->
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

class RowsColumnsCard extends CommonObjCard {
    public RowsColumnsCard(int numberOfPlayers, int status){
        super(numberOfPlayers);
        this.status = status;
    }

    public boolean checker(){
        boolean result = false;
        switch (status){
            case 6 -> result = rowsChecker();
            case 8 -> result = columnsChecker();
            default -> {
                return false;
            }
        }
        return true;
    }

    private boolean rowsChecker(){
        return false;
    }

    private boolean columnsChecker(){
        return false;
    }
}



class ThreeDifferentTypes extends CommonObjCard {
    public ThreeDifferentTypes(int numberOfPlayers, int status){
        super(numberOfPlayers);
        this.status = status;
    }

    public boolean checker(){
        boolean result = false;
        switch (status) {
            case 4 -> result = rowsDifferentTypes();
            case 9 -> result = columnsDifferentTypes();
            default -> {
                return false;
            }
        }
        return true;
    }

    private boolean rowsDifferentTypes(){
        return false;
    }

    private boolean columnsDifferentTypes(){
        return false;
    }
}

class CornerDiagonals extends CommonObjCard {
    public CornerDiagonals(int numberOfPlayers, int status){
        super(numberOfPlayers);
        this.status = status;
    }

    public boolean checker(){
        boolean result = false;
        switch (status) {
            case 2 -> result =diagonals();
            case 3 -> result = corners();
            case 12 -> result = descMatrix();
            default -> {
                return false;
            }
        }
        return true;
    }

    public boolean diagonals(){
        return false;
    }

    public boolean corners(){
        return false;
    }

    public boolean descMatrix() {
        return false;
    }
}

class GroupCards extends CommonObjCard {
    public GroupCards(int numberOfPlayers, int status){
        super(numberOfPlayers);
        this.status = status;
    }

    public boolean check(){
        boolean result = false;
        switch (status) {
            case 1 -> result =groupOfTwo();
            case 5 -> result = groupOfFour();
            case 7 -> result = groupOfSquares();
            case 11 -> result = groupOfEight();
            default -> {
                return false;
            }
        }
        return true;
    }

    private boolean groupOfTwo(){
        return true;
    }

    private boolean groupOfFour(){
        return true;
    }

    private boolean groupOfSquares(){
        return true;
    }

    private boolean groupOfEight(){
        return true;
    }

}

class XCards extends CommonObjCard{
    public XCards(int numberOfPlayers, int status){
        super(numberOfPlayers);
        this.status = status;
    }

    public boolean xChecker(){
        return true;
    }
}
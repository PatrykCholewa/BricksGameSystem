package game;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author Patryk Cholewa
 * @since 24.11.2017
 */

public class Referee {

    private Board board;
    private Rules rules;

    /**
     * Referee constructor
     *
     * @param size Length of board base
     * @throws IllegalArgumentException         On size lower than 3    <br>
     *                                          or higher than 999      <br>
     *                                          or an odd size
     */
    public Referee( int size ) throws IllegalArgumentException {

        Rules.validateSize( size );
        this.board = new Board(size);
        this.rules = new Rules(board);

    }

    /**
     * Returns last player's number.
     *
     * @return last player's number ( or 0 if last move was committed by system
     */
    public int getLastPlayer(){
        return board.getLastPlayer();
    }

    /**
     * Checks if there is any allowed next move.
     *
     * @return true if no next move is allowed on board
     */
    public Boolean isFinished(){
        return rules.isFinished();
    }

    /**
     * Sets a board with random boxes.
     *
     * @param numberOfRandomBoxes number of boxes to generate
     * @return list of generated boxes
     * @throws IllegalArgumentException         If numberOfRandomBoxes is bigger or equals capacity of the board.
     * @throws UnsupportedOperationException    If any rectangle was added.
     */
    public ArrayList<Point> setInitialBoxesRandomly(int numberOfRandomBoxes ){
        return board.setInitialBoxesRandomly(numberOfRandomBoxes);
    }

    /**
     * Sets the board with given boxes.
     *
     * @param listOfBoxes list of boxes to set on board
     * @throws UnsupportedOperationException    If any rectangle was added.
     */
    public void setInitialBoxes( ArrayList<Point> listOfBoxes ){
        board.setInitialBoxes(listOfBoxes);
    }

    /**
     * Validates the player's move. Updates a state of the board.
     *
     * @param box1 With box2 makes the rectangle. Boxes must be stuck to each other
     *             and their indexes have to fit inside board.
     * @param box2 Same as box1
     * @param playerNumber Number of player
     * @throws IllegalArgumentException On playerNumber is greater than 2                   <br>
     *                                  or points are not stuck to each other               <br>
     *                                  or a box was already put                            <br>
     *                                  or a box does not fit into boundaries of the board.
     */
    public void addRectangle( Point box1 , Point box2 , Integer playerNumber ) throws IllegalArgumentException {
        rules.validateAddingRectangle( box1 , box2 , playerNumber );
        board.addRectangle( box1 , box2 , playerNumber );
    }

    @Override
    public String toString() {
        return board.toString();
    }

}
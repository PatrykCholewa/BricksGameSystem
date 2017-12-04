package game;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author Patryk Cholewa
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
     * Checks if there is any allowed next move.
     *
     * @return true if no next move is allowed on board
     */
    public Boolean isFinished(){
        return rules.isFinished();
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
     * @param boxes Rectangle. Boxes must be stuck to each other
     *             and their indexes have to fit inside board.
     * @throws IllegalArgumentException On playerNumber is greater than 2                   <br>
     *                                  or points are not stuck to each other               <br>
     *                                  or a box was already put                            <br>
     *                                  or a box does not fit into boundaries of the board.
     */
    public void addRectangle( Point []boxes ) throws IllegalArgumentException {
        rules.validateAddingRectangle( boxes );
        board.addRectangle( boxes );
    }

    @Override
    public String toString() {
        return board.toString();
    }

}
package game;

import java.awt.*;

public class GameMaster{

    private Board board;
    private RulesMaster rulesMaster;

    public GameMaster( int size ) throws IllegalArgumentException {

        RulesMaster.validateSize( size );
        this.board = new Board(size);
        this.rulesMaster = new RulesMaster(board);

    }

    public GameMaster( Object boardObjectCopy ) {

        try {
            this.board = (Board) boardObjectCopy;
        } catch ( ClassCastException e ){
            throw new IllegalArgumentException( "This argument is not a board, but " + boardObjectCopy.getClass() + "." );
        }
        this.rulesMaster = new RulesMaster(this.board);

    }

    public Boolean isFinished(){
        return rulesMaster.isFinished();
    }

    public void addRandomBoxes(int numberOfRandomBoxes ){
        board.addRandomBoxes(numberOfRandomBoxes);
    }

    public Object getBoardObjectCopy(){
        return board.getCopy();
    }

    public void addRectangle( Point point1 , Point point2 , Integer playerNumber ) throws IllegalArgumentException {
        rulesMaster.validateAddingRectangle( point1 , point2 , playerNumber );
        board.addRectangle( point1 , point2 , playerNumber );
    }

    public Character [][]getBoard(){
        return board.getMatrix();
    }

    @Override
    public String toString() {
        return board.toString();
    }

}
package game;

import java.awt.*;

public class GameMaster{

    private Board board;
    private RulesMaster rulesMaster;
    private int lastPlayer = 0;

    public GameMaster( int size ) throws IllegalArgumentException {

        RulesMaster.validateSize( size );
        this.board = new Board(size);
        this.rulesMaster = new RulesMaster(board);

    }

    public int getLastPlayer(){
        return lastPlayer;
    }

    public Boolean isFinished(){
        return rulesMaster.isFinished();
    }

    public void addRandomBoxes(int numberOfRandomBoxes ){
        board.addRandomBoxes(numberOfRandomBoxes);
    }

    public GameMaster getCopy(){
        GameMaster gmCopy = new GameMaster( board.getMatrixSize() );
        gmCopy.board = board.getCopy();
        gmCopy.rulesMaster = new RulesMaster( board );
        return gmCopy;
    }

    public void addRectangle( Point point1 , Point point2 , Integer playerNumber ) throws IllegalArgumentException {
        lastPlayer = playerNumber;
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
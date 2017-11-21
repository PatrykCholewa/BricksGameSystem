package game;

import java.awt.*;
import java.util.ArrayList;

public class Referee {

    private Board board;
    private Rules rules;
    private int lastPlayer = 0;

    public Referee(int size ) throws IllegalArgumentException {

        Rules.validateSize( size );
        this.board = new Board(size);
        this.rules = new Rules(board);

    }

    public int getLastPlayer(){
        return lastPlayer;
    }

    public Boolean isFinished(){
        return rules.isFinished();
    }

    public ArrayList<Point> addRandomBoxes(int numberOfRandomBoxes ){
        return board.addRandomBoxes(numberOfRandomBoxes);
    }

    public Referee getCopy(){
        Referee gmCopy = new Referee( board.getMatrixSize() );
        gmCopy.board = board.getCopy();
        gmCopy.rules = new Rules( board );
        return gmCopy;
    }

    public void addRectangle( Point point1 , Point point2 , Integer playerNumber ) throws IllegalArgumentException {
        lastPlayer = playerNumber;
        rules.validateAddingRectangle( point1 , point2 , playerNumber );
        board.addRectangle( point1 , point2 , playerNumber );
    }

    public void addBox( Point point ){
        board.setValue( point ,'X' );
    }

    public void removeBox( Point point ){
        board.setValue( point , '0' );
    }

    @Override
    public String toString() {
        return board.toString();
    }

}
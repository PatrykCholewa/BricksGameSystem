package game;

import java.awt.*;
import java.util.ArrayList;

public class GameMaster{

    private Board board;
    private Analyzer analyzer;

    public GameMaster( int size ){
        this.board = new Board(size);
        this.analyzer = new Analyzer(board);
    }

    public GameMaster( int size , ArrayList<Point> listOfBoxes ){
        this( size );
        board.addBoxes( listOfBoxes );
    }

    public Boolean isFinished(){
        return analyzer.isFinished();
    }

    public ArrayList<Point> setRandomBoxesBoard(int numberOfRandomBoxes ){
        return board.setRandomBoxesBoard(numberOfRandomBoxes);
    }

    public void addRectangle( int row1 , int column1 , int row2 , int column2 , Integer playerNumber ) throws IllegalArgumentException {
        analyzer.validateAdding( row1 , column1 , row2 , column2 , playerNumber );
        board.addRectangle( row1 , column1 , row2, column2 , playerNumber );
    }

    public Character [][]getBoard(){
        return board.getMatrix();
    }

    @Override
    public String toString() {
        return board.toString();
    }

}
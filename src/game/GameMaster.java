package game;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

public class GameMaster{

    private Board board;
    private Validator validator;

    public GameMaster( int size ){
        this.board = new Board(size);
        this.validator = new Validator(board);
    }

    public ArrayList<Point> addRandomBoxes( int numberOfRandomBoxes ){

        Random rndGen = new Random();
        ArrayList<Point> listOfBoxes = new ArrayList<>();
        Point box;

        if( board.getMatrixSize()*board.getMatrixSize() <= numberOfRandomBoxes ){
            throw new IllegalArgumentException( "Board is too small for so many boxes." );
        }

        for( int i = 0 ; i < numberOfRandomBoxes ; ){

            box = new Point( rndGen.nextInt(board.getMatrixSize()) , rndGen.nextInt(board.getMatrixSize()) );

            if( board.getValue( box.x , box.y ) != 'X' ) {
                board.setValue( box.x , box.y , 'X');
                i++;
                listOfBoxes.add(box);
            }

        }

        return listOfBoxes;

    }

    public void addRectangle( int row1 , int column1 , int row2 , int column2 , Integer playerNumber ) throws IllegalArgumentException {

        validator.validateAdding( row1 , column1 , row2 , column2 , playerNumber );

        board.setValue( row1 , column1 , playerNumber );
        board.setValue( row2 , column2 , playerNumber );

    }

    public Character [][]getBoard(){
        return board.getMatrix();
    }

    @Override
    public String toString() {
        return board.toString();
    }

}
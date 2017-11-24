package game;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * @author Patryk Cholewa
 */

class Board {

    private Matrix matrix;
    private int lastPlayer = 0;

    Board(int size) {
        matrix = new Matrix( size );
    }

    int getMatrixSize() {
        return matrix.getMatrixSize();
    }

    Character getValue( Point p ) {
        try {
            return matrix.getValue( p );
        } catch ( IndexOutOfBoundsException e ){
            throw new IllegalArgumentException( e.getMessage() );
        }
    }

    int getLastPlayer(){
        return lastPlayer;
    }

    ArrayList<Point> setInitialBoxesRandomly(int numberOfRandomBoxes ){

        if( lastPlayer != 0 )
            throw new UnsupportedOperationException( "There has already been a player's move." );

        Random rndGen = new Random();
        Point box;
        ArrayList<Point> boxList = new ArrayList<>();

        if( matrix.getMatrixSize()*matrix.getMatrixSize() <= numberOfRandomBoxes )
            throw new IllegalArgumentException( "Board is too small for so many boxes." );

        matrix.clear();

        for( int i = 0 ; i < numberOfRandomBoxes ; ){

            box = new Point( rndGen.nextInt(matrix.getMatrixSize()) , rndGen.nextInt(matrix.getMatrixSize()) );

            if( getValue( box ) != 'X' ) {
                matrix.setValue( box , 'X');
                boxList.add(box);
                i++;
            }

        }

        return boxList;

    }

    void setInitialBoxes(ArrayList<Point> listOfBoxes ){

        if( lastPlayer != 0 ) throw new UnsupportedOperationException( "Player has already made move. ");

        matrix.clear();

        for( Point box : listOfBoxes ){
            matrix.setValue( box, 'X' );
        }

    }

    void addRectangle( Point p1 , Point p2 , Integer playerNumber ) throws IllegalArgumentException {

        lastPlayer = playerNumber;
        matrix.setValue( p1 , playerNumber );
        matrix.setValue( p2 , playerNumber );

    }

    Character[][] getMatrix() {
        return matrix.getMatrix();
    }

    @Override
    public String toString(){
        return matrix.toString();
    }

    /*
    Board getCopy() {

        Board boardCopy = new Board( getMatrixSize() );
        boardCopy.setMatrix( matrix.getCopy() );
        return boardCopy;

    }
    */

}

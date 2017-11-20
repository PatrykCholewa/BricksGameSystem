package game;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

class Board {

    private Matrix matrix;

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

    Board getCopy() {

        Board boardCopy = new Board( getMatrixSize() );
        boardCopy.setMatrix( matrix.getCopy() );
        return boardCopy;

    }

    private void setValue( Point p , char value) {
        matrix.setValue( p , value );
    }

    private void setValue( Point p , int value) {
        setValue( p , (char)(value + '0') );
    }

    private void setMatrix( Matrix matrix ){
        this.matrix = matrix;
    }

    ArrayList<Point> addRandomBoxes(int numberOfRandomBoxes ){

        Random rndGen = new Random();
        Point box;
        ArrayList<Point> boxList = new ArrayList<>();

        if( matrix.getMatrixSize()*matrix.getMatrixSize() <= numberOfRandomBoxes ){
            throw new IllegalArgumentException( "Board is too small for so many boxes." );
        }

        matrix.clear();

        for( int i = 0 ; i < numberOfRandomBoxes ; ){

            box = new Point( rndGen.nextInt(matrix.getMatrixSize()) , rndGen.nextInt(matrix.getMatrixSize()) );

            if( getValue( box ) != 'X' ) {
                setValue( box , 'X');
                boxList.add(box);
                i++;
            }

        }

        return boxList;

    }

    void addRectangle( Point p1 , Point p2 , Integer playerNumber ) throws IllegalArgumentException {

        setValue( p1 , playerNumber );
        setValue( p2 , playerNumber );

    }

    Character[][] getMatrix() {
        return matrix.getMatrix();
    }

    @Override
    public String toString(){
        return matrix.toString();
    }

}

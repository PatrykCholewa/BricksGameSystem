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

    Character getValue(int row, int column) {
        try {
            return matrix.getValue(row, column);
        } catch ( IndexOutOfBoundsException e ){
            throw new IllegalArgumentException( e.getMessage() );
        }
    }

    private void setValue(int row, int column, char value) {
        matrix.setValue( row , column , value );
    }

    private void setValue(int row, int column, int value) {
        setValue( row , column , (char)(value + '0') );
    }

    ArrayList<Point> setRandomBoxesBoard(int numberOfRandomBoxes ){

        Random rndGen = new Random();
        ArrayList<Point> listOfBoxes = new ArrayList<>();
        Point box;

        if( matrix.getMatrixSize()*matrix.getMatrixSize() <= numberOfRandomBoxes ){
            throw new IllegalArgumentException( "Board is too small for so many boxes." );
        }

        matrix.clear();

        for( int i = 0 ; i < numberOfRandomBoxes ; ){

            box = new Point( rndGen.nextInt(matrix.getMatrixSize()) , rndGen.nextInt(matrix.getMatrixSize()) );

            if( getValue( box.x , box.y ) != 'X' ) {
                setValue( box.x , box.y , 'X');
                i++;
                listOfBoxes.add(box);
            }

        }

        return listOfBoxes;

    }

    void addBoxes(ArrayList<Point> listOfBoxes) {
        for( Point box : listOfBoxes ){
            setValue( box.x , box.y , 'X' );
        }
    }

    void addRectangle( int row1 , int column1 , int row2 , int column2 , Integer playerNumber ) throws IllegalArgumentException {

        setValue( row1 , column1 , playerNumber );
        setValue( row2 , column2 , playerNumber );

    }

    Character[][] getMatrix() {
        return matrix.getMatrix();
    }

    @Override
    public String toString(){
        return matrix.toString();
    }

}

package game;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * @author Patryk Cholewa
 */

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

    ArrayList<Point> setInitialBoxesRandomly(int numberOfRandomBoxes ){

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

        matrix.clear();

        for( Point box : listOfBoxes ){
            matrix.setValue( box, 'X' );
        }

    }

    void addRectangle( Point []boxes ) throws IllegalArgumentException {

        matrix.setValue( boxes[0] , 'P' );
        matrix.setValue( boxes[1] , 'P' );

    }

    Character[][] getMatrix() {
        return matrix.getMatrix();
    }

    @Override
    public String toString(){
        return matrix.toString();
    }

}

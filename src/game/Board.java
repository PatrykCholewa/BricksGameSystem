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

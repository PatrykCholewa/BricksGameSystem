package game;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author Patryk Cholewa
 */

class Board {

    private Matrix matrix;
    private ArrayList<Point> remainingBoxes= new ArrayList<>();

    Board(int size) {
        matrix = new Matrix( size );

        for( int i = 0 ; i < size ; i++ ){
            for( int j = 0 ; j < size ; j++ ){
                remainingBoxes.add( new Point( i , j ) );
            }
        }

    }

    Boolean isFilled(Point p ) {
        try {
            return matrix.getValue( p );
        } catch ( IndexOutOfBoundsException e ){
            throw new IllegalArgumentException( e.getMessage() );
        }
    }

    void setInitialBoxes(ArrayList<Point> listOfBoxes ){

        matrix.clear();

        for( Point box : listOfBoxes ){
            matrix.setTrue( box );
            remainingBoxes.remove( box );
        }

    }

    void addRectangle( Point []boxes ) throws IllegalArgumentException {

        matrix.setTrue( boxes[0] );
        matrix.setTrue( boxes[1] );

        remainingBoxes.remove( boxes[0] );
        remainingBoxes.remove( boxes[1] );

    }

    ArrayList<Point> getRemainingBoxes(){
        return remainingBoxes;
    }

    Boolean[][] getMatrix() {
        return matrix.getMatrix();
    }

    @Override
    public String toString(){
        return matrix.toString();
    }

}

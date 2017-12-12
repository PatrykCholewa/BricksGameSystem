package game;

import java.awt.*;

/**
 * @author Patryk Cholewa
 */

class Matrix {

    private Boolean [][]matrix;
    private int matrixSize;

    Matrix( int size ){
        this.matrixSize = size;
        this.matrix = new Boolean[size][size];
        clear();
    }

    Boolean getValue( Point p ){
        try{
            return matrix[p.x][p.y];
        } catch( IndexOutOfBoundsException e ){
            throw new IndexOutOfBoundsException( "DataCell(" + String.valueOf(p.x) + ";" + String.valueOf(p.y) + ")"
                    + " is outside data matrix " + matrixSizeToString() + "." );
        }
    }

    Boolean [][]getMatrix(){
        return matrix;
    }

    void clear(){
        for(int i = 0; i < matrixSize; i++ ){
            for(int j = 0; j < matrixSize; j++ ){
                matrix[i][j] = false;
            }
        }
    }

    void setTrue(Point p){
        matrix[p.x][p.y] = true;
    }

    private String matrixSizeToString(){
        return String.valueOf(matrixSize) + "x" + String.valueOf(matrixSize);
    }

    @Override
    public String toString(){

        StringBuilder ret = new StringBuilder( matrixSizeToString() );
        for( int i = 0 ; i < matrixSize ; i++ ){

            ret.append("\n");

            for( int j = 0 ; j < matrixSize ; j++ ){
                ret.append(String.valueOf(matrix[i][j]));
            }

        }

        return ret.toString();

    }

}

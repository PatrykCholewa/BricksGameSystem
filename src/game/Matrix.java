package game;

import java.awt.*;

class Matrix {

    private Character [][]matrix;
    private int matrixSize;

    Matrix( int size ){
        this.matrixSize = size;
        this.matrix = new Character[size][size];
        clear();
    }

    int getMatrixSize(){
        return matrixSize;
    }

    Character getValue( Point p ){
        try{
            return matrix[p.x][p.y];
        } catch( IndexOutOfBoundsException e ){
            throw new IndexOutOfBoundsException( "DataCell(" + String.valueOf(p.x) + ";" + String.valueOf(p.y) + ")"
                    + " is outside data matrix " + matrixSizeToString() + "." );
        }
    }

    Character [][]getMatrix(){
        return matrix;
    }

    void clear(){
        for(int i = 0; i < matrixSize; i++ ){
            for(int j = 0; j < matrixSize; j++ ){
                setValue( new Point( i, j ) ,'0' );
            }
        }
    }

    void setValue( Point p , int value) {
        setValue( p , (char)(value + '0' ) );
    }

    void setValue( Point p , char value ){
        if( value < '0' )
            throw new IllegalArgumentException( "Lower than ASCII('0') " );
        matrix[p.x][p.y] = value;
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


    /*
    Matrix getCopy(){

        Matrix mCopy = new Matrix(matrixSize);
        for( int i = 0 ; i < matrixSize ; i++ ){
            for( int j = 0 ; j < matrixSize ;j++ ){
                mCopy.setValue( new Point( i , j ) , matrix[i][j] );
            }
        }

        return mCopy;

    }
    */

}

package game;

class Board {

    private Character [][]matrix;
    private int matrixSize;

    Board( int size ){
        this.matrixSize = size;
        this.matrix = new Character[size][size];
        clear();
    }

    int getMatrixSize(){
        return matrixSize;
    }

    Character getValue( int row , int column ){
        try{
            return matrix[row][column];
        } catch( IndexOutOfBoundsException e ){
            throw new IndexOutOfBoundsException( "DataCell(" + String.valueOf(row) + ";" + String.valueOf(column) + ")"
                    + " is outside data matrix " + matrixSizeToString() + "." );
        }
    }

    Character [][]getMatrix(){
        return matrix;
    }

    private void clear(){
        for(int i = 0; i < matrixSize; i++ ){
            for(int j = 0; j < matrixSize; j++ ){
                setValue(i,j,0);
            }
        }
    }

    void setValue( int row , int column , char value ){
        matrix[row][column] = value;
    }

    void setValue( int row , int column , int value ){
        setValue( row , column , (char)(value + '0') );
    }

    private String matrixSizeToString(){
        return String.valueOf(matrixSize) + "x" + String.valueOf(matrixSize);
    }

    @Override
    public String toString(){

        StringBuilder ret = new StringBuilder("Board " + matrixSizeToString());
        for( int i = 0 ; i < matrixSize ; i++ ){

            ret.append("\n");

            for( int j = 0 ; j < matrixSize ; j++ ){
                ret.append(String.valueOf(matrix[i][j]));
            }

        }

        return ret.toString();

    }

}

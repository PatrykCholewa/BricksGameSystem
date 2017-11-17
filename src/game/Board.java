package game;

class Board {

    private int [][]matrix;
    private int matrixSize;

    Board( int size ){
        this.matrixSize = size;
        this.matrix = new int[size][size];
        clear();
    }

    int [][]getMatrix(){
        return matrix;
    }

    private void clear(){
        for(int i = 0; i < matrixSize; i++ ){
            for(int j = 0; j < matrixSize; j++ ){
                matrix[i][j] = 0;
            }
        }
    }

    void addValue( int row , int column , int value ){

        try{
            matrix[row][column] = value;
        } catch( IndexOutOfBoundsException e ){
            throw new IllegalArgumentException( "DataCell(" + String.valueOf(row) + ";" + String.valueOf(column) + ")"
                    + " is outside data matrix " + matrixSizeToString() + "." );
        }

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

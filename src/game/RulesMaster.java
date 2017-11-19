package game;

class RulesMaster {

    private final static int MAX_NUMBER_OF_PLAYERS = 2;
    private final static int MAX_NUMBER_OF_SIZE = 999;

    private Board board;

    RulesMaster(Board board ){
        this.board = board;
    }

    private void validateAddingBlock(Integer row , Integer column ) throws IndexOutOfBoundsException , IllegalArgumentException {

        try{
            Character val = board.getValue( row , column );
            if( val != '0' ){
                throw new IllegalArgumentException( "Cell("+row+";"+column+") already has a value of " + val + "."  );
            }
        } catch ( IndexOutOfBoundsException e ){
            throw new IndexOutOfBoundsException( e.getMessage() );
        }
    }


    private Boolean isThereZero( int row , int col ){

        Character c;
        try{
            c = board.getValue( row , col );
        } catch ( IllegalArgumentException e ){
            c = 'X';
        }

        return c == '0';

    }

    private Boolean hasANeighbourZero( int row , int col ){

        if( isThereZero( row - 1, col )){
            return true;
        } else if( isThereZero( row + 1, col ) ){
            return true;
        } else if( isThereZero( row , col - 1 ) ){
            return true;
        } else return isThereZero(row, col + 1);
    }

    static void validateSize ( Integer size ){
        if( size < 3 || size > MAX_NUMBER_OF_SIZE || size % 2 == 0 ){
            throw new IllegalArgumentException( "Forbidden value of size of " + size + "." );
        }
    }

    void validateAddingRectangle(Integer row1, Integer column1, Integer row2, Integer column2 , Integer playerNumber )
            throws IllegalArgumentException {

        try {
            validateAddingBlock( row1 , column1 );
            validateAddingBlock( row2 , column2 );
        } catch ( IllegalArgumentException | IndexOutOfBoundsException e ){
            throw new IllegalArgumentException( e.getMessage() );
        }

        if( playerNumber > MAX_NUMBER_OF_PLAYERS ){
            throw new IllegalArgumentException( "Maximum allowed number of players is " + MAX_NUMBER_OF_PLAYERS + "!" );
        }

        if( row1 != row2 + 1 && row1 != row2 - 1 && column1 != column2 + 1 && column1 != column2 - 1 ){
            throw new IllegalArgumentException( "Blocks (" + row1 + ";" + column1 + ") and ("
                    + row2 + ";" + column2 + ") are not neighbours!" );
        }

    }

    Boolean isFinished(){

        for( int i = 0 ; i < board.getMatrixSize() ; i++ ){
            for( int j = 0 ; j < board.getMatrixSize() ; j++ ){
                if( board.getValue( i , j ) == '0' && hasANeighbourZero( i , j ) ){
                    return false;
                }
            }
        }

        return true;

    }

}

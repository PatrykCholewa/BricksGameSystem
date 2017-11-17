package game;

public class Analyzer {

    private Board board;

    Analyzer(Board board ){
        this.board = board;
    }

    private void validateAdding(Integer row , Integer column ) throws IndexOutOfBoundsException , IllegalArgumentException {

        try{
            Character val = board.getValue( row , column );
            if( val != '0' ){
                throw new IllegalArgumentException( "Cell("+row+";"+column+") already has a value of " + val + "."  );
            }
        } catch ( IndexOutOfBoundsException e ){
            throw new IndexOutOfBoundsException( e.getMessage() );
        }
    }

    void validateAdding(Integer row1, Integer column1, Integer row2, Integer column2 , Integer playerNumber )
            throws IllegalArgumentException {

        try {
            validateAdding( row1 , column1 );
            validateAdding( row2 , column2 );
        } catch ( IllegalArgumentException | IndexOutOfBoundsException e ){
            throw new IllegalArgumentException( e.getMessage() );
        }

        if( playerNumber != 1 && playerNumber != 2 ){
            throw new IllegalArgumentException( "There are only two players!" );
        }

        if( row1 != row2 + 1 && row1 != row2 - 1 && column1 != column2 + 1 && column1 != column2 - 1 ){
            throw new IllegalArgumentException( "Blocks (" + row1 + ";" + column1 + ") and ("
                    + row2 + ";" + column2 + ") are not neighbours!" );
        }

    }

}

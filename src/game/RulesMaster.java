package game;

import java.awt.*;

class RulesMaster {

    private final static int MAX_NUMBER_OF_PLAYERS = 2;
    private final static int MAX_NUMBER_OF_SIZE = 999;

    private Board board;

    RulesMaster(Board board ){
        this.board = board;
    }

    private void validateAddingBlock( Point p ) throws IndexOutOfBoundsException , IllegalArgumentException {

        try{
            Character val = board.getValue( p );
            if( val != '0' ){
                throw new IllegalArgumentException( "Cell("+p.x+";"+p.y+") already has a value of " + val + "."  );
            }
        } catch ( IndexOutOfBoundsException e ){
            throw new IndexOutOfBoundsException( e.getMessage() );
        }
    }


    private Boolean isThereZero( Point p ){

        Character c;
        try{
            c = board.getValue( p );
        } catch ( IllegalArgumentException e ){
            c = 'X';
        }

        return c == '0';

    }

    private Boolean hasANeighbourZero( Point p ){

        if( isThereZero( new Point( p.x - 1 , p.y ) )){
            return true;
        } else if( isThereZero( new Point( p.x + 1 , p.y ) ) ){
            return true;
        } else if( isThereZero( new Point( p.x , p.y - 1 ) ) ){
            return true;
        } else return isThereZero( new Point( p.x , p.y + 1 ) );
    }

    static void validateSize ( Integer size ){
        if( size < 3 || size > MAX_NUMBER_OF_SIZE || size % 2 == 0 ){
            throw new IllegalArgumentException( "Forbidden value of size of " + size + "." );
        }
    }

    void validateAddingRectangle( Point p1 , Point p2 , Integer playerNumber )
            throws IllegalArgumentException {

        try {
            validateAddingBlock( p1 );
            validateAddingBlock( p2 );
        } catch ( IllegalArgumentException | IndexOutOfBoundsException e ){
            throw new IllegalArgumentException( e.getMessage() );
        }

        if( playerNumber > MAX_NUMBER_OF_PLAYERS ){
            throw new IllegalArgumentException( "Maximum allowed number of players is " + MAX_NUMBER_OF_PLAYERS + "!" );
        }

        if( p1.x != p2.x + 1 && p1.x != p2.x - 1 && p1.y != p2.y + 1 && p1.y != p2.y - 1 ){
            throw new IllegalArgumentException( "Blocks (" + p1.x + ";" + p1.y + ") and ("
                    + p2.x + ";" + p2.y + ") are not neighbours!" );
        }

    }

    Boolean isFinished(){

        for( int i = 0 ; i < board.getMatrixSize() ; i++ ){
            for( int j = 0 ; j < board.getMatrixSize() ; j++ ){
                if( board.getValue( new Point( i , j ) ) == '0' && hasANeighbourZero( new Point( i , j ) ) ){
                    return false;
                }
            }
        }

        return true;

    }

}

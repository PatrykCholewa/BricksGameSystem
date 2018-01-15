package game;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author Patryk Cholewa
 */

class Rules {

    private final static int MIN_NUMBER_OF_SIZE = 3;
    private final static int MAX_NUMBER_OF_SIZE = 999;

    private Board board;
    private ArrayList<Point> remainingToCheck;

    Rules( Board board ){
        this.board = board;
    }

    private void validateAddingBlock( Point p ) throws IndexOutOfBoundsException , IllegalArgumentException {

        try{
            Boolean val = board.isFilled( p );
            if(val){
                throw new IllegalArgumentException( "Cell("+p.x+";"+p.y+") already has a value of " + val + "."  );
            }
        } catch ( IndexOutOfBoundsException e ){
            throw new IndexOutOfBoundsException( e.getMessage() );
        }
    }


    private Boolean isThereZero( Point p ){

        Boolean b;
        try{
            b = board.isFilled( p );
        } catch ( IllegalArgumentException e ){
            b = true;
        }

        return !b;

    }

    private Boolean hasANeighbourZero( Point p ){

        if( isThereZero( new Point( p.x - 1 , p.y ) )){
            return true;
        } else if( isThereZero( new Point( p.x + 1 , p.y ) ) ){
            return true;
        } else if( isThereZero( new Point( p.x , p.y - 1 ) ) ){
            return true;
        } else if( isThereZero( new Point( p.x , p.y + 1 ) ) ){
            return true;
        }

        remainingToCheck.remove( p );
        remainingToCheck.remove( new Point( p.x - 1 , p.y ));
        remainingToCheck.remove( new Point( p.x + 1 , p.y ));
        remainingToCheck.remove( new Point( p.x , p.y - 1 ));
        remainingToCheck.remove( new Point( p.x , p.y + 1 ));

        return false;

    }



    static void validateSize ( Integer size ){
        if( size < MIN_NUMBER_OF_SIZE || size > MAX_NUMBER_OF_SIZE || size % 2 == 0 ){
            throw new IllegalArgumentException( "Forbidden value of size of " + size + "." );
        }
    }

    void validateAddingRectangle( Point []boxes )
            throws IllegalArgumentException {

        try {
            validateAddingBlock( boxes[0] );
            validateAddingBlock( boxes[1] );
        } catch ( IllegalArgumentException | IndexOutOfBoundsException e ){
            throw new IllegalArgumentException( e.getMessage() );
        }

        if( boxes[0].x != boxes[1].x + 1 &&
                boxes[0].x != boxes[1].x - 1 &&
                boxes[0].y != boxes[1].y + 1 &&
                boxes[0].y != boxes[1].y - 1 ){
            throw new IllegalArgumentException( "Blocks (" + boxes[0].x + ";" + boxes[0].y + ") and ("
                    + boxes[1].x + ";" + boxes[1].y + ") are not neighbours!" );
        }

    }

    Boolean isFinished(){

        ArrayList<Point> remainingBoxes = board.getRemainingBoxes();
        remainingToCheck = new ArrayList<>();
        remainingToCheck.addAll( remainingBoxes );

        while( remainingToCheck.size() > 0 ){
            if( hasANeighbourZero( remainingToCheck.get(0) ) ) {
                return false;
            }

        }

        return true;

    }

}

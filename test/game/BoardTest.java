package game;

import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @Test
    void setInitialBoxes() {

        Board board = new Board(3);
        ArrayList<Point> list = new ArrayList<>();
        Boolean [][]expectedMatrix = new Boolean[][]{
                {true ,false,true } ,
                {false,true ,false} ,
                {true ,false,true }
        };

        list.add( new Point(0,0) );
        list.add( new Point(0,2) );
        list.add( new Point(1,1) );
        list.add( new Point(2,0) );
        list.add( new Point(2,2) );


        board.setInitialBoxes( list );

        assertArrayEquals( expectedMatrix , board.getMatrix() );

    }

    @Test
    void getMatrix() {

        Board board = new Board( 3 );

        board.addRectangle( new Point[]{ new Point(0,0) , new Point(0,1) }  );
        board.addRectangle( new Point[]{ new Point(1,1) , new Point(1,0) }  );

        assertArrayEquals( new Boolean[][]{ {true,true,false} , {true,true,false} , {false,false,false} } , board.getMatrix() );

    }

}
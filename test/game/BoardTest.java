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
        Character [][]expectedMatrix = new Character[][]{
                {'X','0','X'} ,
                {'0','X','0'} ,
                {'X','0','X'}
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

        assertArrayEquals( new Character[][]{ {'P','P','0'} , {'P','P','0'} , {'0','0','0'} } , board.getMatrix() );

    }

}
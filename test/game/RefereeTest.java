package game;

import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class RefereeTest {

    @Test
    void addRectangle() {

        Referee gm = new Referee(3);

        gm.addRectangle( new Point[]{ new Point(0, 0), new Point(0, 1) } );

        assertThrows(IllegalArgumentException.class,
                () -> gm.addRectangle( new Point[]{ new Point(0, 0), new Point(0, 1) } ) );
        assertThrows(IllegalArgumentException.class,
                () -> gm.addRectangle( new Point[]{ new Point(0, 0), new Point(1, 1) } ) );
        assertThrows(IllegalArgumentException.class,
                () -> gm.addRectangle( new Point[]{ new Point(0, 1), new Point(1, 1) } ) );
        assertThrows(IllegalArgumentException.class,
                () -> gm.addRectangle( new Point[]{ new Point(0, 0), new Point(1, 0) } ) );
        assertThrows(IllegalArgumentException.class,
                () -> gm.addRectangle( new Point[]{ new Point(1, -1), new Point(1, 0) } ) );
        assertThrows(IllegalArgumentException.class,
                () -> gm.addRectangle( new Point[]{ new Point(2, 0), new Point(3, 0) } ) );

        gm.addRectangle( new Point[]{ new Point(1, 1), new Point(1, 0) } );

    }

    @Test
    void isFinished() {

        Referee gm = new Referee( 3 );
        assertFalse( gm.isFinished() );

        gm.addRectangle( new Point[]{ new Point(0,0) , new Point(0,1) } );
        assertFalse( gm.isFinished() );

        gm.addRectangle( new Point[]{ new Point(0,2) , new Point(1,2) } );
        assertFalse( gm.isFinished() );

        gm.addRectangle( new Point[]{ new Point(2,2) , new Point(2,1) } );
        assertFalse( gm.isFinished() );

        gm.addRectangle( new Point[]{ new Point(2,0) , new Point(1,0) } );
        assertTrue( gm.isFinished() );

    }

    @Test
    void setInitialBoxes(){

        Referee gm = new Referee(3);
        ArrayList<Point> list = new ArrayList<>();

        list.add( new Point(0,0) );
        list.add( new Point(0,2) );
        list.add( new Point(1,1) );
        list.add( new Point(2,0) );
        list.add( new Point(2,2) );

        gm.setInitialBoxes( list );

        assertTrue( gm.isFinished() );

    }

}
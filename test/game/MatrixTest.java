package game;

import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class MatrixTest {

    @Test
    void getMatrix() {

        Matrix matrix = new Matrix( 2 );

        matrix.setTrue( new Point(0,0) );
        matrix.setTrue( new Point(1,1) );

        assertArrayEquals( new Boolean[][]{ {true,false} , {false,true} } , matrix.getMatrix() );

    }

}
package game;

import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class MatrixTest {

    @Test
    void getMatrix() {

        Matrix matrix = new Matrix( 2 );

        matrix.setValue( new Point(0,0) , '1' );
        matrix.setValue( new Point(1,1) , '3' );

        assertArrayEquals( new Character[][]{ {'1','0'} , {'0','3'} } , matrix.getMatrix() );

    }

}
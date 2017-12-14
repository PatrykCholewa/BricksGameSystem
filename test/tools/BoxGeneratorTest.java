package tools;

import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BoxGeneratorTest {

    @Test
    void generateBoxes() {

        ArrayList<Point> boxes = BoxGenerator.generateBoxes( 7  , 17 );
        assertEquals( 17 , boxes.size() );

        Boolean [][]table = new Boolean[][]{
                { false , false , false , false , false , false , false } ,
                { false , false , false , false , false , false , false } ,
                { false , false , false , false , false , false , false } ,
                { false , false , false , false , false , false , false } ,
                { false , false , false , false , false , false , false } ,
                { false , false , false , false , false , false , false } ,
                { false , false , false , false , false , false , false }
        };

        for( Point box : boxes ){
            table[box.x][box.y] = true;
        }

        System.out.println( "GENERATE RANDOM ( 17 )" );

        for ( Boolean []row : table) {
            for( Boolean box : row ){
                if( box ){
                    System.out.printf( "X" );
                } else {
                    System.out.printf( "O" );
                }
            }
            System.out.printf( "\n" );
        }

        assertThrows( IllegalArgumentException.class , ()->BoxGenerator.generateBoxes( 7 , 49 ) );

    }

}
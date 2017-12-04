package tools;

import org.junit.jupiter.api.Test;
import tools.Translator;

import java.awt.*;
import java.net.ProtocolException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TranslatorTest {

    @Test
    void boxesFromInitString(){

        ArrayList<Point> listOfBoxes = new ArrayList<>();

        assertEquals( listOfBoxes , Translator.boxesFromInitString( "3" ) );

        listOfBoxes.add( new Point( 0 , 0 ) );
        listOfBoxes.add( new Point( 1 , 2 ) );

        assertEquals( listOfBoxes , Translator.boxesFromInitString( "3_0x0_1x2" ) );

        listOfBoxes.add( new Point( 2 , 1 ) );

        assertEquals( listOfBoxes , Translator.boxesFromInitString( "3_0x0_1x2_2x1" ) );

    }

    @Test
    void getSizeFromInitString(){

        assertEquals( 3 , Translator.getSizeFromInitString( "3" ) );
        assertEquals( 6 , Translator.getSizeFromInitString( "6_0x0_1x2" ) );
        assertEquals( 11 , Translator.getSizeFromInitString( "11_0x0_1x2_2x1" ) );

    }

    @Test
    void initToString() {

        ArrayList<Point> listOfBoxes = new ArrayList<>();

        assertEquals( "3" , Translator.initToString( 3 , listOfBoxes ) );

        listOfBoxes.add( new Point( 0 , 0 ) );
        listOfBoxes.add( new Point( 1 , 2 ) );

        assertEquals( "3_0x0_1x2" , Translator.initToString( 3 , listOfBoxes ) );

        listOfBoxes.add( new Point( 2 , 1 ) );

        assertEquals( "3_0x0_1x2_2x1" , Translator.initToString( 3 , listOfBoxes ) );

    }

    @Test
    void rectangleToString() {

        ArrayList<Point> rectangle = new ArrayList<>();
        rectangle.add( new Point( 0 , 0 ) );
        rectangle.add( new Point( 0 , 2 ) );

        assertEquals( "0x0_0x2" , Translator.rectangleToString( rectangle ) );

    }

    @Test
    void stringToBoxPair() throws ProtocolException {

        ArrayList<Point> rectangle = new ArrayList<>();
        rectangle.add( new Point( 0 , 0 ) );
        rectangle.add( new Point( 0 , 2 ) );

        assertArrayEquals( rectangle.toArray() , Translator.stringToBoxPair( "0x0_0x2" ) );

        assertThrows( ProtocolException.class , ()->Translator.stringToBoxPair( "" ) );
        assertThrows( ProtocolException.class , ()->Translator.stringToBoxPair( "1" ) );
        assertThrows( ProtocolException.class , ()->Translator.stringToBoxPair( "1x1x2" ) );
        assertThrows( ProtocolException.class , ()->Translator.stringToBoxPair( "1_1x2" ) );
        assertThrows( ProtocolException.class , ()->Translator.stringToBoxPair( "1x2_1" ) );
        assertThrows( ProtocolException.class , ()->Translator.stringToBoxPair( "_1" ) );
        assertThrows( ProtocolException.class , ()->Translator.stringToBoxPair( "1x2_" ) );
        assertThrows( ProtocolException.class , ()->Translator.stringToBoxPair( "2_1x2" ) );
        assertThrows( ProtocolException.class , ()->Translator.stringToBoxPair( "_" ) );
        assertThrows( ProtocolException.class , ()->Translator.stringToBoxPair( "1_1x2" ) );
        assertThrows( ProtocolException.class , ()->Translator.stringToBoxPair( "x2_2x2" ) );

    }

}
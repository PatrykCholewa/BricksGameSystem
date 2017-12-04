package tools;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.ProtocolException;

import static org.junit.jupiter.api.Assertions.*;

class InfoReaderTest {

    @Test
    void getLines() throws FileNotFoundException, ProtocolException {

        assertThrows( FileNotFoundException.class , ()->InfoReader.getLines( new File ( "." ) ) );
        assertThrows( ProtocolException.class , ()->InfoReader.getLines( new File( "./test/testFiles/infoFiles/blankFile" ) ) );
        assertThrows( ProtocolException.class , ()->InfoReader.getLines( new File( "./test/testFiles/infoFiles/oneLineFile" ) ) );

        String []gotLines = InfoReader.getLines( new File( "./test/testFiles/infoFiles/okFile"  ));
        assertEquals( "command" , gotLines[0] );
        assertEquals( "nick" , gotLines[1] );
        assertEquals( 2 , gotLines.length );


    }

}
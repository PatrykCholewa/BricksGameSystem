package archiving;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.ProtocolException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class LogReaderTest {

    private final static File logSample = new File( "./test/testFiles/logFiles/logSample.txt" );

    @Test
    void constructor() throws FileNotFoundException, ProtocolException {

        LogReader logReader = new LogReader( logSample );

        assertEquals( 3 , logReader.size );
        assertEquals( "PREDEFINED OUTPUT FINISH1" , logReader.startingPlayer );
        assertEquals( "PREDEFINED OUTPUT FINISH2" , logReader.followingPlayer );
        assertEquals( "3_1x1" , logReader.initData );
        assertEquals( "PREDEFINED OUTPUT FINISH2" , logReader.winner );

        assertTrue( logReader.finishMessage.contains( "normally" ) );

        ArrayList<String> moves = new ArrayList<>();
        moves.add( "0x0_0x1" );
        moves.add( "0x2_1x2" );
        moves.add( "2x2_2x1" );
        moves.add( "2x0_1x0" );

        assertEquals( moves , logReader.getMoves() );

    }

}
package archiving;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.ProtocolException;

import static org.junit.jupiter.api.Assertions.*;

class DuelShowTest {

    private final static File logSample = new File( "./test/testFiles/logFiles/logSample.txt" );


    @Test
    void getWinner() throws ProtocolException, FileNotFoundException {

        DuelShow duelShow = new DuelShow( logSample );
        duelShow.start();
        duelShow.nextMove();
        duelShow.nextMove();
        duelShow.nextMove();
        assertEquals( "PREDEFINED OUTPUT FINISH2" , duelShow.getWinner() );

    }

    @Test
    void isFinished() throws ProtocolException, FileNotFoundException {

        DuelShow duelShow = new DuelShow( logSample );
        duelShow.start();
        assertFalse( duelShow.isFinished() );
        duelShow.nextMove();
        assertFalse( duelShow.isFinished() );
        duelShow.nextMove();
        assertFalse( duelShow.isFinished() );
        duelShow.nextMove();
        assertTrue( duelShow.isFinished() );

        duelShow.start();
        assertFalse( duelShow.isFinished() );

    }

    @Test
    void getLastMove() throws FileNotFoundException, ProtocolException {

        DuelShow duelShow = new DuelShow( logSample );
        duelShow.start();
        assertEquals( "0x0_0x1" , duelShow.lastMove() );
        duelShow.nextMove();
        assertEquals( "0x2_1x2" , duelShow.lastMove() );
        duelShow.nextMove();
        assertEquals( "2x2_2x1" , duelShow.lastMove() );
        duelShow.nextMove();
        assertEquals( "2x0_1x0" , duelShow.lastMove() );

        duelShow.start();
        assertEquals( "0x0_0x1" , duelShow.lastMove() );

    }

    @Test
    void getMessage() throws FileNotFoundException, ProtocolException {

        DuelShow duelShow = new DuelShow( logSample );
        duelShow.start();
        assertEquals( "OK" , duelShow.getMessage() );
        duelShow.nextMove();
        assertEquals( "OK" , duelShow.getMessage() );
        duelShow.nextMove();
        assertEquals( "OK" , duelShow.getMessage() );
        duelShow.nextMove();
        assertTrue( duelShow.getMessage().contains( "normally") );

        duelShow.start();
        assertEquals( "OK" , duelShow.getMessage() );

    }

}
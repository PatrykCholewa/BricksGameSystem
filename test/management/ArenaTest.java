package management;

import org.junit.jupiter.api.Test;
import processes.Court;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ProtocolException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ArenaTest {

    final static File get1Dir = new File( "./test/testFiles/predefinedOutputs/Get_Finish1" );
    final static File get2Dir = new File( "./test/testFiles/predefinedOutputs/Get_Finish2" );
    final static File logWriteFile = new File( "./test/testFiles/logFiles/logWrite.txt" );
    final static File logSample = new File( "./test/testFiles/logFiles/logWrite.txt" );

    @Test
    void finish() throws IOException {

        logWriteFile.createNewFile();

        Arena arena = new Arena( get1Dir , get2Dir );
        arena.setLogFile( logWriteFile );

        ArrayList<String> moves = new ArrayList<>();
        moves.add( "0x2_1x2" );
        moves.add( "2x2_2x1" );
        moves.add( "2x0_1x0" );

        arena.setBoard( "3_1x1" );

        arena.start();

        assertEquals( moves , arena.finish() );
        assertTrue( arena.isFinished() );
        assertEquals( 0 , logSample.compareTo( logWriteFile ) );

        logWriteFile.delete();

    }

}
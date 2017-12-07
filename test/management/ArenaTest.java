package management;

import org.junit.jupiter.api.Test;
import processes.Court;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.ProtocolException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ArenaTest {

    final static File get1Dir = new File( "./test/testFiles/predefinedOutputs/Get_Finish1" );
    final static File get2Dir = new File( "./test/testFiles/predefinedOutputs/Get_Finish2" );
    final static File logWriteFile = new File( "./test/testFiles/logFiles/logWrite.txt" );

    @Test
    void finish() throws FileNotFoundException, ProtocolException {

        Arena arena = new Arena( get1Dir , get2Dir );
        arena.setLogFile( logWriteFile );
        arena.setBoard( 3 , new ArrayList<>() );
        arena.start();
        arena.finish();

        assertTrue( arena.isFinished() );
    }

    @Test
    void setLogFile() {



    }

}
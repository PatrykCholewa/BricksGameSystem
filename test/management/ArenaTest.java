package management;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import testTools.FileComparator;

import java.io.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ArenaTest {

    private final static File get1Dir = new File( "./test/testFiles/predefinedOutputs/Get_Finish1" );
    private final static File get2Dir = new File( "./test/testFiles/predefinedOutputs/Get_Finish2" );
    private final static File logWriteFile = new File( "./test/testFiles/logFiles/logWrite.txt" );
    private final static File logSample = new File( "./test/testFiles/logFiles/logSample.txt" );

    @Test
    void finish() throws IOException {

        logWriteFile.createNewFile();

        Arena arena = new Arena( get1Dir , get2Dir );
        arena.setLogFile( logWriteFile );

        arena.setBoard( "3_1x1" );

        arena.start();
        arena.finish();

        assertTrue( arena.isFinished() );
        FileComparator.compare( logSample , logWriteFile );

        logWriteFile.deleteOnExit();

    }

}
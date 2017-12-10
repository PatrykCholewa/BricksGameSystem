package tournaments;

import org.junit.jupiter.api.Test;
import testTools.FileComparator;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class TournamentScoreTest {

    private final static File dir = new File( "./test/testFiles/tourFiles" );
    private final static File duelWriteFile = new File( "./test/testFiles/tourFiles/duels.txt" );
    private final static File duelSample1 = new File( "./test/testFiles/tourFiles/duelListFiles/sample1.txt" );
    private final static File tourWriteFile = new File( "./test/testFiles/tourFiles/score.txt" );
    private final static File tourSample1 = new File( "./test/testFiles/tourFiles/scoreFiles/sample1.txt" );

    @Test
    void addNewDuel() throws IOException {

        TournamentScore ts = new TournamentScore( dir );

        ts.addNewDuel( "P1" , "P2" , "P2" );
        ts.addNewDuel( "P1" , "P3" , "P3" );
        ts.addNewDuel( "P2" , "P1" , "P2" );
        ts.addNewDuel( "P2" , "P3" , "P2" );
        ts.addNewDuel( "P3" , "P1" , "P1" );
        ts.addNewDuel( "P3" , "P2" , "P2" );

        ts.close();

        FileComparator.compare( duelSample1 , duelWriteFile );
        FileComparator.compare( tourSample1 , tourWriteFile );

        duelWriteFile.deleteOnExit();
        tourWriteFile.deleteOnExit();

    }

}
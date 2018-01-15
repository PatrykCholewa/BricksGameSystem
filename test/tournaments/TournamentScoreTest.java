package tournaments;

import enums.FailureReason;
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
    private final static File tourSample2 = new File( "./test/testFiles/tourFiles/scoreFiles/sample2.txt" );
    private final static File errWriteFile = new File( "./test/testFiles/tourFiles/err.txt" );

    @Test
    void addNewDuel() throws IOException {

        TournamentScore ts = new TournamentScore( dir );

        ts.addNewDuel( "P1" , "P2" , "P2" , "Reason1" , FailureReason.NORMAL);
        ts.addNewDuel( "P1" , "P3" , "P3" , "Reason1" , FailureReason.INVALIDMOVE );
        ts.addNewDuel( "P2" , "P1" , "P2" , "Reason1" , FailureReason.CANTSTART );
        ts.addNewDuel( "P2" , "P3" , "P2" , "Reason1" , FailureReason.DEADLOCK );
        ts.addNewDuel( "P3" , "P1" , "P1" , "Reason1" , FailureReason.OUTOFTIMEMOVE );
        ts.addNewDuel( "P3" , "P2" , "P2" , "Reason1" , FailureReason.PROTOCOLERROR );

        ts.close();

        FileComparator.compare( duelSample1 , duelWriteFile );
        FileComparator.compare( tourSample2 , tourWriteFile );

        duelWriteFile.deleteOnExit();
        errWriteFile.deleteOnExit();
        tourWriteFile.deleteOnExit();

    }

}
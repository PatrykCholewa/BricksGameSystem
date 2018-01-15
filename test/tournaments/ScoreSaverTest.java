package tournaments;

import enums.FailureReason;
import org.junit.jupiter.api.Test;
import testTools.FileComparator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ScoreSaverTest {

    private final static File dir = new File ( "./test/testFiles/tourFiles/scoreFiles" );
    private final static File writeFile = new File( "./test/testFiles/tourFiles/scoreFiles/writeFile.txt" );
    private final static File sample1 = new File( "./test/testFiles/tourFiles/scoreFiles/sample1.txt" );

    @Test
    void updateScore() throws IOException {

        writeFile.createNewFile();

        ScoreSaver scoreSaver = new ScoreSaver( writeFile );

        ArrayList<ScoreDBRecord> players = new ArrayList<>();
        players.add( new ScoreDBRecord( "P1" ) );
        players.add( new ScoreDBRecord( "P2" ) );
        players.add( new ScoreDBRecord( "P3" ) );

        players.get( 0 ).addOnePointToAllWins();
        players.get( 0 ).addOnePointToNormalWins();
        for( int i = 0 ; i < 3 ; i++ ) players.get( 0 ).addOnePointToAllLoses();
        players.get( 0 ).addErrorFailure( FailureReason.OUTOFTIMEMOVE );

        for( int i = 0 ; i < 4 ; i++ ) players.get( 1 ).addOnePointToAllWins();
        for( int i = 0 ; i < 1 ; i++ ) players.get( 1 ).addOnePointToNormalWins();
        for( int i = 0 ; i < 1 ; i++ ) players.get( 1 ).addOnePointToAllLoses();
        //players.get( 1 ).addErrorFailure( FailureReason.OUTOFTIMEMOVE );

        for( int i = 0 ; i < 1 ; i++ ) players.get( 2 ).addOnePointToAllWins();
        //for( int i = 0 ; i < 0 ; i++ ) players.get( 2 ).addOnePointToNormalWins();
        for( int i = 0 ; i < 3 ; i++ ) players.get( 2 ).addOnePointToAllLoses();
        players.get( 2 ).addErrorFailure( FailureReason.INVALIDMOVE );
        players.get( 2 ).addErrorFailure( FailureReason.CANTSTART );
        players.get( 2 ).addErrorFailure( FailureReason.DEADLOCK );

        ArrayList<ScoreDBRecord> clone = (ArrayList<ScoreDBRecord>) players.clone();

        scoreSaver.updateScore( players );

        assertEquals( clone , players );
        FileComparator.compare( sample1 , writeFile );

        writeFile.deleteOnExit();

    }

}
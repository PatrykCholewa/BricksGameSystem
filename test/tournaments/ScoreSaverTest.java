package tournaments;

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

        players.get( 0 ).addOnePoint();
        for( int i = 0 ; i < 4 ; i++ ) players.get( 1 ).addOnePoint();
        players.get( 2 ).addOnePoint();

        ArrayList<ScoreDBRecord> clone = (ArrayList<ScoreDBRecord>) players.clone();

        scoreSaver.updateScore( players );

        assertEquals( clone , players );
        FileComparator.compare( sample1 , writeFile );

        writeFile.deleteOnExit();

    }

}
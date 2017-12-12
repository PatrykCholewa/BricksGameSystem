package management;

import org.junit.jupiter.api.Test;
import testTools.FileComparator;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TournamentTest {

    private final static File playersDir = new File( "./test/testFiles/tourTest/players" );
    private final static File resultsDir = new File( "./test/testFiles/tourTest/results" );
    private final static File writeScore = new File( "./test/testFiles/tourTest/results/score.txt" );
    private final static File writeErr = new File( "./test/testFiles/tourTest/results/err.txt" );
    private final static File writeDuels = new File( "./test/testFiles/tourTest/results/duels.txt" );
    private final static File scoreSample = new File( "./test/testFiles/tourTest/samples/score.txt" );

    private void deleteDirectory( File dir ){
        for( File file : dir.listFiles() ){
            if( file.isDirectory() ){
                deleteDirectory( file );
                file.delete();
            } else {
                file.delete();
            }
        }

    }

    @Test
    void start() throws IOException {

        resultsDir.mkdir();

        Tournament tournament = new Tournament( playersDir , resultsDir );
        tournament.start( 3,new ArrayList<Point>());

        System.out.println( "SCORE" );
        System.out.println( "-----------------------");
        FileComparator.writeOut( writeScore );

        System.out.println();
        System.out.println( "DUELS" );
        System.out.println( "-----------------------");
        FileComparator.writeOut( writeDuels );

        System.out.println();
        System.out.println( "ERR" );
        System.out.println( "-----------------------");
        FileComparator.writeOut( writeErr );
        System.out.println();
        System.out.println();

        FileComparator.compare( scoreSample , writeScore );

        deleteDirectory( resultsDir );

    }

}
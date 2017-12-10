package tournaments;

import org.junit.jupiter.api.Test;
import testTools.FileComparator;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class DuelSaverTest {

    private final static File dir = new File ( "./test/testFiles/tourFiles/duelListFiles" );
    private final static File writeFile = new File( "./test/testFiles/tourFiles/duelListFiles/writeFile.txt" );
    private final static File sample1 = new File( "./test/testFiles/tourFiles/duelListFiles/sample1.txt" );

    @Test
    void addDuel() throws IOException {

        writeFile.createNewFile();

        DuelSaver ds = new DuelSaver( writeFile );

        ds.addDuel( 0 , "P1" , "P2" , "P2" );
        ds.addDuel( 1 , "P1" , "P3" , "P3" );
        ds.addDuel( 2 , "P2" , "P1" , "P2" );
        ds.addDuel( 3 , "P2" , "P3" , "P2" );
        ds.addDuel( 4 , "P3" , "P1" , "P1" );
        ds.addDuel( 5 , "P3" , "P2" , "P2" );
        ds.close();

        FileComparator.compare( sample1 , writeFile );

        writeFile.deleteOnExit();

    }

}
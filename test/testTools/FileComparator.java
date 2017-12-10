package testTools;

import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileComparator {

    @Test
    public static void compare( File sample , File tested ) throws IOException {
        BufferedReader sbr = new BufferedReader( new FileReader( sample ) );
        BufferedReader tbr = new BufferedReader( new FileReader( tested ) );

        String sampleLine;
        while( (sampleLine = sbr.readLine()) != null ){
            assertEquals( sampleLine , tbr.readLine() );
        }

        assertTrue( tbr.readLine() == null );

        sbr.close();
        tbr.close();

    }

    public static void writeOut( File file ) throws IOException{
        BufferedReader br = new BufferedReader( new FileReader( file ) );

        String line;
        while( (line = br.readLine()) != null ){
            System.out.println( line );
        }
        br.close();

    }

}

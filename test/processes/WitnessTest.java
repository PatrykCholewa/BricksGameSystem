package processes;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class WitnessTest {

    @AfterEach
    void tearDown() {
        witness.destroy();
    }

    private Witness witness;


    @Test
    synchronized void  constructor() throws IOException, InterruptedException {

        File dir = new File( "./test/testFiles/mainDir/virusDir" );
        witness = new Witness( dir );
        Scanner scanner = new Scanner( witness.getInputStream() );
        BufferedWriter bw = new BufferedWriter( new OutputStreamWriter( witness.getOutputStream() ) );

        assertEquals( "PROCESS START TEST" , witness.getNick() );

        bw.write( "OK" );

        assertTrue( witness.isAlive() );

        double time = System.currentTimeMillis();
        while( !scanner.hasNext() ){
            if( !witness.isAlive() ){
                System.out.println( "Died after " +  (System.currentTimeMillis() - time) + "ms." );
                break;
            }
            if( System.currentTimeMillis() - time > 1000 ){
                fail( "TOO LONG" );
            }
        }

        assertTrue( witness.isAlive() );
        assertEquals( "OK" , scanner.nextLine() );

    }

}
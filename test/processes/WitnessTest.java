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

        File dir = new File( "./test/testFiles/mainDir/onlyTypeOkDir" );
        witness = new Witness( dir );
        Scanner scanner = new Scanner( witness.getInputStream() );

        assertEquals( "TEST TYPE OK" , witness.getNick() );
        assertTrue( witness.isAlive() );

        wait(1000);

        assertEquals( "OK" , scanner.nextLine() );
        witness.destroy();

    }

}
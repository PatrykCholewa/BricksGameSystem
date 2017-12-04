package processes;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class CommanderTest {

    Commander virusCommander;
    Commander okCommander;

    @AfterEach
    void tearDown(){

        try {
            virusCommander.killProcess();
        } catch ( NullPointerException e ){
            ;
        }

        try {
            okCommander.killProcess();
        } catch ( NullPointerException e ){
            ;
        }
    }

    private void setVirusCommander() throws IOException {
        virusCommander = new Commander( new File( "./test/testFiles/mainDir/virusDir" ) );
        virusCommander.initProcess();
    }

    private void setOkCommander() throws IOException {
        okCommander = new Commander( new File( "./test/testFiles/mainDir/onlyTypeOkDir" ) );
        okCommander.initProcess();
    }

    @Test
    void getWitnessNick() throws IOException {

        setOkCommander();
        Commander commander = okCommander;

        assertEquals( "TEST TYPE OK" , commander.getNick() );
        commander.killProcess();

    }

    @Test
    synchronized void hasOutputLine() throws IOException, InterruptedException {
        setOkCommander();
        Commander commander = okCommander;

        wait( 50);
        assertTrue( commander.hasInput() );
    }

    @Test
    synchronized void getOutputLine() throws IOException, InterruptedException {

        setOkCommander();
        Commander commander = okCommander;

        wait( 50);
        assertEquals( "OK" , commander.getInputLine() );

    }

    @Test
    synchronized void tellInputLine() throws IOException, InterruptedException {

        setVirusCommander();
        Commander commander = virusCommander;

        commander.insertOutputLine( "TEST" );
        wait( 500 );
        assertEquals( "TEST" , commander.getInputLine() );

        commander.insertOutputLine( "TEST2" );
        wait( 500 );
        assertEquals( "TEST2" , commander.getInputLine() );

        commander.killProcess();

    }

}
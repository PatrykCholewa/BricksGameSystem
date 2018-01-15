package processes;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;

class TrialTest {

    final static File []dirs = new File[]{
            new File("./test/testFiles/predefinedOutputs/Get_Finish1"),
            new File("./test/testFiles/predefinedOutputs/Get_Finish2")
    };

    @Test
    void getStartingPlayerNick() throws IOException, TimeoutException {

        Trial trial = new Trial( dirs );
        trial.setProcessWakeUpTime( 1.5 );
        trial.setInputTimeBuffer( 0 );
        trial.initPlayer( "3" , 0 );

        assertEquals( "PREDEFINED OUTPUT FINISH1" , trial.getStartingPlayerNick() );
        trial.close();

    }

    @Test
    void getFollowingPlayerNick() throws IOException, TimeoutException {

        Trial trial = new Trial( dirs );
        trial.setProcessWakeUpTime( 1.5 );
        trial.setInputTimeBuffer( 0 );
        trial.initPlayer( "3" , 1 );

        assertEquals( "PREDEFINED OUTPUT FINISH2" , trial.getFollowingPlayerNick() );
        trial.close();

    }

    @Test
    void getLastPlayer() throws IOException, TimeoutException {

        Trial trial = new Trial( dirs );
        trial.setProcessWakeUpTime( 1.5 );
        trial.setInputTimeBuffer( 0 );
        trial.initPlayer( "3" , 0 );
        trial.initPlayer( "3" , 1 );

        trial.move( "START" );

        assertEquals( "PREDEFINED OUTPUT FINISH1" , trial.getLastPlayer() );

        trial.move( trial.getLastMove() );

        assertEquals( "PREDEFINED OUTPUT FINISH2" , trial.getLastPlayer() );

        trial.close();

    }

    @Test
    void getLastMove() throws IOException, TimeoutException {

        Trial trial = new Trial( dirs );
        trial.setProcessWakeUpTime( 1.5 );
        trial.setInputTimeBuffer( 0 );
        trial.initPlayer( "3" , 0 );
        trial.initPlayer( "3" , 1 );

        trial.move( "START" );

        assertEquals( "0x0_0x1" , trial.getLastMove() );

        trial.close();

    }

    @Test
    void initPlayer() throws IOException, TimeoutException {

        Trial trial = new Trial( dirs );
        trial.setProcessWakeUpTime( 1.5 );
        trial.setInputTimeBuffer( 0 );
        trial.initPlayer( "3" , 0 );
        trial.initPlayer( "3" , 1 );
        trial.close();

    }

    @Test
    void move() throws IOException, TimeoutException {

        Trial trial = new Trial( dirs );
        trial.setProcessWakeUpTime( 1.5 );
        trial.setInputTimeBuffer( 0 );
        trial.initPlayer( "3" , 0 );
        trial.initPlayer( "3" , 1 );

        trial.move( "START" );
        trial.move( trial.getLastMove() );

        trial.close();

    }

}
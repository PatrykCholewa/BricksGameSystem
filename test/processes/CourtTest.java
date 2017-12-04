package processes;

import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.ProtocolException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CourtTest {

    final static File get1Dir = new File( "./test/testFiles/predefinedOutputs/Get_Finish1" );
    final static File get2Dir = new File( "./test/testFiles/predefinedOutputs/Get_Finish2" );

    @Test
    void nextMove() throws ProtocolException, FileNotFoundException {

        Court court = new Court( get1Dir , get2Dir );
        court.setBoard( 3 , new ArrayList<>() );
        court.start();

        court.nextMove();
        court.nextMove();

        court.close();

    }

    @Test
    void start() throws ProtocolException, FileNotFoundException {

        Court court = new Court( get1Dir , get2Dir );
        court.setBoard( 3 , new ArrayList<>() );
        court.start();
        court.close();

    }

    @Test
    void getWinner() throws ProtocolException, FileNotFoundException {

        Court court = new Court( get1Dir , get2Dir );
        court.setBoard( 3 , new ArrayList<>() );
        court.start();
        court.nextMove();
        court.nextMove();
        court.nextMove();
        assertEquals( "PREDEFINED OUTPUT FINISH2" , court.getWinner() );

        ArrayList<Point> box = new ArrayList<>();
        box.add( new Point (0 , 1 ) );
        court.setBoard( 3 , box );
        court.start();
        assertEquals( "PREDEFINED OUTPUT FINISH2" , court.getWinner() );

    }

    @Test
    void switchPlayers() throws FileNotFoundException, ProtocolException {

        Court court = new Court( get1Dir , get2Dir );

        assertEquals( "PREDEFINED OUTPUT FINISH1" , court.getStartingPlayerNick() );
        assertEquals( "PREDEFINED OUTPUT FINISH2" , court.getFollowingPlayerNick() );

        court.switchPlayers();

        assertEquals( "PREDEFINED OUTPUT FINISH2" , court.getStartingPlayerNick() );
        assertEquals( "PREDEFINED OUTPUT FINISH1" , court.getFollowingPlayerNick() );

    }

    @Test
    void isFinished() throws ProtocolException, FileNotFoundException {

        Court court = new Court( get1Dir , get2Dir );
        court.setBoard( 3 , new ArrayList<>() );
        court.start();
        assertFalse( court.isFinished() );
        court.nextMove();
        assertFalse( court.isFinished() );
        court.nextMove();
        assertFalse( court.isFinished() );
        court.nextMove();
        assertTrue( court.isFinished() );

        court.resetBoard();
        court.start();
        assertFalse( court.isFinished() );

        ArrayList<Point> box = new ArrayList<>();
        box.add( new Point (0 , 1 ) );
        court.setBoard( 3 , box );
        court.start();
        assertTrue( court.isFinished() );

    }

    @Test
    void getLastMove() throws FileNotFoundException, ProtocolException {

        Court court = new Court( get1Dir , get2Dir );
        court.setBoard( 3 , new ArrayList<>() );
        court.start();
        assertEquals( "0x0_0x1" , court.getLastMove() );
        court.nextMove();
        assertEquals( "0x2_1x2" , court.getLastMove() );
        court.nextMove();
        assertEquals( "2x2_2x1" , court.getLastMove() );
        court.nextMove();
        assertEquals( "2x0_1x0" , court.getLastMove() );

        court.resetBoard();
        court.start();
        assertEquals( "0x0_0x1" , court.getLastMove() );

        ArrayList<Point> box = new ArrayList<>();
        box.add( new Point (0 , 1 ) );
        court.setBoard( 3 , box );
        court.start();
        assertEquals( "0x0_0x1" , court.getLastMove() );

    }

    @Test
    void getMessage() throws FileNotFoundException, ProtocolException {

        Court court = new Court( get1Dir , get2Dir );
        court.setBoard( 3 , new ArrayList<>() );
        court.start();
        assertEquals( "OK" , court.getMessage() );
        court.nextMove();
        assertEquals( "OK" , court.getMessage() );
        court.nextMove();
        assertEquals( "OK" , court.getMessage() );
        court.nextMove();
        assertTrue( court.getMessage().contains( "normally") );

        court.resetBoard();
        court.start();
        assertEquals( "OK" , court.getMessage() );

        ArrayList<Point> box = new ArrayList<>();
        box.add( new Point (0 , 1 ) );
        court.setBoard( 3 , box );
        court.start();
        assertTrue( court.getMessage().contains( "already" ) );

    }

}
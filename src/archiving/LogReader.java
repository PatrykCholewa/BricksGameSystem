package archiving;

import java.io.*;
import java.net.ProtocolException;
import java.util.ArrayList;

/**
 * @author Patryk Cholewa
 */

public class LogReader {

    private int size;
    private String startingPlayer;
    private String followingPlayer;
    private String initData;
    private ArrayList<String> moves;
    private String winner;
    private String finishMessage;

    LogReader(File infoFile) throws FileNotFoundException, ProtocolException {

            BufferedReader bufferedReader = new BufferedReader( new FileReader( infoFile ) );
            String []stringList;
            moves = new ArrayList<>();

        try {

            //INIT
            String line = bufferedReader.readLine();
            line = line.split( "\\|" )[0];
            stringList = line.split( "#" );
            size = Integer.valueOf( stringList[0] );
            startingPlayer = stringList[1];
            followingPlayer = stringList[2];
            initData = bufferedReader.readLine();

            //MOVES
            while( !(line = bufferedReader.readLine() ).equals( "---" ) ){
                line = line.split( ":" )[0];
                line = line.substring( 0 , line.length() - 1 );
                moves.add( line );
            }

            winner = bufferedReader.readLine();
            finishMessage = bufferedReader.readLine();

            bufferedReader.close();

        } catch (IOException | IndexOutOfBoundsException e ) {
            throw new ProtocolException( "Can't read logFile!" );
        }

    }

    ArrayList<String> getMoves( ){
        return moves;
    }

    int getSize() {
        return size;
    }

    String getStartingPlayer() {
        return startingPlayer;
    }

    String getFollowingPlayer() {
        return followingPlayer;
    }

    String getInitData() {
        return initData;
    }

    String getWinner() {
        return winner;
    }

    String getFinishMessage() {
        return finishMessage;
    }

}

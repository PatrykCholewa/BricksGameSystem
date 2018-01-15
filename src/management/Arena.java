package management;

import archiving.Recorder;
import enums.FailureReason;
import processes.Court;
import tools.Translator;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.ProtocolException;
import java.util.ArrayList;

/**
 * @author Patryk Cholewa
 */

class Arena {

    private Court court;
    private Recorder recorder = null;

    Arena( File player1Dir , File player2Dir  ) throws FileNotFoundException, ProtocolException {
        court = new Court( player1Dir , player2Dir );
    }

    void setLogFile( File logFile ) throws FileNotFoundException {
        recorder = new Recorder( logFile );
    }

    String getStartingPlayerNick(){
        return court.getStartingPlayerNick();
    }

    String getFollowingPlayerNick(){
        return court.getFollowingPlayerNick();
    }

    String getInitData(){
        return court.getInitData();
    }

    String getWinner(){
        return court.getWinner();
    }

    String getLoser(){
        return court.getLoser();
    }

    FailureReason getFailureReason() {
        return court.getFailureReason();
    }

    String getLastMove(){
        return court.getLastMove();
    }

    String getMessage(){
        return court.getMessage();
    }

    Boolean wasDeadlocked(){
        return court.wasDeadlocked();
    }

    void setWatchConstants( double wakeUpTime , double inputBufferTime ){
        court.setWatchConstants( wakeUpTime , inputBufferTime );
    }

    void setBoard( int size , ArrayList<Point> boxes ){
        court.setBoard( size , boxes );
    }

    ArrayList<Point> setBoard( int size , int numberOfBoxes ){
        return court.setBoard( size , numberOfBoxes );
    }

    void setBoard( String initData ){
        court.setBoard( initData );
    }

    Boolean isFinished(){
        return court.isFinished();
    }

    void start() {

        if( recorder != null ) {
            recorder.printHeader(
                    Translator.getSizeFromInitString(court.getInitData()),
                    court.getStartingPlayerNick(),
                    court.getFollowingPlayerNick());

            recorder.printToLog( court.getInitData() );
        }

        court.start();
        updateRecorder();

    }

    private void updateRecorder(){

        if( recorder != null ){

            recorder.printToLog( court.getLastMove() + " :" + court.getLastPlayer() );

            if( court.isFinished() ){

                recorder.printToLog( "---" );
                recorder.printToLog( court.getWinner() );
                recorder.printToLog( court.getMessage() );
                close();

            }

        }

    }

    /**
     * @throws NullPointerException when the game is finished!
     */
    void nextMove() throws SecurityException{
        if( court.getWinner() != null ){
            throw new NullPointerException( "The game has ended!" );
        }

        court.nextMove();
        updateRecorder();

    }

    void finish(){

        while( !isFinished() ){
            try {
                nextMove();
            } catch ( NullPointerException ignored ){
            }
        }

    }

    void close(){
        court.close();
        recorder.logClose();
    }

}

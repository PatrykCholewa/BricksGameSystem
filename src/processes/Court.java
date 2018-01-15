package processes;

import enums.FailureReason;
import game.Referee;
import tools.BoxGenerator;
import tools.Translator;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

/**
 * @author Patryk Cholewa
 */

public class Court {

    private Trial trial;
    private Referee referee;

    private String initData;

    private String winner;
    private FailureReason failureReason;
    private String message = "OK";

    /**
     * @param player1Dir starting player directory
     * @param player2Dir following player directory
     * @throws FileNotFoundException if info.txt cannot be found
     * @throws ProtocolException if interrupted in the other way
     */
    public Court( File player1Dir , File player2Dir ) throws FileNotFoundException, ProtocolException {
        File[] playerFiles = new File[2];
        playerFiles[0] = player1Dir;
        playerFiles[1] = player2Dir;
        trial = new Trial(playerFiles);
    }

    public String getStartingPlayerNick() {
        return trial.getStartingPlayerNick();
    }

    public String getFollowingPlayerNick() {
        return trial.getFollowingPlayerNick();
    }

    public String getInitData(){
        return initData;
    }

    /**
     * @return winner's nick
     */
    public String getWinner(){
        return winner;
    }

    public FailureReason getFailureReason(){
        return failureReason;
    }

    public String getLastMove(){
        return trial.getLastMove();
    }

    /**
     * Returns message about status of a game.
     * OK if a game hasn't finished!
     * something else if a game has already finished!
     * @return message
     */
    public String getMessage(){
        return message;
    }

    public String getLastPlayer(){
        return trial.getLastPlayer();
    }

    /**
     * Sets duelling delays.
     * Params should be given in seconds.
     * @param wakeUpTime time between waking up a process and the actual output to it
     * @param inputBufferTime allowed delay of getting input from processes
     */
    public void setWatchConstants( double wakeUpTime , double inputBufferTime ){
        trial.setProcessWakeUpTime( wakeUpTime );
        trial.setInputTimeBuffer( inputBufferTime );
    }

    /**
     * Sets board with params got from initData
     * @param initData can be got from getInitData() method
     */
    public void setBoard( String initData ){
        this.initData = initData;
        referee = new Referee( Translator.getSizeFromInitString( initData ) );
        referee.setInitialBoxes( Translator.boxesFromInitString( initData ) );
    }

    public void setBoard( int size , ArrayList<Point> listOfBoxes ){

        initData = Translator.initToString( size , listOfBoxes );
        referee = new Referee( size );
        referee.setInitialBoxes( listOfBoxes );

    }

    public ArrayList<Point> setBoard( int size , int numberOfBoxes ){

        referee = new Referee( size );

        ArrayList<Point> boxes = BoxGenerator.generateBoxes( size , numberOfBoxes );
        referee.setInitialBoxes( boxes );

        initData = Translator.initToString( size , boxes );

        return boxes;

    }

    /**
     * @return true if the game is finished
     */
    public Boolean isFinished(){
        return winner != null;
    }

    private void updateWinner(){
        if( referee.isFinished() ){
            winner = trial.getLastPlayer();
            trial.close();
            message = "Player " + winner + " won normally!";
            failureReason = FailureReason.NORMAL;
        }
    }

    private void failure( String message , FailureReason failureReason ){
        winner = trial.getNotLastPlayer();
        trial.close();
        this.failureReason = failureReason;
        this.message = message;
    }

    private void initPlayer( int player ){
        try {
            trial.initPlayer( initData , player );
        } catch ( IOException | TimeoutException e ){
            failure( "Player " + trial.getLastPlayer() + " -> " + e.getMessage() , trial.getFailureReasonEnum() );
        } catch ( SecurityException e ){
            failure( e.getMessage() , trial.getFailureReasonEnum() );
        }
    }

    /**
     * Starts a game.
     */
    public void start(){

        winner = null;
        message = "OK";
        trial.reset();

        initPlayer( 0 );
        if( isFinished() ) return;

        initPlayer( 1 );
        if( isFinished() ) return;

        try {
            trial.move("START");
            referee.addRectangle(Translator.stringToBoxPair(trial.getLastMove()));
        }catch ( IllegalArgumentException e ){
            failure( "Player " + trial.getLastPlayer() + " -> " + e.getMessage() , FailureReason.INVALIDMOVE );
        }catch ( IOException | TimeoutException e ) {
            failure( "Player " + trial.getLastPlayer() + " -> " + e.getMessage() , trial.getFailureReasonEnum() );
        }catch ( SecurityException e ){
            failure( e.getMessage() , trial.getFailureReasonEnum() );
        }

        updateWinner();

    }

    /**
     * Allows next player to make a move.
     */
    public void nextMove() {

        try {
            trial.move(trial.getLastMove());
            referee.addRectangle(Translator.stringToBoxPair(trial.getLastMove()));
        } catch ( IllegalArgumentException e ){
            failure( "Player " + trial.getLastPlayer() + " -> " + e.getMessage() , FailureReason.INVALIDMOVE );
        } catch ( IOException | TimeoutException e ){
            failure( "Player " + trial.getLastPlayer() + " -> " + e.getMessage() , trial.getFailureReasonEnum() );
        } catch ( SecurityException e  ){
            failure( e.getMessage() , trial.getFailureReasonEnum() );
        }

        updateWinner();

    }

    public void close() {
        trial.close();
    }

}

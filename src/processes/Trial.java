package processes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ProtocolException;
import java.util.concurrent.TimeoutException;

/**
 * @author Patryk Cholewa
 */

class Trial {

    private Commander []commanders = new Commander[2];
    private Watch watch = new Watch();
    private int lastPlayer = -1;
    private String lastMove;
    private String errorMessage;

    Trial( File []playerDirs ) throws FileNotFoundException, ProtocolException {

        this.commanders[0] = new Commander( playerDirs[0] );
        this.commanders[1] = new Commander( playerDirs[1] );

    }

    String getStartingPlayerNick(){
        return commanders[0].getNick();
    }

    String getFollowingPlayerNick(){
        return commanders[1].getNick();
    }

    String getLastPlayer(){
        return commanders[lastPlayer].getNick();
    }

    String getNotLastPlayer(){
        return commanders[(lastPlayer+1)%2].getNick();
    }

    String getLastMove(){
        return lastMove;
    }

    private void nextPlayer( ){
        lastPlayer =  ( lastPlayer + 1 )%2;
    }

    void reset(){
        lastPlayer = -1;
        close();
    }

    void initPlayer( String initData , int player ) throws IOException, TimeoutException {

        nextPlayer();
        try {
            commanders[player].initProcess();
        } catch ( IOException e ){
            lastMove = null;
            throw e;
        }
        commanders[player].insertOutputLine( initData );

        DeadlockProtector deadlockProtector = new DeadlockProtector();
        waitForAnswer( deadlockProtector , player );
        errorCheck( deadlockProtector , player );

        lastMove = commanders[player].getInputLine();
        if( !lastMove.equals( "OK" ) ){
            throw new ProtocolException( "Player " + commanders[player].getNick() + " should have given \"OK\", but gave \"" + lastMove + "\"!" );
        }

    }

    private void errorCheck( DeadlockProtector deadlockProtector , int player ) throws  TimeoutException , InternalError {
        if( deadlockProtector.isDeadlockOccurred() ){
            lastMove = "DEADLOCKNORESPONSE";
            throw new InternalError( "Player " + commanders[player].getNick() + " caused deadlock!" );
        }

        if( errorMessage != null ){
            throw new TimeoutException( errorMessage );
        }
    }

    private void waitForAnswer( DeadlockProtector deadlockProtector , int player ){

        errorMessage = null;
        Thread lineGetterThread = new Thread( ()->{

            watch.initTimer();
            try {
                while (!commanders[player].hasInput()) {

                    if (watch.exceededInitTime()) {
                        deadlockProtector.stop();
                        lastMove = "NORESPONSE";
                        errorMessage = "Player " + commanders[player].getNick() + " do not answer!";
                    } else {
                        watch.waitCheckInterval();
                    }
                }
            } catch ( IOException e ){
                errorMessage = e.getMessage();
            }

            deadlockProtector.stop();

        });

        lineGetterThread.setDaemon( true );

        deadlockProtector.init(lineGetterThread);
        lineGetterThread.start();

        while( !lineGetterThread.isInterrupted() && lineGetterThread.isAlive() ){
            watch.waitCheckInterval();
        }

    }

    void move( String playerInput ) throws TimeoutException, IOException {

        nextPlayer();
        commanders[lastPlayer].insertOutputLine( playerInput );

        watch.initTimer();

        DeadlockProtector deadlockProtector = new DeadlockProtector();
        waitForAnswer( deadlockProtector , lastPlayer );
        errorCheck( deadlockProtector ,  lastPlayer );

        lastMove = commanders[lastPlayer].getInputLine();

    }

    void close(){
        commanders[0].killProcess();
        commanders[1].killProcess();
    }

}

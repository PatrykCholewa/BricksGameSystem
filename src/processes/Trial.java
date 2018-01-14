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

        watch.waitProcessWakeupTime();

        commanders[player].insertOutputLine( initData );

        DeadlockProtector deadlockProtector = new DeadlockProtector();
        waitForAnswer( deadlockProtector , false , player );
        errorCheck( deadlockProtector , player );

        lastMove = commanders[player].getInputLine();
        if( !( lastMove.equals( "OK" ) || lastMove.equals( "ok" ) ) ){
            throw new ProtocolException( "Player " + commanders[player].getNick() + " should have given \"OK\", but gave \"" + lastMove + "\"!" );
        }

    }

    private void errorCheck( DeadlockProtector deadlockProtector , int player ) throws  TimeoutException , SecurityException {
        if( deadlockProtector.isDeadlockOccurred() ){
            lastMove = "DEADLOCKNORESPONSE";
            throw new SecurityException( "Player " + commanders[player].getNick() + " does not answer within 5s!" );
        }

        if( errorMessage != null ){
            throw new TimeoutException( errorMessage );
        }
    }

    private void waitForAnswer( DeadlockProtector deadlockProtector , boolean isMove , int player ){

        errorMessage = null;
        Thread lineGetterThread;

        if( isMove ) {
            lineGetterThread = new Thread(() -> {

                watch.initTimer();
                try {
                    while (!commanders[player].hasInput()) {
                        watch.waitCheckInterval();
                    }

                    if(watch.exceededMoveTime()){
                        lastMove = "NORESPONSE";
                        errorMessage = "Player " + commanders[player].getNick() + " is out of time (0.5s)!";
                    }
                } catch (IOException e) {
                    errorMessage = e.getMessage();
                }

                deadlockProtector.stop();

            });
        } else {
            lineGetterThread = new Thread(() -> {

                try {
                    watch.initTimer();
                    while (!commanders[player].hasInput()) {
                       watch.waitCheckInterval();
                    }

                    if(watch.exceededInitTime()){
                        lastMove = "NOOKRESPONSE";
                        errorMessage = "Player " + commanders[player].getNick() + " does not answer OK within (1s)!";
                    }
                } catch (IOException e) {
                    errorMessage = e.getMessage();
                }

                deadlockProtector.stop();

            });
        }

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
        waitForAnswer( deadlockProtector ,true , lastPlayer );
        errorCheck( deadlockProtector ,  lastPlayer );

        lastMove = commanders[lastPlayer].getInputLine();

    }

    void close(){
        commanders[0].killProcess();
        commanders[1].killProcess();
    }

}

package processes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ProtocolException;
import java.util.concurrent.TimeoutException;

/**
 * @author Patryk Cholewa
 */

public class Trial {

    private Commander []commanders = new Commander[2];
    private Watch watch = new Watch();
    private int lastPlayer = -1;
    private String lastMove;

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
        commanders[player].initProcess();
        commanders[player].insertOutputLine( initData );
        watch.initTimer();

        while( !commanders[player].hasInput() ){
            if( watch.exceededInitTime() ){
                throw new TimeoutException( "Player " + commanders[player].getNick() + " do not answer!" );
            }
        }

        lastMove = commanders[player].getInputLine();
        if( !lastMove.equals( "OK" ) ){
            throw new ProtocolException( "Player " + commanders[player].getNick() + " should have given \"OK\", but gave \"" + lastMove + "\"!" );
        }

    }

    void move( String playerInput ) throws TimeoutException, IOException {

        nextPlayer();
        commanders[lastPlayer].insertOutputLine( playerInput );

        watch.initTimer();
        while ( !commanders[lastPlayer].hasInput() ){
            if( watch.exceededMoveTime() ){
                throw new TimeoutException( "Player " + commanders[lastPlayer].getNick() + " timed out!" );
            } else {
                watch.waitCheckInterval();
            }
        }

        lastMove = commanders[lastPlayer].getInputLine();

    }

    void close(){
        commanders[0].killProcess();
        commanders[1].killProcess();
    }

}

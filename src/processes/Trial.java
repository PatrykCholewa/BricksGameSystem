package processes;

import game.Referee;
import tools.Translator;

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

    /**
     * Return starting player nick.
     * @return starting player nick
     * @throws NullPointerException when game is not started.
     */
    String getStartingPlayerNick(){
        return commanders[0].getWitnessNick();
    }

    /**
     * Return following player nick.
     * @return following player nick
     * @throws NullPointerException when game is not started.
     */
    String getFollowingPlayerNick(){
        return commanders[1].getWitnessNick();
    }

    String getLastPlayer(){
        return commanders[lastPlayer].getWitnessNick();
    }

    String getLastMove(){
        return lastMove;
    }

    private void nextPlayer( ){
        lastPlayer =  ( lastPlayer + 1 )%2;
    }

    void initPlayer( String initData , int player ) throws IOException, TimeoutException {

        nextPlayer();
        commanders[player].tellInputLine( initData );
        watch.initTimer();

        while( !commanders[player].hasOutputLine() ){
            if( watch.exceededInitTime() ){
                throw new TimeoutException( "Player " + commanders[player].getWitnessNick() + " do not answer!" );
            }
        }

        lastMove = commanders[player].getOutputLine();
        if( !lastMove.equals( "OK" ) ){
            throw new ProtocolException( "Player " + commanders[player].getWitnessNick() + " should have given \"OK\", but gave \"" + lastMove + "\"!" );
        }

    }

    void move( String playerInput ) throws TimeoutException, IOException {

        nextPlayer();
        commanders[lastPlayer].tellInputLine( playerInput );

        watch.initTimer();
        while ( !commanders[lastPlayer].hasOutputLine() ){
            if( watch.exceededMoveTime() ){
                throw new TimeoutException( "Player " + commanders[lastPlayer].getWitnessNick() + " timed out!" );
            } else {
                watch.waitCheckInterval();
            }
        }

        lastMove = commanders[lastPlayer].getOutputLine();

    }

    void close(){
        commanders[0].killWitness();
        commanders[1].killWitness();
    }

}

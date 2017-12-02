package processes;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ProtocolException;
import java.util.ArrayList;

public class Trial {

    private Commander []commanders = new Commander[2];
    private Watch watch = new Watch();
    private int lastPlayer = -1;
    private String initData;

    public Trial(File player1Dir , File player2Dir ) throws FileNotFoundException, ProtocolException {

        this.commanders[0] = new Commander( player1Dir );
        this.commanders[1] = new Commander( player2Dir );

    }

    public String getStartingPlayerNick(){
        return commanders[0].getWitnessNick();
    }

    public String getFollowingPlayerNick(){
        return commanders[1].getWitnessNick();
    }

    private void nextPlayer( ){
        lastPlayer =  ( lastPlayer + 1 )%2;
    }

    private void initPlayer( int player ) throws ProtocolException {
        watch.initTimer();
        commanders[player].tellInputLine( initData );
        try {
            while( !commanders[player].hasOutputLine() ){
                if( watch.exceededInitTime() ){
                    throw new ProtocolException( "Player " + commanders[player].getWitnessNick() + " do not answer!" );
                }
            }
        } catch (IOException e) {
            throw new ProtocolException( "Can't start a match!" );
        }
    }

    public void start( int size , ArrayList<Point> listOfBoxes ) throws ProtocolException {

        initData = Translator.initToString( size , listOfBoxes );

        initPlayer( 0 );
        initPlayer( 1 );

    }

    public void close(){
        commanders[0].killWitness();
        commanders[1].killWitness();
    }

}

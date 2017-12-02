package processes;

import game.Referee;
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

public class Trial {

    private Referee referee;
    private Commander []commanders = new Commander[2];
    private File []playerFiles = new File[2];
    private Watch watch = new Watch();
    private int lastPlayer = -1;
    private String initData;
    private String lastMove;

    public Trial( File player1Dir , File player2Dir ) throws FileNotFoundException, ProtocolException {

        this.playerFiles[0] = player1Dir;
        this.playerFiles[1] = player2Dir;

    }

    /**
     * Return starting player nick.
     * @return starting player nick
     * @throws NullPointerException when game is not started.
     */
    public String getStartingPlayerNick(){
        return commanders[0].getWitnessNick();
    }

    /**
     * Return following player nick.
     * @return following player nick
     * @throws NullPointerException when game is not started.
     */
    public String getFollowingPlayerNick(){
        return commanders[1].getWitnessNick();
    }

    public String getLastPlayer(){
        return commanders[lastPlayer].getWitnessNick();
    }

    public String getLastMove(){
        return lastMove;
    }

    private void nextPlayer( ){
        lastPlayer =  ( lastPlayer + 1 )%2;
    }

    private void initPlayer( int player ) throws IOException, TimeoutException {

        nextPlayer();
        commanders[player] = new Commander( playerFiles[player] );
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

    public void resetBoard(){

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
        ArrayList<Point> boxes = referee.setInitialBoxesRandomly( numberOfBoxes );

        initData = Translator.initToString( size , boxes );

        return boxes;

    }

    public void start() throws ProtocolException {

        try {

            initPlayer( 0 );
            initPlayer( 1 );

            move("START");
            referee.addRectangle(Translator.stringToBoxPair(lastMove));

        } catch ( IllegalArgumentException | IOException | TimeoutException e ){
            throw new ProtocolException( "Player " + commanders[lastPlayer].getWitnessNick() + " : " + e.getMessage() );
        }
    }

    private void move( String playerInput ) throws TimeoutException, IOException {

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

    public void nextMove() throws ProtocolException {

        try {
            move( lastMove );
            referee.addRectangle(Translator.stringToBoxPair(lastMove));
        } catch ( IOException | TimeoutException | IllegalArgumentException e ){
            throw new ProtocolException( "Player " + commanders[lastPlayer].getWitnessNick() + " : " + e.getMessage() );
        }

    }

    public void close(){
        commanders[0].killWitness();
        commanders[1].killWitness();
    }

}

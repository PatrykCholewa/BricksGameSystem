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

public class Court {

    private File []playerFiles = new File[2];
    private Trial trial;
    private String initData;
    private Referee referee;

    public Court(File player1Dir , File player2Dir ) throws FileNotFoundException, ProtocolException {
        this.playerFiles[0] = player1Dir;
        this.playerFiles[1] = player2Dir;
    }

    public String getStartingPlayerNick() {
        return trial.getStartingPlayerNick();
    }

    public String getFollowingPlayerNick() {
        return trial.getFollowingPlayerNick();
    }

    public String getLastMove(){
        return trial.getLastMove();
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

    public void start() throws ProtocolException, FileNotFoundException {

        trial = new Trial( playerFiles );

        try {

            trial.initPlayer( initData , 0 );
            trial.initPlayer( initData , 1 );

            trial.move("START");
            referee.addRectangle( Translator.stringToBoxPair( trial.getLastMove() ) );

        } catch ( IllegalArgumentException | IOException | TimeoutException e ){
            throw new ProtocolException( "Player " + trial.getLastPlayer() + " : " + e.getMessage() );
        }
    }

    void nextMove() throws ProtocolException {

        try {
            trial.move( trial.getLastMove() );
            referee.addRectangle(Translator.stringToBoxPair( trial.getLastMove() ) );
        } catch ( IOException | TimeoutException | IllegalArgumentException e ){
            throw new ProtocolException( "Player " + trial.getLastPlayer() + " : " + e.getMessage() );
        }

    }

    public void close() {
        trial.close();
    }

}

package management;

import archiving.DuelShow;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.ProtocolException;
import java.util.ArrayList;

/**
 * @author Patryk Cholewa
 */

public class Duel {

    private DuelShow duelShow;
    private Arena arena;
    private boolean readable;


    public Duel( File logFile ) throws FileNotFoundException, ProtocolException {
        this.readable = true;
        duelShow = new DuelShow( logFile );
    }

    public Duel( File dir1Player , File dir2Player ) throws FileNotFoundException, ProtocolException {
        this.arena = new Arena( dir1Player , dir2Player );
        this.readable = false;
    }

    public Duel( File dir1Player , File dir2Player , File logFile ) throws FileNotFoundException, ProtocolException {
        this( dir1Player , dir2Player );
        this.arena.setLogFile( logFile );
    }

    public String getStartingPlayer(){
        return readable ? duelShow.getStartingPlayer() : arena.getStartingPlayerNick();
    }

    public String getFollowingPlayer(){
        return readable ? duelShow.getFollowingPlayer() : arena.getFollowingPlayerNick();
    }

    public String getInitData(){
        return readable ? duelShow.getInitData() : arena.getInitData();
    }

    public String getWinner(){
        return readable ? duelShow.getWinner() : arena.getWinner();
    }

    public String getLastMove(){
        return readable ? duelShow.lastMove() : arena.getLastMove();
    }

    public int getMoveCounter() {
        return duelShow.getMoveCounter();
    }

    /**
     * Returns the game status.
     * "OK" if the game hasn't finished.
     * End cause if the game has finished.
     * @return game status
     */
    public String getMessage(){
        return readable ? duelShow.getMessage() : arena.getMessage();
    }

    public ArrayList<Point> setBoard( int size , int numberOfBoxes ){

        if( readable ){
            throw new UnsupportedOperationException( "Cannot set board if you read logs!" );
        }

        return arena.setBoard( size , numberOfBoxes );

    }

    public void setBoard( int size , ArrayList<Point> boxes ){

        if( readable ){
            throw new UnsupportedOperationException( "Cannot set board if you read logs!" );
        }

        arena.setBoard( size, boxes);

    }

    public void setBoard( String initData ){

        if( readable ){
            throw new UnsupportedOperationException( "Cannot set board if you read logs!" );
        }

        arena.setBoard( initData );

    }

    public Boolean isFinished(){
        return readable ? duelShow.isFinished() : arena.isFinished();
    }

    /**
     * Initializes a game and makes an initial move.
     */
    public void start(){
        if( readable ){
            duelShow.start();
        } else {
            try {
                arena.start();
            } catch ( SecurityException ignored){
                ;
            }
        }
    }

    public void nextMove(){
        if( readable ){
            duelShow.nextMove();
        } else {
            try {
                arena.nextMove();
            } catch ( SecurityException ignored){
                ;
            }
        }
    }

    public void close(){
        if( !readable ){
            arena.close();
        }
    }

}

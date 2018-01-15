package management;

import enums.FailureReason;
import tournaments.TournamentScore;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

public class Tournament {

    private TournamentScore score;
    private Executioner executioner;
    private int boardSize;
    private ArrayList<Point> boxes;

    private double wakeUpTime = 0;
    private double inputBufferTime = 0;

    public Tournament( File playerDirectory , File resultsDirectory ) throws IOException {
        this.executioner = new Executioner( playerDirectory );
        this.score = new TournamentScore( resultsDirectory );
    }

    public void setWatchConstants( double wakeUpTime , double inputBufferTime ){
        this.wakeUpTime = wakeUpTime;
        this.inputBufferTime = inputBufferTime;
    }

    public void start( int size , ArrayList<Point> boxes ) throws IOException {
       this.boardSize = size;
       this.boxes = boxes;
    }

    public void nextDuel(){
        if( isFinished() ){
            throw new IllegalStateException( "The tournament is finished!" );
        }

        File []players = executioner.next();

        try {
            Arena arena = new Arena(players[0], players[1]);
            arena.setLogFile( score.createNewDuelLogFile() );
            arena.setBoard( boardSize , boxes );
            arena.setWatchConstants( wakeUpTime , inputBufferTime );
            arena.start();
            arena.finish();

            if( arena.getFailureReason() == FailureReason.DEADLOCK ){
                score.saveError( new TimeoutException( arena.getMessage() ));
            }

            score.addNewDuel( arena.getStartingPlayerNick() ,
                    arena.getFollowingPlayerNick() ,
                    arena.getWinner() ,
                    arena.getMessage() ,
                    arena.getFailureReason() );

        } catch ( Exception e ){
            score.saveError( e );
        }

        if( isFinished() ){
            score.close();
        }

    }

    public Boolean isFinished(){
        return !executioner.hasNext();
    }

    public Double progressPercentage(){
        return executioner.progressPercentage();
    }

}

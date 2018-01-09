package management;

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

    public Tournament( File playerDirectory , File resultsDirectory ) throws IOException {
        this.executioner = new Executioner( playerDirectory );
        this.score = new TournamentScore( resultsDirectory );
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
            arena.start();
            arena.finish();

            if( arena.wasDeadlocked() ){
                score.saveError( new TimeoutException( arena.getMessage() ));
            }

            score.addNewDuel( arena.getStartingPlayerNick() ,
                    arena.getFollowingPlayerNick() ,
                    arena.getWinner() ,
                    arena.getMessage() );

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

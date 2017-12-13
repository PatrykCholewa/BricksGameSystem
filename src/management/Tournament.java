package management;

import tournaments.TournamentScore;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Tournament {

    private Arena arena;
    private TournamentScore score;
    private Executioner executioner;

    public Tournament( File playerDirectory , File resultsDirectory ) throws IOException {
        this.executioner = new Executioner( playerDirectory );
        this.score = new TournamentScore( resultsDirectory );
    }

    public void start( int size , ArrayList<Point> boxes ) throws IOException {
        File []players;
        while( executioner.hasNext() ){
            players = executioner.next();
            try {
                arena = new Arena(players[0], players[1]);
                arena.setLogFile( score.createNewDuelLogFile() );
                arena.setBoard(size, boxes );
                arena.start();
                arena.finish();
                score.addNewDuel( arena.getStartingPlayerNick() ,
                        arena.getFollowingPlayerNick() ,
                        arena.getWinner() ,
                        arena.getMessage() );
            } catch ( Exception e ){
                score.saveError( e );
            }
        }
        score.close();
    }

}

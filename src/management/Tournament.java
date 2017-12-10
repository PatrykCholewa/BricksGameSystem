package management;

import tournaments.TournamentScore;

import java.io.File;
import java.io.IOException;

public class Tournament {

    private Arena arena;
    private TournamentScore score;
    private Executioner executioner;

    public Tournament( File playerDirectory , File resultsDirectory ) throws IOException {
        this.executioner = new Executioner( playerDirectory );
        this.score = new TournamentScore( resultsDirectory );
    }

    public void start( String initData ) throws IOException {
        File []players;
        while( executioner.hasNext() ){
            players = executioner.next();
            try {
                arena = new Arena(players[0], players[1]);
                arena.setLogFile( score.createNewDuelLogFile() );
                arena.setBoard( initData );
                arena.start();
                arena.finish();
                score.addNewDuel( arena.getStartingPlayerNick() ,
                        arena.getFollowingPlayerNick() ,
                        arena.getWinner() );
            } catch ( Exception e ){
                score.saveError( e );
            }
        }
        score.close();
    }

}

package tournaments;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Patryk Cholewa
 */

public class TournamentScore {

    private ArrayList<ScoreDBRecord> players;
    private ArrayList<DuelDBRecord> matches;
    private DuelSaver duelSaver;
    private ScoreSaver scoreSaver;

    public TournamentScore( File saveDirectory ) throws IOException {

        String path = saveDirectory.getPath();
        File duelsFile = new File( path + "/duels.txt" );
        File scoreFile = new File( path + "/score.txt" );

        duelsFile.createNewFile();
        scoreFile.createNewFile();

        players = new ArrayList<>();
        matches = new ArrayList<>();
        duelSaver = new DuelSaver( duelsFile );
        scoreSaver = new ScoreSaver( scoreFile );

    }

    public void addNewDuel( String startingPlayer , String followingPlayer , String winner ) throws IOException {

        int stPlayerIndex = players.indexOf( new ScoreDBRecord(startingPlayer) );
        int flPlayerIndex = players.indexOf( new ScoreDBRecord(followingPlayer) );
        int wnPlayerIndex;
        int matchIndex;

        if( stPlayerIndex == -1 ){
            stPlayerIndex = players.size();
            players.add( new ScoreDBRecord(startingPlayer) );
        }

        if( flPlayerIndex == -1 ){
            flPlayerIndex = players.size();
            players.add( new ScoreDBRecord( followingPlayer ) );
        }

        wnPlayerIndex = winner.equals(startingPlayer) ? stPlayerIndex : flPlayerIndex;
        matchIndex = matches.size();

        matches.add( new DuelDBRecord( matchIndex , stPlayerIndex , flPlayerIndex , wnPlayerIndex ) );
        players.get( wnPlayerIndex ).addOnePoint();
        duelSaver.addDuel( matchIndex , startingPlayer , followingPlayer , winner );
        scoreSaver.updateScore( players );

    }

    void close(){
        duelSaver.close();
    }

}

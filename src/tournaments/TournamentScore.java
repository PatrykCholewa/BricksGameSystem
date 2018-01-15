package tournaments;

import enums.FailureReason;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Patryk Cholewa
 */

public class TournamentScore {

    private String path;
    private ArrayList<ScoreDBRecord> players;
    private ArrayList<DuelDBRecord> matches;
    private DuelSaver duelSaver;
    private ScoreSaver scoreSaver;
    private ErrorSaver errorSaver;

    public TournamentScore( File saveDirectory ) throws IOException {

        this.path = saveDirectory.getPath();
        File duelsFile = new File( path + "/duels.txt" );
        File scoreFile = new File( path + "/score.txt" );
        File errFile = new File( path + "/err.txt" );
        new File( path + "/duels" ).mkdir();

        duelsFile.createNewFile();
        scoreFile.createNewFile();

        players = new ArrayList<>();
        matches = new ArrayList<>();
        duelSaver = new DuelSaver( duelsFile );
        scoreSaver = new ScoreSaver( scoreFile );
        errorSaver = new ErrorSaver( errFile );

    }

    public File createNewDuelLogFile() throws IOException {
        File logFile = new File( path + "/duels/" + (matches.size()) + ".txt" );
        logFile.createNewFile();
        return logFile;
    }

    private int getIndexOfPlayer( String player ){
        for( int i = 0 ; i < players.size() ; i ++ ){
            if( players.get(i).getPlayer().equals(player) ){
                return i;
            }
        }
        return -1;
    }

    public void addNewDuel(String startingPlayer , String followingPlayer ,
                           String winner ,
                           String failureMessage , FailureReason failureEnum ) throws IOException {

        int stPlayerIndex = getIndexOfPlayer( startingPlayer );
        int flPlayerIndex = getIndexOfPlayer( followingPlayer );
        int wnPlayerIndex;
        int lsPlayerIndex;
        int matchIndex;

        if( stPlayerIndex == -1 ){
            stPlayerIndex = players.size();
            players.add( new ScoreDBRecord(startingPlayer) );
        }

        if( flPlayerIndex == -1 ){
            flPlayerIndex = players.size();
            players.add( new ScoreDBRecord( followingPlayer ) );
        }

        if( winner.equals(startingPlayer) ){
            wnPlayerIndex = stPlayerIndex;
            lsPlayerIndex = flPlayerIndex;
        } else {
            wnPlayerIndex = flPlayerIndex;
            lsPlayerIndex = stPlayerIndex;
        }

        matchIndex = matches.size();

        matches.add( new DuelDBRecord( matchIndex , stPlayerIndex , flPlayerIndex , wnPlayerIndex ) );

        if( failureEnum == FailureReason.NORMAL ){
            players.get( wnPlayerIndex ).addOnePointToAllWins();
            players.get( wnPlayerIndex ).addOnePointToNormalWins();
            players.get( lsPlayerIndex ).addOnePointToAllLoses();
        } else {
            players.get( wnPlayerIndex ).addOnePointToAllWins();
            players.get( lsPlayerIndex ).addOnePointToAllLoses();
            players.get( lsPlayerIndex ).addErrorFailure( failureEnum );
        }

        duelSaver.addDuel( matchIndex , startingPlayer , followingPlayer , winner , failureMessage );
        scoreSaver.updateScore( players );

    }

    public void saveError( Exception e ){
        try {
            errorSaver.writeErr( e );
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void close(){
        duelSaver.close();
        errorSaver.close();
    }

}

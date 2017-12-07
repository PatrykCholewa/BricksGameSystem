package archiving;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.ProtocolException;

/**
 * @author Patryk Cholewa
 */

public class DuelShow {

    private LogReader logReader;
    int moveCounter = 0;

    public DuelShow( File logFile ) throws FileNotFoundException, ProtocolException {
        logReader = new LogReader( logFile );
    }

    public String getStartingPlayer(){
        return logReader.getStartingPlayer();
    }

    public String getFollowingPlayer(){
        return logReader.getFollowingPlayer();
    }

    public String getInitData(){
        return logReader.getInitData();
    }

    public String getWinner(){
        return logReader.getWinner();
    }

    public String lastMove(){
        return logReader.getMoves().get(moveCounter);
    }

    public String getMessage(){
        return isFinished() ? logReader.getFinishMessage() : "OK" ;
    }

    public Boolean isFinished(){
        return moveCounter > logReader.getMoves().size() - 2;
    }

    public void start(){
        moveCounter = 0;
    }

    public void nextMove(){
        moveCounter++;
    }

    public void finish(){
        moveCounter = logReader.getMoves().size();
    }

}

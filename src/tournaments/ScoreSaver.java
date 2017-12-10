package tournaments;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Patryk Cholewa
 */

public class ScoreSaver {

    private File saveFile;

    ScoreSaver( File saveFile ){
        this.saveFile = saveFile;
    }

    void updateScore( ArrayList<ScoreDBRecord> records ) throws IOException {

        BufferedWriter bw = new BufferedWriter( new FileWriter( saveFile ) );
        ScoreDBRecord []recordsCopy = records.toArray( new ScoreDBRecord[records.size()] );

        Arrays.sort( recordsCopy );

        for( ScoreDBRecord record : recordsCopy ){
            bw.write( record.getPlayer() + " : " + record.getPoints() );
            bw.newLine();
        }
        bw.flush();
        bw.close();

    }

}

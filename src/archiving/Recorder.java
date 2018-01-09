package archiving;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * @author Paweł Zych
 */

public class Recorder {
    private File logFile;
    private PrintWriter out;

    public Recorder(File logFile) throws FileNotFoundException {
        this.logFile = logFile;
        this.out = new PrintWriter(logFile);
    }

    public void printHeader(int size, String nickname1, String nickname2){
        out.print(size+"#"+nickname1+"#"+nickname2+"#| :size :P1_nick :P2_nick\n");
        out.flush();
    }

    public void printToLog(String command) {
        out.print(command + "\n");
        out.flush();
    }

    public void logClose(){
        out.close();
    }
}

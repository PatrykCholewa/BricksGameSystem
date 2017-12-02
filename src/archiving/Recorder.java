package archiving;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * @author Paweł Zych
 */


public class Recorder {
    private File logFile;
    private PrintWriter out;
    private Scanner in;

    public Recorder(File logFile)throws Exception{
        this.logFile = logFile;
        this.out = new PrintWriter(logFile);
        this.in = new Scanner(logFile);
    }

    public void printToLog(String command) throws FileNotFoundException {
        out.print(command + "\n");
    }

    public void logClose(){
        out.close();
    }

    public boolean hasLogNextLine() {
        return in.hasNextLine();
    }

    public String readLineFromLog() {
        return in.nextLine();
    }
}

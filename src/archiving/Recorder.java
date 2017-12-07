package archiving;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

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
        out.print("//#"+size+"#"+nickname1+"#"+nickname2+"#| :size :P1_nick :P2_nick\n");
    }

    public void printGenCells(ArrayList<Point> generatedPoints) {
        for (Point p:generatedPoints) {
            out.print("G "+p.x+"x"+p.y+"\n");
        }
    }

    public void printToLog(String command) {
        out.print(command + "\n");
    }

    public void logClose(){
        out.close();
    }
}

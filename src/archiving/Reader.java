package archiving;

import tools.Translator;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.ProtocolException;
import java.util.Scanner;

public class Reader {
    File inputFile;
    Scanner scn;
    int no;

    public Reader(File input) throws FileNotFoundException {
        this.inputFile = input;
        this.scn = new Scanner(inputFile);
        this.no = 1;
    }

    public String readNext() throws ProtocolException {
            if (scn.hasNextLine()) {
                no++;
                return scn.nextLine();
            }
           return null;
    }
    public int getPlayer(){
        return  (no%2)+1;
    }
}

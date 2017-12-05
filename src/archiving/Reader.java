package archiving;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.ProtocolException;
import java.util.Scanner;

/**
 * @author Paweł Zych
 */


public class Reader {
    private File inputFile;
    private Scanner scn;
    private int no;
    public int size;
    public String nickname1;
    public String nickname2;


    public Reader(File input) throws FileNotFoundException {
        this.inputFile = input;
        this.scn = new Scanner(inputFile);
        this.no = 1;
    }
    public void readHeader(){
        String header = scn.nextLine();
        String[] split = header.split("#");
        size = Integer.parseInt(split[1]);
        nickname1 = split[2];
        nickname2 = split[3];
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

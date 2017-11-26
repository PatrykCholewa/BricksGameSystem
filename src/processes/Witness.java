package processes;

import java.io.*;

/**
 * @author Patryk Cholewa
 */

public class Witness {

    final Process process;
    final String nick;

    Witness( File directory ) throws IOException {

        File infoFile = new File( directory.getPath() + "/info.txt" );

        FileReader fr = new FileReader( infoFile );
        BufferedReader bufferedReader = new BufferedReader( fr );

        //PROCESS SET
        ProcessBuilder pb = new ProcessBuilder();
        pb.command( bufferedReader.readLine().split( " " ) );
        pb.directory( directory );
        this.process = pb.start();

        //NICK SET
        this.nick = bufferedReader.readLine();

    }

}

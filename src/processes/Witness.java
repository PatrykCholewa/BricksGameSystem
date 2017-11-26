package processes;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Patryk Cholewa
 */

class Witness extends Process {

    private final Process process;
    private final String nick;

    Witness( File directory ) throws IOException {

        File infoFile = new File( directory.getPath() + "/info.txt" );

        FileReader fr = new FileReader( infoFile );
        BufferedReader bufferedReader = new BufferedReader( fr );

        //PROCESS SET
        ProcessBuilder pb = new ProcessBuilder();
        pb.command( bufferedReader.readLine().split( " " ) );
        pb.directory( directory );
        process =  pb.start();

        //NICK SET
        this.nick = bufferedReader.readLine();

    }

    public String getNick(){
        return nick;
    }

    @Override
    public OutputStream getOutputStream() {
        return process.getOutputStream();
    }

    @Override
    public InputStream getInputStream() {
        return process.getInputStream();
    }

    @Override
    public InputStream getErrorStream() {
        return process.getErrorStream();
    }

    @Override
    public int waitFor() throws InterruptedException {
        return process.waitFor();
    }

    @Override
    public int exitValue() {
        return process.exitValue();
    }

    @Override
    public void destroy() {
        process.destroy();
    }
}

package processes;

import java.io.*;
import java.net.ProtocolException;

/**
 * @author Patryk Cholewa
 */

class Witness extends Process {

    private final Process process;
    private final String nick;

    Witness( File directory ) throws FileNotFoundException, ProtocolException {

        File infoFile = new File( directory.getPath() + "/info.txt" );

        FileReader fr = new FileReader( infoFile );
        BufferedReader bufferedReader = new BufferedReader( fr );

        //PROCESS SET
        ProcessBuilder pb = new ProcessBuilder();
        try {
            pb.command(bufferedReader.readLine().split(" "));
        } catch ( IOException e ){
            throw new ProtocolException( "Invalid info.txt file format!" );
        }
        pb.directory( directory );

        //NICK SET
        try {
            this.nick = bufferedReader.readLine();
        } catch ( IOException e ){
            throw new ProtocolException( "Invalid info.txt file format!" );
        }

        try {
            process =  pb.start();
        } catch (IOException e) {
            throw new RuntimeException( "Can't start program :\"" + nick + "\"." );
        }


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

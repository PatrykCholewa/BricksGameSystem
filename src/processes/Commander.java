package processes;

import tools.InfoReader;

import java.io.*;
import java.net.ProtocolException;

/**
 * @author Patryk Cholewa
 */

class Commander {

    private ProcessBuilder processBuilder;
    private String nick;

    private BufferedReader input;
    private PrintWriter output;
    private Process process;

    private String gotNotShowedLine;

    Commander( File directory ) throws ProtocolException , FileNotFoundException{

        processBuilder = new ProcessBuilder();

        String []info = InfoReader.getLines( directory );
        if( info[0].split( " " )[0].contains( ".exe" ) ){
            info[0] = directory.getPath() + "\\" + info[0];
            processBuilder.command( info[0].split(" ")  );
        } else {
            processBuilder.command( info[0].split(" ")  );
            processBuilder.directory(directory);
        }

        nick = info[1];

    }

    void initProcess() throws IOException {

        process = processBuilder.start();
        input = new BufferedReader( new InputStreamReader( process.getInputStream() ) );
        output = new PrintWriter( process.getOutputStream() , true );

    }

    String getNick(){
        return nick;

    }

    boolean hasInput() throws IOException {
        return gotNotShowedLine != null || (gotNotShowedLine = input.readLine()) != null;
    }

    private String removeGotNotShowedLine(){
        String tmp = gotNotShowedLine;
        gotNotShowedLine = null;
        return tmp;
    }

    String getInputLine() throws IOException{
        if( gotNotShowedLine == null ){
            if( hasInput() ){
                return removeGotNotShowedLine();
            } else {
                throw new NullPointerException( "There's no line!" );
            }
        } else {
            return removeGotNotShowedLine();
        }
    }

    void insertOutputLine(String line ){
        output.println( line );
    }

    void killProcess(){

        try {
            insertOutputLine("STOP");
            process.destroy();

            output.close();
            input.close();
        } catch ( NullPointerException | IOException e) {
            ;
        }

    }

}

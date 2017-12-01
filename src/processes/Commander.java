package processes;

import java.io.*;
import java.net.ProtocolException;

/**
 * @author Patryk Cholewa
 */

class Commander {

    private BufferedReader witnessStdout;
    private PrintWriter witnessStdin;
    private Witness witness;

    Commander( File witnessDir ) throws ProtocolException , FileNotFoundException{

        try {
            this.witness = new Witness(witnessDir);
        } catch ( RuntimeException e ) {
            throw new ProtocolException( e.getMessage() );
        }

        witnessStdout = new BufferedReader( new InputStreamReader( witness.getInputStream() ) );
        witnessStdin = new PrintWriter( witness.getOutputStream() , true );

    }

    String getWitnessNick(){
        return witness.getNick();
    }

    String getOutputLine() throws IOException{
        return witnessStdout.readLine();
    }

    void tellInputLine( String line ){
        witnessStdin.println( line );
    }

    void killWitness(){

        tellInputLine("STOP");

        witnessStdin.close();

        try {
            witnessStdout.close();
        } catch (IOException e) {
            ;
        }

        witness.destroy();

    }

}

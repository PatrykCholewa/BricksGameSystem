package processes;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStreamWriter;
import java.net.ProtocolException;
import java.util.Scanner;

class Commander {

    private Scanner witnessStdout;
    private BufferedWriter witnessStdin;
    private Witness witness;

    Commander( File witnessDir ) throws ProtocolException {

        try {
            this.witness = new Witness(witnessDir);
        } catch ( FileNotFoundException  | ProtocolException | RuntimeException e) {
            throw new ProtocolException( e.getMessage() );
        }

        witnessStdout = new Scanner( witness.getInputStream() );
        witnessStdin = new BufferedWriter( new OutputStreamWriter( witness.getOutputStream() ) );

    }

    String getWitnessNick(){
        return witness.getNick();
    }

    Boolean hasOutput(){
        throw new UnsupportedOperationException();
//        return witnessStdout.hasNext();
    }

    String getOutputLine(){
        return witnessStdout.nextLine();
    }

    void tellInputLine( String line){
        throw new UnsupportedOperationException();
//        witnessStdin.write(line);
    }

    void killWitness(){
/*
        try {
            tellInputLine("STOP");
        } catch ( IOException e ){
            ;
        }
*/
        witness.destroy();

    }

}

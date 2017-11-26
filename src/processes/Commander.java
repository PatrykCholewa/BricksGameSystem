package processes;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Scanner;

class Commander {

    private Scanner witnessStdout;
    private BufferedWriter witnessStdin;
    private Witness witness;

    Commander( Witness witness ){

        this.witness = witness;
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

    void tellInputLine( String line) throws IOException{
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

package processes;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.util.Scanner;

class Commander {

    private Scanner witnessStdout;
    private BufferedWriter witnessStdin;

    Commander( Witness witness ){

        witnessStdout = new Scanner( witness.getInputStream() );
        witnessStdin = new BufferedWriter( new OutputStreamWriter( witness.getOutputStream() ) );

    }

}

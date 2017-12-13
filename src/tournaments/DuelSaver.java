package tournaments;

import java.io.*;

/**
 * @author Patryk Cholewa
 */

class DuelSaver {

    private BufferedWriter bufferedWriter;

    DuelSaver( File saveFile ) throws IOException {

        this.bufferedWriter = new BufferedWriter( new FileWriter( saveFile ) );

    }

    void addDuel( int number , String startingPlayer , String followingPlayer , String winner , String endReason ) throws IOException {

        bufferedWriter.write( number + ":" + startingPlayer + ":" + followingPlayer + ":" + winner  + ":" + endReason );
        bufferedWriter.newLine();
        bufferedWriter.flush();

    }

    void close(){
        try {
            bufferedWriter.close();
        } catch ( NullPointerException e ){

        } catch ( IOException e ){
            System.out.println( e.getMessage() );
        }
    }

}

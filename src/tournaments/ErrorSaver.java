package tournaments;

import java.io.*;

/**
 * @author Patryk Cholewa
 */

class ErrorSaver {

    private File errFile;
    private BufferedWriter bufferedWriter;

    ErrorSaver(File errFile) throws IOException {
        this.errFile = errFile;
        bufferedWriter = new BufferedWriter( new FileWriter( errFile ) );
    }

    void writeErr(Exception e) throws IOException {
        try {
            bufferedWriter.write(e.getMessage());
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch ( NullPointerException e1 ){
            ;
        }

    }

    void close() {
        try {
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

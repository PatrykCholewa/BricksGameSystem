package tools;

import java.io.*;
import java.net.ProtocolException;

public class InfoReader {

    public static String []getLines( File directory ) throws ProtocolException, FileNotFoundException {

        File infoFile = new File( directory.getPath() + "/info.txt" );

        FileReader fr = new FileReader( infoFile );
        BufferedReader bufferedReader = new BufferedReader( fr );

        String []ret = new String[2];

        try {
            ret[0] = bufferedReader.readLine();
            ret[1] = bufferedReader.readLine();
        } catch ( IOException e ){
            throw new ProtocolException( "Invalid info.txt file format in \"" + directory + "\"!" );
        }

        if( ret[0] == null || ret[1] == null ){
            throw new ProtocolException( "Invalid info.txt file format in \"" + directory + "\"!" );
        }

        return ret;

    }

}


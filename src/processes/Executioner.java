package processes;

import java.io.File;
import java.nio.file.NotDirectoryException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * @author Patryk Cholewa
 */

public class Executioner implements Iterator{

    private File mainDir;
    private ArrayList<File> playerDirs;

    Executioner( File mainDir ) throws NotDirectoryException {

        if( !mainDir.isDirectory() ){
            throw new NotDirectoryException( "\"" + mainDir.getPath() + "\" is not a directory!" );
        }

        this.mainDir = mainDir;

        this.playerDirs = new ArrayList<>();
        playerDirs.addAll(Arrays.asList(mainDir.listFiles()));

    }

    @Override
    public boolean hasNext() {
        return !playerDirs.isEmpty();
    }

    @Override
    public File next() {
        File tmp = new File( playerDirs.get(0).getPath() );
        playerDirs.remove(0);
        return tmp;
    }


}

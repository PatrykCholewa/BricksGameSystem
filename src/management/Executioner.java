package management;

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
    private int pointer;

    Executioner( File mainDir ) throws NotDirectoryException {

        if( !mainDir.isDirectory() ){
            throw new NotDirectoryException( "\"" + mainDir.getPath() + "\" is not a directory!" );
        }

        this.mainDir = mainDir;

        this.playerDirs = new ArrayList<>();
        playerDirs.addAll(Arrays.asList(mainDir.listFiles()));

        pointer = 0;

    }

    private File []dirsOnPointer(){

        File []dirs = new File[2];
        dirs[0] = playerDirs.get(pointer / playerDirs.size());
        dirs[1] = playerDirs.get(pointer % playerDirs.size());

        return dirs;

    }

    private Boolean validatePointer(){
        File []dirs;
        while( pointer < playerDirs.size()*playerDirs.size() ) {
            dirs = dirsOnPointer();
            if (dirs[0] == dirs[1]) {
                pointer++;
            } else {
                break;
            }
        }

        return pointer < playerDirs.size() * playerDirs.size();

    }

    Double progressPercentage(){
        int max = playerDirs.size()*playerDirs.size();
        return ( (double)pointer )/max;
    }

    @Override
    public boolean hasNext() {

        return validatePointer();

    }

    @Override
    public File []next() {

        if( !validatePointer() ){
            return null;
        }

        File []ret = dirsOnPointer();
        pointer++;
        return ret;

    }


}

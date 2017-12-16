package management;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.NotDirectoryException;

import static org.junit.jupiter.api.Assertions.*;

class ExecutionerTest {

    @Test
    void constructor(){

        assertThrows( NotDirectoryException.class ,  ()-> new Executioner( new File( "./XXX" ) ) );
        try {
            new Executioner( new File ( "./test/testFiles/miniMainDir" ) );
        } catch (NotDirectoryException e) {
            fail( e.getMessage() );
        }
    }

    @Test
    void Iterator() throws NotDirectoryException {

        Executioner executioner = new Executioner(new File("./test/testFiles/miniMainDir"));

        File []files = new File[]{
                new File( "./test/testFiles/miniMainDir/onlyTypeOKDir" ),
                new File( "./test/testFiles/miniMainDir/virusDir" )
        };

        assertTrue( executioner.hasNext() );
        assertArrayEquals( files , executioner.next() );

        File tmp = files[0];
        files[0] = files[1];
        files[1] = tmp;

        assertTrue( executioner.hasNext() );
        assertArrayEquals( files , executioner.next() );

        assertFalse( executioner.hasNext() );

    }

    @Test
    void progressPercentage() throws NotDirectoryException {
        Executioner executioner = new Executioner(new File("./test/testFiles/miniMainDir"));

        assertEquals( 0 , (double)executioner.progressPercentage() );
        executioner.next();
        assertEquals( 0.5 , (double)executioner.progressPercentage() );
        executioner.next();
        assertEquals( 0.75 , (double)executioner.progressPercentage() );
        executioner.next();
        assertEquals( 1.0 , (double)executioner.progressPercentage() );

    }

}
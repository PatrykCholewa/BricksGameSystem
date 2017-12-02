package tools;

import java.awt.*;
import java.net.ProtocolException;
import java.util.ArrayList;

/**
 * @author Patryk Cholewa
 */

public class Translator {

    private static String boxToString( Point box ){
        return (Integer)box.x + "x" + (Integer)box.y;
    }

    private static Point stringToBox(String s )
            throws NumberFormatException{

        String []gotParts = s.split( "x" );
        return new Point( Integer.valueOf( gotParts[0] ) , Integer.valueOf( gotParts[1] ) );

    }

    public static String initToString( Integer size , ArrayList<Point> listOfBoxes ){

        String ret = size.toString();

        for( Point box : listOfBoxes ){
            ret += "_" + boxToString( box );
        }

        return ret;

    }

    public static int getSizeFromInitString( String init ){

        String []gotParts = init.split( "_" );
        return Integer.valueOf( gotParts[0] );

    }

    public static ArrayList<Point> boxesFromInitString( String init ){

        String []gotParts = init.split( "_" );
        ArrayList<Point> boxes = new ArrayList<>();
        for( int i = 1 ; i < gotParts.length ; i++ ){
            boxes.add( stringToBox(gotParts[i]) );
        }

        return boxes;

    }

    public static String rectangleToString(ArrayList<Point> boxes ){

        return boxToString( boxes.get(0) ) + "_" + boxToString( boxes.get(1) );

    }

    public static Point []stringToBoxPair( String command )
            throws ProtocolException{

        Point []boxes = new Point[2];

        String []gotParts = command.split( "_" );

        try{
            boxes[0] = stringToBox( gotParts[0] );
            boxes[1] = stringToBox( gotParts[1] );
        } catch ( NumberFormatException | IndexOutOfBoundsException e ){
            throw new ProtocolException( "Can't translate input : \"" + command + "\"." );
        }

        return  boxes;

    }

}

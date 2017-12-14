package tools;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class BoxGenerator {

    public static ArrayList<Point> generateBoxes( int size , int numberOfRandomBoxes ){

        Random rndGen = new Random();
        Point box;
        ArrayList<Point> toGenerate = new ArrayList<>();
        ArrayList<Point> gotBoxes = new ArrayList<>();

        if( size*size <= numberOfRandomBoxes ) {
            throw new IllegalArgumentException("Board is too small for so many boxes.");
        }

        for( int i = 0 ; i < size*size ; i++ ){
            toGenerate.add( new Point( i/size , i%size ) );
        }

        for( int i = 0 ; i < numberOfRandomBoxes ; i++ ){

            box = toGenerate.remove( rndGen.nextInt( toGenerate.size() ) );
            gotBoxes.add( box );

        }

        return gotBoxes;

    }

}

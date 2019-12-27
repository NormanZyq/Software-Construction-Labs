package P1;

import P1.MagicSquare;
import org.junit.Test;

import java.io.FileNotFoundException;

import static org.junit.Assert.*;

public class MagicSquareTest {

    @Test
    public void magicSquareTest() throws FileNotFoundException {

        /*
            Tests for illegal input handler in generateMagicSquare()
         */
        assertFalse(MagicSquare.generateMagicSquare(90));

        /*
            Tests for method isLegalMagicSquare()
         */
        assertTrue(MagicSquare.isLegalMagicSquare("1.txt"));
        assertTrue(MagicSquare.isLegalMagicSquare("2.txt"));
        assertFalse(MagicSquare.isLegalMagicSquare("3.txt"));
        assertFalse(MagicSquare.isLegalMagicSquare("4.txt"));
        assertFalse(MagicSquare.isLegalMagicSquare("5.txt"));
        /*
            Make a new file to test the generation method.
         */
        MagicSquare.generateMagicSquare(11);
        assertTrue(MagicSquare.isLegalMagicSquare("6.txt"));
    }


}

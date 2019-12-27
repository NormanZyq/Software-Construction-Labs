package application;

import org.junit.Test;

import java.io.FileNotFoundException;

import static org.junit.Assert.*;

public class FileParserTest {
    private FileParser parser = new FileParser();

    {
        try {
            parser.parse("src/txt/Competition_1.txt");
        } catch (FileNotFoundException e) {
            assert false;
        }
    }

    @Test
    public void getN() {
        assertEquals(5, parser.getN());
    }

    @Test
    public void getH() {
        assertEquals(20, parser.getH());
    }

    @Test
    public void getMonkeys() {
        assertEquals(300, parser.getMonkeys().size());
    }
}
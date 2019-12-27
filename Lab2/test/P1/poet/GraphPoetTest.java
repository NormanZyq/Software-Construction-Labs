/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package P1.poet;

import static org.junit.Assert.*;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * Tests for GraphPoet.
 */
public class GraphPoetTest {
    
    // Testing strategy
    //   TODO

    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    /**
     * 测试创造诗句功能
     * @throws IOException  如果文件不存在，会抛出此异常
     */
    @Test
    public void testPoet() throws IOException {
        File file = new File("src/P1/poet/mugar-omni-theater.txt");
//        File file = new File("src/P1/poet/test2.txt");

        GraphPoet graphPoet = new GraphPoet(file);

//        System.out.println(graphPoet.poem("Seek to explore new and exciting synergies!"));
        assertEquals("Test of the system.", graphPoet.poem("Test the system."));
    }

    /**
     * 测试2
     * @throws IOException  文件不存在时抛出
     */
    @Test
    public void testPoet2() throws IOException {
        File file = new File("src/P1/poet/test2.txt");

        GraphPoet graphPoet = new GraphPoet(file);

        assertEquals("Seek to explore strange new life and exciting synergies!", graphPoet.poem("Seek to explore new and exciting synergies!"));
    }
}

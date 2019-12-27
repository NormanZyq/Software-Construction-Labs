/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package P1.graph;

import static org.junit.Assert.*;

import java.util.Collections;

import org.junit.Test;

/**
 * Tests for instance methods of Graph.
 * 
 * <p>PS2 instructions: you MUST NOT add constructors, fields, or non-@Test
 * methods to this class, or change the spec of {@link #emptyInstance()}.
 * Your tests MUST only obtain Graph instances by calling emptyInstance().
 * Your tests MUST NOT refer to specific concrete implementations.
 */
public abstract class GraphInstanceTest {
    
    // Testing strategy
    //   test for graph should contain at least 5 methods' tests
    
    /**
     * Overridden by implementation-specific test classes.
     * 
     * @return a new empty graph of the particular implementation being tested
     */
    public abstract Graph emptyInstance();
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testInitialVerticesEmpty() {
        assertEquals("expected new graph to have no vertices",
                Collections.emptySet(), emptyInstance().vertices());
    }
    
    // other tests for instance methods of Graph

    /**
     * test if the add function works correctly
     */
    @Test
    abstract public void testAdd();

    /**
     * test if the remove vertex function works correctly
     */
    @Test
    abstract public void testRemove();

    /**
     * test if the set function works correctly
     * this test will consist of many parts, because of the spec of the set() method
     * separate them to weight > 0, == 0, and < 0 to test the every possibility
     */
    @Test
    abstract public void testSet();

    /**
     * test if the source function works correctly
     */
    @Test
    abstract public void testSources();

    /**
     * test if the target function works correctly
     */
    @Test
    abstract public void testTargets();

    @Test
    abstract public void testToString();


}

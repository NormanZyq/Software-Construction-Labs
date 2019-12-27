/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package P1.graph;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests for ConcreteVerticesGraph.
 * 
 * This class runs the GraphInstanceTest tests against ConcreteVerticesGraph, as
 * well as tests for that particular implementation.
 * 
 * Tests against the Graph spec should be in GraphInstanceTest.
 */
public class ConcreteVerticesGraphTest extends GraphInstanceTest {
    
    /*
     * Provide a ConcreteVerticesGraph for tests in GraphInstanceTest.
     */
    @Override public Graph<String> emptyInstance() {
        return new ConcreteVerticesGraph<>();
    }

    /*
     * Testing ConcreteVerticesGraph...
     */

    /**
     * test for toString() method
     * in every ConcreteVerticesGraph object, toString should make an apparent string to represent the object
     * for example, now have A, B, C three vertexes and A->B edge with weight 100, A->C edge with weight 200.
     * toString should make string like this:
     *
     * A--100->B--200->C
     * B
     * C
     *
     * if add one more vertex and edge C->D with weight 300, it should be like this:
     *
     * A--100->B--200->C
     * B
     * C--300->D
     * D
     *
     * this test is designed to test if the string is acceptable
     */
    @Test
    public void testToString() {
        ConcreteVerticesGraph<String> testGraph = (ConcreteVerticesGraph<String>) emptyInstance();

        // add test vertexes
        testGraph.add("A");
        testGraph.add("B");
        testGraph.add("C");
        testGraph.add("D");
        testGraph.add("E");

        // add test edges
        testGraph.set("A", "B", 10);
        testGraph.set("C", "E", 20);
        testGraph.set("E", "B", 30);

        String answer = "A--10->B\nB\nC--20->E\nD\nE--30->B";   // answer string should look like this

        assertEquals(answer, testGraph.toString());

    }

    /**
     * 测试添加，分为两组
     * <ul>
     *     <li>
     *         添加不存在的点，返回值应为true
     *     </li>
     *     <li>
     *         添加已存在的点，返回值为false，图不发生任何变化
     *     </li>
     * </ul>
     */
    @Override
    @Test
    public void testAdd() {
        ConcreteVerticesGraph<String> graph = (ConcreteVerticesGraph<String>) emptyInstance();
        assertTrue(graph.add("A"));
        assertTrue(graph.vertices().contains("A"));
        assertEquals(1, graph.vertices().size());

        assertTrue(graph.add("B"));
        assertTrue(graph.vertices().contains("B"));
        assertEquals(2, graph.vertices().size());

        assertFalse(graph.add("B"));
        assertEquals(2, graph.vertices().size());
    }

    /**
     * 测试移除点，分为两组
     * <ul>
     *     <li>
     *         移除图中存在的点，点应该被移除掉
     *     </li>
     *     <li>
     *         移除不存在的点，图应该不发生任何变化
     *     </li>
     * </ul>
     */
    @Test
    @Override
    public void testRemove() {
        ConcreteVerticesGraph<String> graph = (ConcreteVerticesGraph<String>) emptyInstance();

        String a = "A";

        graph.add(a);
        graph.add("B");
        graph.add("C");
        graph.add("D");

        // 1
        assertTrue(graph.remove(a));
        assertEquals(3, graph.vertices().size());
        assertFalse(graph.vertices().contains(a));

        // 2
        assertFalse(graph.remove(a));
        assertEquals(3, graph.vertices().size());
        assertFalse(graph.vertices().contains(a));

    }

    /**
     * 测试设置权，分为三组，输入的weight应该为 >= 0的数
     * <ul>
     *     <li>
     *         weight == 0 且source和target都存在，则此边应当被移除
     *     </li>
     *     <li>
     *         weight == 0 且source和target分别有一个不存在、都不存在时，图不会有任何修改
     *     </li>
     *     <li>
     *         weight > 0 且source和target中分别有一个不存在、都不存在时，会创建缺失的顶点并设置权值
     *     </li>
     * </ul>
     */
    @Test
    @Override
    public void testSet() {
        ConcreteVerticesGraph<String> graph = (ConcreteVerticesGraph<String>) emptyInstance();

        graph.add("A");
        graph.add("B");
        graph.add("C");
        graph.add("D");
        graph.add("E");

        /*

          E--20-->A--10-->B--30-->D
                  |
                  20
                  |
                  ∨
                  C


         */

        // 初始化边，同时作为情况3的测试
        graph.set("A", "B", 10);
        graph.set("A", "C", 20);
        graph.set("B", "D", 30);
        graph.set("E", "A", 20);

        // 1
        graph.set("B", "D", 0);

        // 2
        graph.set("F", "A", 0);
        graph.set("A", "F", 0);

        // 3 续
        // source only
        graph.set("A", "F", 100);

        // target only
        graph.set("G", "B", 100);

        // neither
        graph.set("AA", "BB", 100);
    }

    /**
     * 测试sources()方法
     */
    @Test
    @Override
    public void testSources() {
        ConcreteVerticesGraph<String> testGraph = (ConcreteVerticesGraph<String>) emptyInstance();

        // 添加测试顶点
        testGraph.add("Zhengxin Building");
        testGraph.add("Xueyuan Building");
        testGraph.add("Dormitory");
        testGraph.add("Black Store");
        testGraph.add("Playground");

        // 添加测试边
        testGraph.set("Zhengxin Building", "Xueyuan Building", 50);
        testGraph.set("Xueyuan Building", "Black Store", 100);
        testGraph.set("Playground", "Black Store", 1200);

        assertEquals(2, testGraph.sources("Black Store").size());
    }

    /**
     * 测试targets()方法
     */
    @Test
    @Override
    public void testTargets() {
        ConcreteVerticesGraph<String> testGraph = (ConcreteVerticesGraph<String>) emptyInstance();

        // 添加测试顶点
        testGraph.add("A");
        testGraph.add("B");
        testGraph.add("C");
        testGraph.add("D");

        testGraph.set("A", "B", 10);
        testGraph.set("A", "C", 20);
        testGraph.set("A", "D", 30);
        testGraph.set("B", "A", 40);

        assertEquals(3, testGraph.targets("A").size());
    }


    /*
     * Testing Vertex...
     */

    /*
        Testing strategy for Vertex

        testing Vertex contains teo part:

        1. fundamental methods
        2. equals() method

     */

    // tests for operations of Vertex

    /**
     * test Vertex's constructor
     */
    @Test
    public void testVertexConstructor() {
        Vertex<String> vertex = new Vertex<>("A");

        assertEquals("A", vertex.getLABEL());
    }

    /**
     * testAddVertexToNext
     */
    @Test
    public void testAddVertexToNext() {
        Vertex<String> a = new Vertex<>("A");
        Vertex<String> b = new Vertex<>("B");
        Vertex<String> c = new Vertex<>("C");

//        assertTrue(a.addConnectedVertex(b));
//        assertTrue(a.addConnectedVertex(c));
//        assertFalse(a.addConnectedVertex(b));

//        assertEquals(2, a.getConnectedLabels().size());
    }

    /**
     * testRemoveVertexFromNext
     */
    @Test
    public void testRemoveVertexFromNext() {
        Vertex<String> a = new Vertex<>("A");
        Vertex<String> b = new Vertex<>("B");
        Vertex<String> c = new Vertex<>("C");

        a.addConnectedVertex(b, 10);
        a.addConnectedVertex(c, 20);

        assertTrue(a.removeConnectedVertex(b));
        assertFalse(a.removeConnectedVertex(b));
        assertTrue(a.removeConnectedVertex(c));

        assertEquals(0, a.getConnectedLabels().size());
    }

    /**
     * test for equals() method in Vertex
     * this test contains two aspects: truly equals and not equals
     */
    @Test
    public void testVertexEquals() {
        Vertex<String> a = new Vertex<>("A");
        Vertex<String> a2 = new Vertex<>("A");
        Vertex<String> b = new Vertex<>("B");
        Vertex<String> c = new Vertex<>("C");

        // simply equals or not
        assertEquals(a, a2);
        assertNotEquals(a, c);

        // complex equals or not
        a.addConnectedVertex(b, 10);
        a2.addConnectedVertex(b, 20);
        assertNotEquals(a, a2);
    }



}

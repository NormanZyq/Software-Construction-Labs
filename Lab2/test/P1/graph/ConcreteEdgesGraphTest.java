/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package P1.graph;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests for ConcreteEdgesGraph.
 * <p>
 * This class runs the GraphInstanceTest tests against ConcreteEdgesGraph, as
 * well as tests for that particular implementation.
 * <p>
 * Tests against the Graph spec should be in GraphInstanceTest.
 */
public class ConcreteEdgesGraphTest extends GraphInstanceTest {

    /*
     * Provide a ConcreteEdgesGraph for tests in GraphInstanceTest.
     */
    @Override
    public Graph<String> emptyInstance() {
        return new ConcreteEdgesGraph<>();
    }

    /*
     * Testing ConcreteEdgesGraph...
     */

    /**
     * test for toString() method
     * in every ConcreteEdgesGraph object, toString should make an apparent string to represent the object
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
        ConcreteEdgesGraph<String> testGraph = (ConcreteEdgesGraph<String>) emptyInstance();

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
     * 测试添加顶点，分成三组进行测试
     * <ul>
     * <li>
     * 图中没有的顶点：此时调用添加方法的返回值必定为true，并且顶点集合的size增加1
     * </li>
     * <li>
     * 图中已经有的顶点：此时调用添加方法的返回值需要为false，并且图不会受到任何影响
     * </li>
     * <li>
     * 继续添加顶点，图的顶点集合数量应当正确变化
     * </li>
     * </ul>
     */
    @Override
    @Test
    public void testAdd() {
        Graph<String> testGraph = emptyInstance();

        assertTrue("Should be a successful add", testGraph.add("A"));
        assertFalse("Should be a failed add", testGraph.add("A"));

        assertEquals("Now should have 1 vertex", 1, testGraph.vertices().size());

        testGraph.add("B");
        testGraph.add("C");
        assertEquals("Now should have 1 vertexes", 3, testGraph.vertices().size());

    }

    /**
     * 测试设置边
     * 测试分为5种情况
     * <ul>
     * <li>
     *  weight > 0时，添加原本不存在的边，边的数量应该增加1
     * </li>
     * <li>
     *  weight > 0时，添加的边中包含了不存在的顶点，此时会先增加顶点，于是顶点数量应该增加，然后增加边
     * </li>
     * <li>
     *   weight == 0时，输入的source和target都是顶点集合中存在的顶点，则移除这条边，边的数量减1
     * </li>
     * <li>
     *  weight == 0时，输入的source和target之中有一者不存在，则不会添加边，图不会发生任何变化
     * </li>
     * <li>
     *  weight < 0时，会抛出RuntimeException
     * </li>
     * <li>
     *  补充情况：连续作两次weight > 0的调用，边的权应该被修改
     * </li>
     * </ul>
     */
    @Override
    @Test
    public void testSet() {
        ConcreteEdgesGraph<String> testGraph = (ConcreteEdgesGraph<String>) emptyInstance();

        testGraph.add("School");
        testGraph.add("Canteen");
        testGraph.add("Dormitory");

        testGraph.set("A", "B", 100);
        testGraph.set("C", "B", 120);

//        System.out.println(testGraph.toString());

        // 情况1
        testGraph.set("School", "Canteen", 200);
        assertEquals("Now should have 3 edge", 3, testGraph.getEdges().size());

        // 情况2
        assertEquals("Now should have 6 vertexes", 6, testGraph.vertices().size());
        testGraph.set("School", "Playground", 1000);
        assertEquals("Now should have 7 vertexes", 7, testGraph.vertices().size());
        assertEquals("Now should have 4 edge", 4, testGraph.getEdges().size());

        // 情况3
        testGraph.set("School", "Canteen", 0);
        assertEquals("Now should have 3 edge", 3, testGraph.getEdges().size());

        // 情况4
        testGraph.set("School", "Not exist", 0);
        assertEquals("Now should have 3 edge", 3, testGraph.getEdges().size());

        // 补充情况
        testGraph.set("School", "Canteen", 100);
        testGraph.set("School", "Canteen", 200);
        assertEquals(200, testGraph.targets("School").get("Canteen").intValue());


    }

    /**
     * 测试移除顶点，测试分为两组，测试结果需要从三方面判断
     * <ul>
     * <li>
     * 移除一个图中存在的顶点，这个顶点又存在两种情况，一是有边和它相连，二是没有边和它相连
     * </li>
     * <li>
     * 移除一个图中不存在的顶点，这个点可能是一个原始图就没有的点，也需要是已经移除过的点
     * </li>
     * </ul>
     * 移除已经存在的顶点时，会有如下变化
     * <ul>
     * <li>返回值为true</li>
     * <li>顶点集合size减小1</li>
     * <li>与此顶点相连的边都被移除</li>
     * </ul>
     * 移除一个不存在的顶点时，图不应该发生任何变化
     */
    @Override
    @Test
    public void testRemove() {
        ConcreteEdgesGraph<String> testGraph = (ConcreteEdgesGraph<String>) emptyInstance();

        // 添加测试顶点
        testGraph.add("A");
        testGraph.add("B");
        testGraph.add("C");
        testGraph.add("Zhengxin Building");
        testGraph.add("Xueyuan Building");
        testGraph.add("Dormitory");
        testGraph.add("Black Store");
        testGraph.add("Playground");

        // 添加测试边
        testGraph.set("Zhengxin Building", "Xueyuan Building", 50);
        testGraph.set("Xueyuan Building", "Black Store", 100);
        testGraph.set("Playground", "Black Store", 1200);

        // 移除一个存在的顶点，并且没有边和它相连
        assertTrue("Should be a successful removal", testGraph.remove("A"));
        assertEquals("Now should have 7 vertexes", 7, testGraph.vertices().size());

        // 移除一个不存在的顶点，图不可以发生任何变化
        // 这个点已经被移除过了
        assertFalse("Should be a failed removal", testGraph.remove("A"));
        assertEquals("Now should still have 7 vertexes", 7, testGraph.vertices().size());
        // 这个点本来就不存在
        assertFalse("Should be a failed removal", testGraph.remove("D"));
        assertEquals("Now should still have 7 vertexes", 7, testGraph.vertices().size());

        // 移除一个存在的顶点，它的边应该也被移除
        assertEquals("Now should have 3 edges", 3, testGraph.getEdges().size());
        assertTrue("Should be a successful removal", testGraph.remove("Xueyuan Building"));
        assertEquals("Now should still have 6 vertexes", 6, testGraph.vertices().size());
        assertEquals("Now should only have 1 edges", 1, testGraph.getEdges().size());


    }

    @Test
    @Override
    public void testSources() {
        ConcreteEdgesGraph<String> testGraph = (ConcreteEdgesGraph<String>) emptyInstance();

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

    @Test
    @Override
    public void testTargets() {
        ConcreteEdgesGraph<String> testGraph = (ConcreteEdgesGraph<String>) emptyInstance();

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
     * Testing Edge...
     */

    /*
        Testing strategy for Edge

        testing Edge contains teo part:

        1. fundamental methods
        2. equals() method

     */

    // tests for operations of Edge

    /**
     * test Edge's fundamental methods, including constructor and three getters
     */
    @Test
    public void testEdge() {
        Edge<String> edge = new Edge<>("A", "B", 10);

        assertEquals("A", edge.getSOURCE());    // source

        assertEquals("B", edge.getTARGET());    // target

        assertEquals(10, edge.getWEIGHT());     // weight
    }

    /**
     * test for equals() method in Edge
     * this test contains two aspects: truly equals and not equals
     */
    @Test
    public void testEdgeEquals() {
        Edge<String> edge1 = new Edge<>("A", "B", 10);
        Edge<String> edge2 = new Edge<>("A", "B", 10);
        Edge<String> edge3 = new Edge<>("B", "C", 10);

        assertEquals(edge1, edge2);

        assertNotEquals(edge1, edge3);
    }



}

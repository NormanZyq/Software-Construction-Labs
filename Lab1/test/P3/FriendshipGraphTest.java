package P3;

import P3.FriendshipGraph;
//import P3.Helper;
import P3.Person;
import org.junit.Test;

import static org.junit.Assert.*;

public class FriendshipGraphTest {

    @Test
    public void addVertexTest() {
        FriendshipGraph graph = new FriendshipGraph();
        Person rachel = new Person("rachel");
        Person ben = new Person("ben");

        graph.addVertex(rachel);
        graph.addVertex(rachel);
        assertTrue("rachel's set should not be empty", !graph.getFriendship().get(graph.getPerson2Index().get(rachel)).isEmpty());
        assertNull("ben's set should be empty", graph.getFriendship().get(graph.getPerson2Index().get(ben)));
    }

    @Test
    public void addEdgeTest() {
        FriendshipGraph graph = new FriendshipGraph();
        Person rachel = new Person("rachel");
        Person ben = new Person("ben");
        Person ross = new Person("ross");

        // add vertexes
        graph.addVertex(rachel);
        graph.addVertex(ben);
        graph.addVertex(ross);

        // add edges
        graph.addEdge(rachel, ross);
        graph.addEdge(ross, ben);

        assertEquals("rachel can access to ross", 1, graph.getDistance(rachel, ross));
        assertEquals("ben cannot access to ross", -1, graph.getDistance(ben, ross));
//        assertEquals("rachel can access to ross", graph.getFriendship().get(graph.getPerson2Index().get(rachel));

    }

    /**
     * Two tests for undirected graph
     */
    @Test
    public void friendshipTest1() {
        FriendshipGraph graph = new FriendshipGraph();
        Person rachel = new Person("Rachel");
        Person ross = new Person("Ross");


        Person ben = new Person("Ben");
        Person kramer = new Person("Kramer");
        graph.addVertex(rachel);
        graph.addVertex(ross);
        graph.addVertex(ben);
        graph.addVertex(kramer);
        graph.addEdge(rachel, ross);
        graph.addEdge(ross, rachel);
        graph.addEdge(ross, ben);
        graph.addEdge(ben, ross);

        assertEquals(1, graph.getDistance(rachel, ross));
        assertEquals(2, graph.getDistance(rachel, ben));
        assertEquals(0, graph.getDistance(rachel, rachel));
        assertEquals(-1, graph.getDistance(rachel, kramer));

    }


    @Test
    public void friendshipGraphTest2() {
        FriendshipGraph graph = new FriendshipGraph();

        Person rachel = new Person("Rachel");
        Person ross = new Person("Ross");

        Person ben = new Person("Ben");
        Person kramer = new Person("Kramer");

        Person a1 = new Person("a1");
        Person b1 = new Person("b1");
        Person c1 = new Person("c1");
        Person alone = new Person("alone");

        graph.addVertex(rachel);
        graph.addVertex(ross);
        graph.addVertex(ben);
        graph.addVertex(kramer);
        graph.addVertex(a1);
        graph.addVertex(b1);
        graph.addVertex(c1);
        graph.addVertex(alone);
        graph.addEdge(rachel, ross);
        graph.addEdge(ross, rachel);
        graph.addEdge(ross, ben);
        graph.addEdge(ben, ross);
        graph.addEdge(rachel, a1);
        graph.addEdge(a1, rachel);
        graph.addEdge(b1, a1);
        graph.addEdge(a1, b1);
        graph.addEdge(c1, ross);
        graph.addEdge(ross, c1);
        graph.addEdge(kramer, c1);
        graph.addEdge(c1, kramer);

        graph.printFriendship();
        
        assertEquals(1, graph.getDistance(rachel, ross));
        assertEquals(2, graph.getDistance(rachel, ben));
        assertEquals(0, graph.getDistance(rachel, rachel));
        assertEquals(3, graph.getDistance(rachel, kramer));
        assertEquals(2, graph.getDistance(ben, rachel));
        assertEquals(3, graph.getDistance(kramer, rachel));
        assertEquals(0, graph.getDistance(kramer, kramer));
        assertEquals(-1, graph.getDistance(alone, rachel));
    }


    /**
     * test for directed graph
     */
    @Test
    public void friendshipGraphTest3() {
        FriendshipGraph graph = new FriendshipGraph();

        Person rachel = new Person("Rachel");
        Person ross = new Person("Ross");

        Person ben = new Person("Ben");
        Person kramer = new Person("Kramer");

        Person a1 = new Person("a1");
        Person b1 = new Person("b1");
        Person c1 = new Person("c1");
        Person alone = new Person("alone");

        graph.addVertex(rachel);
        graph.addVertex(ross);
        graph.addVertex(ben);
        graph.addVertex(kramer);
        graph.addVertex(a1);
        graph.addVertex(b1);
        graph.addVertex(c1);
        graph.addVertex(alone);
        graph.addEdge(rachel, ross);
        graph.addEdge(ross, rachel);
//        graph.addEdge(ross, ben);
        graph.addEdge(ben, ross);
//        graph.addEdge(rachel, a1);
        graph.addEdge(a1, rachel);
        graph.addEdge(b1, a1);
//        graph.addEdge(a1, b1);
        graph.addEdge(c1, ross);
        graph.addEdge(ross, c1);
//        graph.addEdge(kramer, c1);
        graph.addEdge(c1, kramer);

        graph.printFriendship();

        assertEquals(1, graph.getDistance(rachel, ross));
        assertEquals(1, graph.getDistance(ross, c1));

        // directed graph, so ben can access to ross but ross cannot access to ben
        assertEquals(1, graph.getDistance(ben, ross));
        assertEquals(-1, graph.getDistance(ross, ben));

    }
}

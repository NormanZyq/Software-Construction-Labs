package P2;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("Duplicates")
public class FriendshipTest {

    @Test
    public void testFriendshipGraph() {
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

    /**
     * test for directed graph
     */
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
        graph.addEdge(ben, ross);
        graph.addEdge(a1, rachel);
        graph.addEdge(b1, a1);
        graph.addEdge(c1, ross);
        graph.addEdge(ross, c1);
        graph.addEdge(c1, kramer);

        assertEquals(1, graph.getDistance(rachel, ross));
        assertEquals(1, graph.getDistance(ross, c1));

        // directed graph, so ben can access to ross but ross cannot access to ben
        assertEquals(1, graph.getDistance(ben, ross));
        assertEquals(-1, graph.getDistance(ross, ben));

    }

}

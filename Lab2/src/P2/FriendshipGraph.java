package P2;

import P1.graph.ConcreteEdgesGraph;

import java.util.*;

import static org.junit.Assert.assertEquals;


public class FriendshipGraph {

    private final ConcreteEdgesGraph<Person> friendshipGraph = new ConcreteEdgesGraph<>();

    private int vertexCount;

    // AF
    //  This class represents a friendship graph in society.
    //  It is implemented by concrete edges graph, and the graph has its vertexes and edges.
    //  Every edge represents a connection between two persons.
    //  The two persons are the source and target vertex as the start and the end of an edge.

    // RI
    //  true

    // safety from exposure
    //  the mutator in this class can only modify the friendshipGraph object,
    //  In that object's instance methods, every change followed up with a checkRep() method

    /**
     * 为朋友圈添加一个顶点
     *
     * @param person 作为顶点的人的对象
     * @return 添加了新的顶点则返回true，否则为false
     */
    public boolean addVertex(Person person) {
        this.vertexCount++;
        return friendshipGraph.add(person);
    }

    /**
     * 为图添加一条边
     *
     * @param p1 出发点的人
     * @param p2 指向的人
     * @return 添加成功返回true，添加失败或其他情况返回false
     */
    public boolean addEdge(Person p1, Person p2) {
        /*
            根据lab1的要求，这里不应该自动创建新的顶点
            如果传入的p1、p2都存在才能添加边，否则不能添加，返回false
         */
        if (!friendshipGraph.vertices().containsAll(Arrays.asList(p1, p2))) return false;

        // 此时p1 p2肯定都在顶点列表中，如果set方法返回值为0说明之前没有这条边，如果不是0，则已经存在了这条边
        return friendshipGraph.set(p1, p2, 1) == 0;
    }

    /**
     * calculate the distance from p1 to p2
     * @param p1    start person
     * @param p2    target person
     * @return      the int value represents distance from p1 to p2.
     *              -1 if p1 cannot access to p2.
     *              0 if p1 == p2
     */
    public int getDistance(Person p1, Person p2) {
        if (p1 == p2) return 0;
        class Record {
            private Person person;
            private int distance = 0;

            private Record(Person person) {
                this.person = person;
            }

            private Record(Person person, int distance) {
                this.person = person;
                this.distance = distance;
            }

            private Person getPerson() {
                return person;
            }

            private int getDistance() {
                return distance;
            }
        }
        Set<Person> visitedSet = new HashSet<>();
        LinkedList<Record> queue = new LinkedList<>();

        Record r1 = new Record(p1);
//        Record r2 = new Record(p2);

        queue.addLast(r1);

        // 广度优先搜索
        while (!queue.isEmpty()) {
            Record sourceRecord = queue.pollFirst();

            // 遍历当前出队顶点所有的邻接点
            assert sourceRecord != null;
            Set<Person> visitingRow = friendshipGraph.targets(sourceRecord.getPerson()).keySet();
            for (Person p : visitingRow) {
                // 只有在未访问时才进行访问操作
                if (!visitedSet.contains(p)) {
                    visitedSet.add(p);      // 标记已访问
                    // 因为是第一次访问，所以队列中一定没有这个person，新建一个record对象，并设置距离，然后进队
                    Record newVisited = new Record(p, sourceRecord.getDistance() + 1);
                    queue.addLast(newVisited);
                    if (p2 == p) {
                        return newVisited.getDistance();
                    }
                }
            }
        }

        return -1;

    }

    /**
     * 对外暴露当前交际圈中的总人数
     * @return  当前交际圈总人数
     */
    public int peopleCount() {
        return vertexCount;
    }

    public static void main(String[] args) {
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
}






















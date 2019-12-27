package P3;

import java.util.*;

/**
 * 朋友圈的图类
 */
public class FriendshipGraph {
    private int vertexCount;

    private HashMap<Integer, List<Helper>> friendship = new HashMap<>();

//    private List<Helper> allHelpers = new ArrayList<>();

    private HashMap<Person, Helper> person2Helper = new HashMap<>();    // use hash map to improve performance

    private HashMap<Person, Integer> person2Index = new HashMap<>();    // use hash map to convert person to its index in List friendship

    /**
     *
     */
    public FriendshipGraph() {
        Person.ALL_PEOPLE_NAMES.clear();
    }

    /**
     * 为朋友圈添加一个顶点
     * @param person    作为顶点的人的对象
     * @return  添加了新的顶点则返回true，否则为false
     */
    public boolean addVertex(Person person) {
        Helper helper = new Helper(person);     // 用当前person实例化一个helper

        if (person2Index.get(person) != null) {
            System.out.println("Person name: " + person.getName() + ", hash code: " + person.hashCode() + " 的顶点已存在");
            return false;     // 如果这个person已经存在，则不要创建并且返回false
        }
        List<Helper> newRow = new ArrayList<>();    // 一个新的helper列表，用来存放与当前person的关系

        newRow.add(helper);     // 添加新的helper到这个list，即0号元素就是当前用户的helper

        // 添加到两个辅助map
        person2Helper.put(person, helper);
        person2Index.put(person, vertexCount);

        // 添加到关系圈
        friendship.put(vertexCount, newRow);

        this.vertexCount++;     // 顶点计数+1
        return true;    // 运行到此表示添加成功
    }

    /**
     * 刷新已经从friendship的map里修改的visit和distance变量
     * visited置为false
     * distance置为0
     */
    private void resetDistanceVisited() {
        for (List<Helper> helpersInMap : friendship.values()) {
            for (Helper helper : helpersInMap) {
                helper.makeUnvisited();
                helper.setDistance(0);
            }
        }
    }

    /**
     * 为图添加一条边
     * @param p1    出发点的人
     * @param p2    指向的人
     * @return      添加成功返回true，添加失败或其他情况返回false
     */
    public boolean addEdge(Person p1, Person p2) {
        if (p1 == p2) throw new RuntimeException("不能向同一个人身上添加边");

        // use hash map to find p1 in the graph
        List<Helper> currentRow = friendship.get(person2Index.get(p1));
        if (currentRow == null) return false;

        return currentRow.add(new Helper(p2));
    }

    public int getDistance(Person p1, Person p2) {
        resetDistanceVisited();

        LinkedList<Helper> queue = new LinkedList<>();

        Helper h1 = person2Helper.get(p1);
        Helper h2 = person2Helper.get(p2);

        queue.add(h1);

        while (!queue.isEmpty()) {
            Helper helper = queue.pollFirst();  // dequeue
            if (helper == null) return -1;

            List<Helper> visitingRow = friendship.get(person2Index.get(helper.getPerson()));

            for (Iterator<Helper> iterator = visitingRow.iterator(); iterator.hasNext(); ) {
//                iterator.next();
                Helper h = iterator.next();
//                System.out.println(h.getPerson().getName() + " isVisited = " + h.isVisited());
                if (!h.isVisited()) {   // not visited
                    h.makeVisited();    // make visited
                    h.setDistance(helper.getDistance() + 1);    // set distance as the distance from the starter
                    queue.addLast(h);
                    if (h.getPerson() == p2) {
                        return h.getDistance() - 1;     // find the guy. the real distance should -1 because one more was added in the starter
                    }
                }
            }
        }

//        return h2.getDistance() - 1;
        return -1;  // cannot find the guy and return -1;
    }

    /**
     * 获得顶点个数
     * @return  顶点个数
     */
    public int getVertexCount() {
        return vertexCount;
    }

    /**
     * 获得朋友圈的map
     * @return  朋友圈的map
     */
    public HashMap<Integer, List<Helper>> getFriendship() {
        return friendship;
    }

    public HashMap<Person, Helper> getPerson2Helper() {
        return person2Helper;
    }

    public HashMap<Person, Integer> getPerson2Index() {
        return person2Index;
    }

    /**
     * 根据朋友圈的map作为邻接表打印出关系图
     */
    public void printFriendship() {
        for (int index = 0; index < vertexCount; index++) {
            for (Iterator<Helper> iterator = friendship.get(index).iterator(); iterator.hasNext(); ) {
                Helper helper = iterator.next();
                System.out.print(helper.getPerson().getName());
                if (iterator.hasNext()) {
                    System.out.print("--->");
                }
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
//        new frigt
    }
}

/**
 * 作为辅助类帮助朋友圈类来工作而不影响Person类
 */
class Helper {
    private Person person;      // 组合设计的person

    /**
     * 距离变量，距离表示从访问点开始，到此对象的边数
     */
    private int distance;
    private boolean visited;    // 访问标记，已访问为true，未访问为false
//    private int indexInList;

    /**
     * 通过person对象构造helper对象
     * @param person    组合进这个helper对象的person对象
     */
    Helper(Person person) {
        this.person = person;
        distance = 0;   // 初始化距离为0
//        distance = Integer.MAX_VALUE;
        this.visited = false;   // 初始化访问标记为false
    }

    /**
     * 获取当前helper对象中的person对象
     * @return  当前helper对象中的person对象
     */
    public Person getPerson() {
        return person;
    }

    void setDistance(int distance) {
        this.distance = distance;
    }

    int getDistance() {
        return distance;
    }

    void makeUnvisited() {
        this.visited = false;
    }

    void makeVisited() {
        this.visited = true;
    }

    boolean isVisited() {
        return visited;
    }
}
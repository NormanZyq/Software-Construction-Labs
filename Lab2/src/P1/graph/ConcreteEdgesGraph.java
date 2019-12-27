package P1.graph;

import java.util.*;

/**
 * An implementation of Graph.
 *
 * <p>PS2 instructions: you MUST use the provided rep.
 */
public class ConcreteEdgesGraph<L> implements Graph<L> {

    private final Set<L> vertices = new HashSet<>();
    private final List<Edge<L>> edges = new ArrayList<>();

    // Abstraction function:
    //   This class represents a graph, implemented with concrete edges.

    // Representation invariant:
    //   1. There should be no same edges in edges list, i.e. there shouldn't be
    //   two edges that their sources and targets are the same.
    //   2. Source and target in each edge must also be in vertices set.

    // Safety from rep exposure:
    //   Every observer in this class does not return the original value to the caller,
    //   but instead, they return a copied version.(getEdges, vertices, sources, targets)
    //   add and remove methods are under control with the checkRep() method, so
    //	 callers can't mutate by their mind.

    // checkRep
    private void checkRep() {
        // check edges --- no same edges
        Set<L> sources = new HashSet<>();
        Set<L> targets = new HashSet<>();
        for (Edge<L> edge : edges) {
            sources.add(edge.getSOURCE());
            targets.add(edge.getTARGET());
        }
        if (new HashSet<>(edges).size() != edges.size()) throw new RuntimeException("存在起点和终点都相同的边");
        if (!vertices.containsAll(sources) || !vertices.containsAll(targets)) throw new RuntimeException("存在顶点未被包含在顶点列表");
    }

    /**
     * get a copied version of edges
     *
     * @return a copied version of edges
     */
    public List<Edge<L>> getEdges() {
        return new ArrayList<>(this.edges);
    }

    /**
     * add a new vertex to the vertices set
     *
     * @param vertex label for the new vertex
     * @return true if succeed in adding a new vertex, false otherwise(e.g. the set already has the vertex)
     */
    @Override
    public boolean add(L vertex) {
        boolean success = vertices.add(vertex);
        checkRep();
        return success;
    }

    /**
     * 为起点和终点设置一条边，没有source或target时自动创建
     *
     * @param source label of the SOURCE vertex
     * @param target label of the TARGET vertex
     * @param weight nonnegative WEIGHT of the edge
     * @return 旧的边权
     */
    @Override
    public int set(L source, L target, int weight) {
        if (weight < 0) {
            throw new RuntimeException("输入的权不可为负数");
        }
        // 输入的权合法
        // 寻找输入的起点和终点的边
        int oldWeight = 0;
        boolean found = false;
        for (Edge<L> edge : edges) {
            if (edge.getSOURCE().equals(source) && edge.getTARGET().equals(target)) {
                // 找到了这条指定的边
                found = true;
                oldWeight = edge.getWEIGHT();   // 记录返回值：旧的权
                if (weight == 0) {
                    // 传入的权为0，移除这个边
                    edges.remove(edge);
                } else {
                    // 传入的权>0，修改权值
                    set(source, target, 0);     // 先移除这条边，递归就行了，此时绝对不会再走这条路线
                    set(source, target, weight);       // 再设置这条边，也不可能走这条路线，因为边已经被移除
//                    edge.setWeight(WEIGHT);
                }
                break;
            }
        }
        if (!found) {
            // 未找到对应的边
            // 可能连顶点都没有
            add(source);
            add(target);
            // 新建一条边
            if (weight != 0) {
                Edge<L> newEdge = new Edge<>(source, target, weight);
                edges.add(newEdge);
            }
        }

        checkRep();
        return oldWeight;
    }

    /**
     * remove the vertex from the set
     *
     * @param vertex label of the vertex to remove
     * @return true if succeed in remove, false otherwise
     */
    @Override
    public boolean remove(L vertex) {
        if (!vertices.remove(vertex)) {
            checkRep();
            return false;
        }

        for (int i = 0, size = edges.size(); i < size; i++) {
            Edge<L> edge = edges.get(i);
            if (vertex.equals(edge.getSOURCE()) || vertex.equals(edge.getTARGET())) {
                edges.remove(edge);     // 删除含有这个点的边
                i--;
                size--;
            }
        }

        checkRep();
        return true;
    }

    /**
     * get a vertices copy
     *
     * @return a copied version of vertices set
     */
    @Override
    public Set<L> vertices() {
        return new HashSet<>(this.vertices);
    }

    /**
     * 创建一份source和与其相连的边的权构成的map的副本
     *
     * @param target a label
     * @return source和与其相连的边的权构成的map的副本
     */
    @Override
    public Map<L, Integer> sources(L target) {
        Map<L, Integer> sources = new HashMap<>();

        for (Edge<L> edge : edges) {
            if (target.equals(edge.getTARGET())) {
                sources.put(edge.getSOURCE(), edge.getWEIGHT());
            }
        }

        return sources;
    }

    /**
     * get the edges' labels and weights which has the SOURCE in argument
     *
     * @param source a label
     * @return a map that contains the label and WEIGHT of each satisfied edge
     */
    @Override
    public Map<L, Integer> targets(L source) {
        Map<L, Integer> targets = new HashMap<>();

        for (Edge<L> edge : edges) {
            if (source.equals(edge.getSOURCE())) {
                targets.put(edge.getTARGET(), edge.getWEIGHT());
            }
        }

        return targets;
    }

    /**
     * toString method, showing the detail of this class
     *
     * @return a string that can represent this object
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Iterator<L> sourceIterator = vertices.iterator(); sourceIterator.hasNext(); ) {
            L source = sourceIterator.next();     // get SOURCE vertex
            sb.append(source);
            Map<L, Integer> targets = this.targets(source);     // get targets by SOURCE
            for (Map.Entry<L, Integer> target : targets.entrySet()) {
                sb.append("--").append(target.getValue()).append("->").append(target.getKey());
            }
            if (sourceIterator.hasNext()) sb.append("\n");
        }
//
//
//        sb.append("顶点有：{\n");
//        for (L vertex : vertices) {
//            sb.append("\t").append(vertex).append("\n");
//        }
//        sb.append("}\n");
//
//        sb.append("边有：{\n");
//        for (Edge<L> edge : edges) {
//            sb.append(edge.toString()).append("\n");
//        }
        return sb.toString();

    }
}

/**
 * Edge used in ConcreteEdgesGraph
 * Immutable.
 * This class is internal to the rep of ConcreteEdgesGraph.
 *
 * <p>PS2 instructions: the specification and implementation of this class is
 * up to you.
 */
class Edge<L> {

    private final L SOURCE;

    private final L TARGET;

    private final int WEIGHT;

    /**
     * constructor for Edge
     *
     * @param SOURCE SOURCE vertex label
     * @param TARGET TARGET vertex label
     * @param WEIGHT edge's WEIGHT between SOURCE and TARGET
     */
    public Edge(L SOURCE, L TARGET, int WEIGHT) {
        this.SOURCE = SOURCE;
        this.TARGET = TARGET;
        this.WEIGHT = WEIGHT;
        checkRep();
    }

    // Abstraction function:
    //   This class represents an edge in a real graph.
    //   It has source label and target label(both are vertexes) and its weight.

    // Representation invariant:
    //   source and target can not be the same, and weight can not be zero.

    // Safety from rep exposure:
    //   Except the constructor, the other methods are all observers.
    //   3 getters return the final value, which means they are safe from exposure.
    //   2 rest are equals and hasCode don't change any value in this class and return values are all immutable.

    /**
     * get the source label
     * @return  source label
     */
    public L getSOURCE() {
        return SOURCE;
    }

    /**
     * get the target label
     * @return  target label
     */
    public L getTARGET() {
        return TARGET;
    }

    /**
     * get the weight between source and target
     * @return  weight between source and target
     */
    public int getWEIGHT() {
        return WEIGHT;
    }

    // checkRep
    private void checkRep() {
        if (this.SOURCE == this.TARGET) {
            throw new RuntimeException("起点和终点不能一样！");
        }
        if (this.WEIGHT <= 0) {
            throw new RuntimeException("边的权不可为0！");
        }
    }

    /**
     * 覆盖的equals方法，当source和target和weight都相同时才为相同
     *
     * @param o 另一个对象
     * @return 如果上述内容均为相同的，则返回true，否则返回false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Edge<?> edge = (Edge<?>) o;

        if (!SOURCE.equals(edge.SOURCE)) return false;
        return TARGET.equals(edge.TARGET);
    }

    @Override
    public int hashCode() {
        int result = SOURCE.hashCode();
        result = 31 * result + TARGET.hashCode();
        return result;
    }
}

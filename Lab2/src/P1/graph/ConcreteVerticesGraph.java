/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package P1.graph;

import java.util.*;

/**
 * An implementation of Graph.
 *
 * <p>PS2 instructions: you MUST use the provided rep.
 */
public class ConcreteVerticesGraph<L> implements Graph<L> {

    private final List<Vertex<L>> vertices = new ArrayList<>();

    // Abstraction function:
    //   This class represents a graph which is implemented with concrete vertexes.

    // Representation invariant:
    //   No two vertexes have the same LABEL

    // Safety from rep exposure:
    //   All provided methods's result will be checked by checkRep() method, so it's safe.
    //   There's no mutator can avoid the check in this class

    // checkRep
    private void checkRep() {
        Set<L> vertexLabels = new HashSet<>();
        for (Vertex<L> vertex : vertices) {
            vertexLabels.add(vertex.getLABEL());
        }
        if (vertexLabels.size() != vertices.size()) {
            throw new RuntimeException("图不能存在重复的顶点！");
        }
    }

    /**
     * add a new vertex to the vertices list
     *
     * @param vertex LABEL for the new vertex
     * @return true if succeeded, false otherwise
     */
    @Override
    public boolean add(L vertex) {
        for (Vertex<L> v : vertices) {
            if (vertex.equals(v.getLABEL())) {
                return false;
            }
        }
        Vertex<L> v = new Vertex<>(vertex);
        vertices.add(v);    // add vertex
        checkRep();
        return true;
    }

    /**
     * 添加/设置边，
     * 当weight大于0时，如果source和target不存在，则创建这条边并设置权；
     * 如果source和target都存在，则直接设置边权
     * <p>
     * 当weight是0时，如果source和target有一者不存在，则不发生任何变化
     * 如果都存在，则移除这条边
     * <p>
     * 当weight<0时，抛出异常
     *
     * @param source LABEL of the source vertex
     * @param target LABEL of the target vertex
     * @param weight nonnegative weight of the edge
     * @return the old weight if this existed before, or 0 if it did not exist.
     */
    @Override
    public int set(L source, L target, int weight) {
        if (weight < 0) throw new RuntimeException("边权不可为负");
        int returnValue;
        if (weight > 0) {
            // weight > 0 设置权
            // 找到source对应的点对象就获得该对象，否则新建
            Vertex<L> sourceVertex = vertices.stream().filter(vertex -> source.equals(vertex.getLABEL())).findFirst().orElse(new Vertex<>(source));
            // 找到target就返回，否则新建
            Vertex<L> targetVertex = vertices.stream().filter(nextVertex -> target.equals(nextVertex.getLABEL())).findFirst().orElse(new Vertex<>(target));

            if (!vertices.contains(sourceVertex)) vertices.add(sourceVertex);
            if (!vertices.contains(targetVertex)) vertices.add(targetVertex);

            // 记录旧的权值
            int oldWeight = sourceVertex.getWeightTo(targetVertex.getLABEL());

            // 添加target到source的next集合
//            sourceVertex.getNext().add(targetVertex);
            sourceVertex.addConnectedVertex(targetVertex, weight);

            // 设置权
//            sourceVertex.getConnectedVertexes().put(targetVertex.getLABEL(), weight);

//            if (oldWeight == null) returnValue = 0;
            returnValue = oldWeight;

        } else {
            // weight == 0 移除边
            // 找到source对应的点对象就获得该对象，否则null
            Vertex<L> sourceVertex = vertices.stream().filter(vertex -> source.equals(vertex.getLABEL())).findFirst().orElse(null);
            // 找到target就返回，否则null
            Vertex<L> targetVertex = vertices.stream().filter(nextVertex -> target.equals(nextVertex.getLABEL())).findFirst().orElse(null);

            if (sourceVertex == null || target == null) {
                // 有一个为null，就不改变图
                returnValue = 0;
            } else {
                // 记录旧的边权值
                if (targetVertex == null) {
                    // null说明便不存在
                    returnValue = 0;
                } else {
                    int oldWeight = sourceVertex.getWeightTo(targetVertex.getLABEL());
                    // 移除边
//                sourceVertex.getNext().remove(targetVertex);    // 移除邻接点
                    sourceVertex.removeConnectedVertex(targetVertex);
//                    sourceVertex.getConnectedVertexes().remove(targetVertex.getLABEL());  // 移除边
                    returnValue = oldWeight;
                }
            }
        }

        checkRep();
        return returnValue;
    }

    /**
     * 移除顶点和与其相连的边
     *
     * @param vertex LABEL of the vertex to remove
     * @return 移除成功返回true，没有找到点返回false
     */
    @Override
    public boolean remove(L vertex) {
        Vertex<L> removeVertex = vertices.stream().filter(vertexInList -> vertex.equals(vertexInList.getLABEL())).findFirst().orElse(null);
        if (removeVertex == null) {
            // 没有找到这个顶点，图不发生任何变化
            return false;
        } else {
            // 找到了顶点，移除顶点和其相连的边
            for (Vertex<L> source : vertices) {
                // 移除指向它的边
                source.removeConnectedVertex(removeVertex);
            }

            vertices.remove(removeVertex);      // 从列表中移除这个点，它指出去的边也没了
            return true;
        }
    }

    /**
     * 获得这个图的所有顶点组成的集合
     *
     * @return 这个图所有顶点组成的集合
     */
    @Override
    public Set<L> vertices() {
        Set<L> vertexSet = new HashSet<>();

        for (Vertex<L> vertex : vertices) {
            vertexSet.add(vertex.getLABEL());
        }
        return vertexSet;
    }

    /**
     * 获得指向label是target的source顶点集合，以map表示，key为source的label，value为边权值
     *
     * @param target a LABEL
     * @return 指向label是target的source顶点相关内容，包括source的label和权值
     */
    @Override
    public Map<L, Integer> sources(L target) {
        Map<L, Integer> sources = new HashMap<>();

        for (Vertex<L> vertex : vertices) {
            // 遍历next里的元素，取出label是target的顶点存进map
            if (vertex.getConnectedLabels().contains(target)) {
                sources.put(vertex.getLABEL(), vertex.getWeightTo(target));
            }
//            for (L next : vertex.getConnectedLabels()) {
//                if (target.equals(next)) {
//                    sources.put(vertex.getLABEL(), vertex.getConnectedVertexes().get(target));
//                }
//            }
        }

        return sources;

    }

    /**
     * 获得label为source的顶点指向的顶点的集合，以map表示，key为source的label，value为边权值
     *
     * @param source a LABEL
     * @return 来源的label为source的顶点指向的target顶点相关内容，包括source的label和权值
     */
    @Override
    public Map<L, Integer> targets(L source) {
        Vertex<L> sourceVertex = null;
        for (Vertex<L> vertex : vertices) {
            if (source.equals(vertex.getLABEL())) {
                sourceVertex = vertex;
                break;
            }
        }

        if (sourceVertex == null) return new HashMap<>();
        else return sourceVertex.getConnectedVertexes();

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Iterator<Vertex<L>> vertexIterator = vertices.iterator(); vertexIterator.hasNext(); ) {
            Vertex<L> source = vertexIterator.next();
            sb.append(source.getLABEL());

            Map<L, Integer> targets = this.targets(source.getLABEL());

            for (Map.Entry<L, Integer> target : targets.entrySet()) {
                sb.append("--").append(target.getValue()).append("->").append(target.getKey());
            }

            if (vertexIterator.hasNext()) sb.append("\n");
        }
        return sb.toString();
    }

}

/**
 * 顶点类的实现
 * <p>
 * Mutable.
 * This class is internal to the rep of ConcreteVerticesGraph.
 *
 * <p>PS2 instructions: the specification and implementation of this class is
 * up to you.
 */
class Vertex<L> {

    private final L LABEL;

    private final Set<Vertex<L>> next = new HashSet<>();

    private final Map<L, Integer> nextTargets = new HashMap<>();

    // Abstraction function:
    //   This class represents a concrete vertex which has its LABEL and the other vertexes connecting to this vertex

    // Representation invariant:
    //   1. next set cannot contain a vertex which has the same label as this.label.
    //   2. nextTargets' param "L" and next's L param should be the same.

    // Safety from rep exposure:
    //  callers cannot modify any value ans they want.
    //  All mutator in this class contains checkRep() method, so any value will not be illegal.
    //  the getConnectedVertexes() observer returns a copied version of the map
    //  in the class, so the original will not be changed as well.

    /**
     * constructor of this class
     * requires a LABEL as L type
     *
     * @param LABEL LABEL(or name) that represents this vertex
     */
    public Vertex(L LABEL) {
        this.LABEL = LABEL;
        checkRep();
    }

    /**
     * getter of LABEL
     *
     * @return LABEL
     */
    public L getLABEL() {
        return LABEL;
    }

    /**
     * 获取邻接点的标签
     * @return  返回邻接点标签集合
     */
    public Set<L> getConnectedLabels() {
        Set<L> labels = new HashSet<>();
        for (Vertex<L> vertex : next) {
            labels.add(vertex.LABEL);
        }
        checkRep();
        return labels;
    }

    /**
     * this method only append a vertex to the next set
     * @param vertex    new vertex which need to be added
     * @return          true if succeeded in appending, false otherwise
     */
    private boolean addVertexToNext(Vertex<L> vertex) {
        return this.next.add(vertex);
    }

    /**
     * todo 有时候checkRep未通过，有时候又通过
     *
     * this method will add a new vertex to this vertex,
     * and mark them as a pair of connected vertexes.
     * the edge between these vertexes is also set.
     *
     * @param vertex    new vertex to connect
     * @param weight    weight of edge
     * @return          true if succeeded, false otherwise
     */
    public boolean addConnectedVertex(Vertex<L> vertex, int weight) {
        this.nextTargets.put(vertex.getLABEL(), weight);
        boolean success = addVertexToNext(vertex);
        checkRep();
        return success;
    }

    /**
     * remove the vertex from the connected vertexes, as well as removing the edge
     * @param vertex    vertex to remove
     * @return          true if succeeded, false otherwise
     */
    public boolean removeConnectedVertex(Vertex<L> vertex) {
        this.nextTargets.remove(vertex.getLABEL());
        boolean success = this.next.remove(vertex);

        checkRep();

        return success;
    }

    /**
     * return a copied version of the connected vertexes and weight between this object and the vertex in map
     *
     * @return a map containing connected vertex's label and the weight between these vertexes (copied version)
     */
    public Map<L, Integer> getConnectedVertexes() {
        return new HashMap<>(nextTargets);
    }

    public int getWeightTo(L vertexLabel) {
        if (this.nextTargets.get(vertexLabel) == null) {
            return 0;
        } else return this.nextTargets.get(vertexLabel);
    }

    // checkRep
    private void checkRep() {
        // get all L in next set
        Set<L> nextLabels = new HashSet<>();
        for (Vertex<L> vertex : next) {
            nextLabels.add(vertex.LABEL);
        }

        if (nextLabels.contains(this.LABEL)) throw new RuntimeException("不能有顶点自己连自己");

        // get key set of map
        Set<L> keySet = nextTargets.keySet();
//        if (!keySet.containsAll(nextLabels) || !nextLabels.containsAll(keySet)) throw new RuntimeException("邻接的顶点不正确");
        if (keySet.size() != nextTargets.size()) throw new RuntimeException("邻接的顶点数目不正确");

    }

    /**
     * equals() method created by IDEA
     * only when the two labels, two next sets and the two nextTargets maps are equal, this method returns true
     *
     * @param o another object to compare if equals to this object
     * @return only when the two labels, two next sets and the two nextTargets maps are equal,
     * this method returns true
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vertex<?> vertex = (Vertex<?>) o;

        if (!LABEL.equals(vertex.LABEL)) return false;
        if (!next.equals(vertex.next)) return false;
        return nextTargets.equals(vertex.nextTargets);
    }

    /**
     * hashCode() method created by IDEA
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        int result = LABEL.hashCode();
        result = 31 * result + next.hashCode();
        result = 31 * result + nextTargets.hashCode();
        return result;
    }
}

package abs;

import java.util.*;

public class ConcreteCircularOrbit<L extends PhysicalObject, E extends PhysicalObject> implements CircularOrbit<L, E>, Iterable<E> {

    /**
     * factory to create a track
     *
     * @param level  level of track
     * @param radius radius of track
     * @return a new track
     */
    public static Track createTrack(int level, double radius) {
        return new Track(level, radius);
    }

    private L centerObject = null;

    private Set<ConcreteRelation<L, E>> relationsWithCenter = new HashSet<>();

    private Set<ConcreteRelation<E, E>> relations = new HashSet<>();

    private Set<E> allObjects = new HashSet<>();

    protected Map<Track, List<E>> objectsOnTrack = new HashMap<>();

    protected Map<Integer, E> objectOnTrack = new HashMap<>();

    protected Map<Integer, Track> tracks = new HashMap<>();

    private Map<E, Position> positionMap = new HashMap<>();

    void checkRep() {
        // todo
    }

    @Override
    public L addCenterObject(L centerObject) {
        L old = this.centerObject;
        this.centerObject = centerObject;
        return old;
    }

    @Override
    public boolean addTrack(int newLevel, double radius) {
        if (newLevel <= 0 || radius <= 0) return false;

        if (tracks.get(newLevel) != null) return false;     // contains this level

        // no this level, create a new track
        Track newTrack = createTrack(newLevel, radius);

        tracks.put(newLevel, newTrack);    // == null -> true
        objectsOnTrack.put(newTrack, new ArrayList<>());

        checkRep();

        return true;
    }

    /**
     * todo 有很大问题
     * @param level    level of track to remove
     * @return
     */
    @Override
    public boolean removeTrack(int level) {
        Track toRemove = tracks.get(level);
        if (toRemove != null) {
            // remove everything associated with this track

            // 移除轨道上的物体，移除关系交给移除物体的方法
            // 移除轨道本身
            // 比轨道大的都要向内移动

            // 移除轨道物体，包括了移除关系
            List<E> objects = objectsOnTrack.remove(toRemove);
            for (E o : objects) {
                removeObject(o);
            }

            // 移除轨道本身
//            tracks.remove(level);

            // 向内移动：
            // 1. 这些轨道上的物体位置position map要变更
            // 2. tracks map的key要变更

            //  修改position map
            for (Map.Entry<E, Position> ePositionEntry : positionMap.entrySet()) {
                if (ePositionEntry.getValue().getLevel() > level) {
                    // 向内移
                    Position oldPosition = ePositionEntry.getValue();
                    changePosition(ePositionEntry.getKey(), new Position(oldPosition.getLevel() - 1, oldPosition.getRadius(), oldPosition.getAngle()));
                }
            }

            // 修改tracks map的值
            int maxLevel = tracks.size();
            for (int i = level; i < maxLevel; i++) {
                tracks.put(i, tracks.get(i + 1));
            }

            tracks.remove(maxLevel);

            checkRep();

            return true;
        }

        checkRep();

        return false;
    }

    @Override
    public boolean addRelation(E from, E to, double weight) {

        ConcreteRelation<E, E> newRelation = new ConcreteRelation<>(from, to, weight);

        boolean b = relations.add(newRelation);

        checkRep();

        return b;
    }

    @Override
    public boolean addRelation(E to, double weight) {

//        ConcreteRelation<L, E> newRelation = new ConcreteRelation<>(this.centerObject, to, weight);

//        boolean b = relations.add(newRelation);

        relationsWithCenter.add(new ConcreteRelation<>(centerObject, to, weight));

        checkRep();

        return true;

    }

    @Override
    public boolean removeRelation(E p1, E p2) {
        if (p1 == centerObject) {
            // this relation is with the center object
            for (Iterator<ConcreteRelation<L, E>> iterator = relationsWithCenter.iterator(); iterator.hasNext(); ) {
                ConcreteRelation<L, E> relation = iterator.next();
                if (relation.getTarget() == p2) {
                    iterator.remove();
                    return true;
                }
            }
            return false;
        } else {
            // this relation is e E to E
            for (Iterator<ConcreteRelation<E, E>> iterator = relations.iterator(); iterator.hasNext(); ) {
                ConcreteRelation<E, E> relation = iterator.next();
                if (relation.getSource() == p1 && relation.getTarget() == p2) {
                    iterator.remove();
                    return true;
                }
            }
            return false;
        }

    }

    @Override
    public boolean addToTrack(E object, int targetLevel, double radius, double angle) {
        Track toAdd = tracks.get(targetLevel);

        if (toAdd != null) {
            positionMap.put(object, new Position(targetLevel, radius, angle));
            objectsOnTrack.get(toAdd).add(object);
            allObjects.add(object);
            checkRep();
            return true;
        }
        checkRep();
        return false;
    }

    @Override
    public boolean addToTrack(E object, Position position) {
        return addToTrack(object, position.getLevel(), position.getRadius(), position.getAngle());
    }

    @Override
    public boolean removeObject(E object) {
        if (!containsObject(object)) return false;

        // 记录这个物体的层数，用于后面移除操作
        int level = positionMap.get(object).getLevel();

        // 从track中移除掉
        List<E> objects = objectsOnTrack.get(tracks.get(level));
//        objects.remove(object);

        positionMap.remove(object);  // 移除物体的坐标

        allObjects.remove(object);  // 移除它的存在

        // 移除关系
        // L->E
        relationsWithCenter.removeIf(relation -> relation.getTarget() == object);
        // E->E
        relations.removeIf(relation -> relation.getSource() == object || relation.getTarget() == object);

        return true;
    }

    @Deprecated
    @Override
    public E removeFromTrack(E object) {
        int level = getLevelByObject(object);
        if (level == -1) return null;       // -1 no such object
        boolean b = objectsOnTrack.get(tracks.get(level)).remove(object);
        checkRep();
        return b ? object : null;
    }

    /**
     * returns an unmodifiable list
     *
     * @param level level to find
     * @return objects on @level (unmodifiable list)
     */
    @Override
    public List<E> getObjectsByLevel(int level) {
        if (level <= 0) return new ArrayList<>();

        // level > 0
        Track track = tracks.get(level);
        if (track != null) {
            // != null
            List<E> objects = objectsOnTrack.get(track);
            return Collections.unmodifiableList(objects);
        }

        // == null
        return new ArrayList<>();
    }

    @Override
    public L getCenterObject() {
        return this.centerObject;
    }

    @Override
    public Position getPosition(E object) {
        return positionMap.get(object);
    }

    @Override
    public int trackCount() {
        return this.tracks.size();
    }

    @Override
    public int getDistance(String name1, String name2) {
        Set<String> visitedSet = new HashSet<>();
        LinkedList<Record> queue = new LinkedList<>();

        Record r1 = new Record(name1);

        queue.addLast(r1);

        // 广度优先搜索
        while (!queue.isEmpty()) {
            Record sourceRecord = queue.pollFirst();

            // 遍历当前出队顶点所有的邻接点
            assert sourceRecord != null;
            Set<String> visitingRow = this.targets(sourceRecord.getPerson()).keySet();
            for (String p : visitingRow) {
                // 只有在未访问时才进行访问操作
                if (!visitedSet.contains(p)) {
                    visitedSet.add(p);      // 标记已访问
                    // 因为是第一次访问，所以队列中一定没有这个person，新建一个record对象，并设置距离，然后进队
                    Record newVisited = new Record(p, sourceRecord.getDistance() + 1);
                    queue.addLast(newVisited);
                    if (name2.equals(p)) {
                        return newVisited.getDistance();
                    }
                }
            }
        }

        return -1;
    }

    @Override
    public int getLevelByObject(E object) {
        Set<Map.Entry<Track, List<E>>> entry = objectsOnTrack.entrySet();

        for (Map.Entry<Track, List<E>> e : entry) {
            List<E> objects = e.getValue();
            for (E o : objects) {
                if (o == object) {
                    // find!!!!!
                    return e.getKey().getLevel();
                }
            }
        }
        return -1;
    }

    public boolean containsLevel(int level) {
        return tracks.keySet().contains(level);
    }

    public Map<String, Double> sources(String name) {
        Map<String, Double> relationMap = new HashMap<>();

        for (ConcreteRelation<E, E> relation : relations) {
            if (relation.getTarget().getName().equals(name)) {
                relationMap.put(relation.getSource().getName(), relation.getWeight());
            }
        }

        return relationMap;
    }

    public Map<String, Double> targets(String name) {
//        String objectName = objectOnTrack.getName();
        Map<String, Double> relationMap = new HashMap<>();

        if (centerObject.getName().equals(name)) {
            for (Relation<L, E> relation : relationsWithCenter) {
                relationMap.put(relation.getTarget().getName(), relation.getWeight());
            }
            return relationMap;
        }

        for (ConcreteRelation<E, E> relation : relations) {
            if (relation.getSource().getName().equals(name)) {
                relationMap.put(relation.getTarget().getName(), relation.getWeight());
            }
        }

        return relationMap;
    }

    public boolean containsObject(E object) {
        return allObjects.contains(object);
    }

    public Position changePosition(E object, Position newPosition) {
        if (object == null) return null;

        int oldLevel = getLevelByObject(object);
        int newLevel = newPosition.getLevel();
        Position old = positionMap.get(object);

        if (old == null) return null;       // object passed in does not exist

        if (oldLevel != newLevel) {
            objectsOnTrack.get(tracks.get(oldLevel)).remove(object);        // remove from old track
            if (!containsLevel(newLevel)) addTrack(newLevel, newLevel);     // if level of track not exist, add it
            objectsOnTrack.get(tracks.get(newLevel)).add(object);           // add to new track
            positionMap.put(object, newPosition);       // change position
        }

        return old;

    }

    @Override
    public Iterator<E> iterator() {
        return new TrackObjectIterator();
    }

    private class TrackObjectIterator implements Iterator<E> {

        int nextIndex = 0;

        int level = 1;

        List<E> currentTrackObjects = objectsOnTrack.get(tracks.get(1));

        @Override
        public boolean hasNext() {
            int tracksCount = trackCount();
            if (level >= tracksCount || nextIndex >= currentTrackObjects.size()) return false;
            return true;
        }

        @Override
        public E next() {
            if (nextIndex >= currentTrackObjects.size()) {
                this.level += 1;
                List<E> newTrackObjects = objectsOnTrack.get(tracks.get(level));
                if (newTrackObjects != null) {
                    this.currentTrackObjects = newTrackObjects;
                    this.nextIndex = 0;
                } else {
                    return null;
                }
            }
            return currentTrackObjects.get(nextIndex);
        }
    }

}

class Track {

    private final int LEVEL;

    private final double radius;

    /*
        AF
        this class represents a track around any orbit model

        RI
        level > 0 && radius > 0

        safety from exposure
        all fields are final stuff
        NO mutator
     */

    private void checkRep() {
        if (LEVEL <= 0 || radius <= 0)
            throw new RuntimeException("check rep failed in Track. level or radius is not legal");
    }

    /**
     * level must > 0 and radius must > 0
     *
     * @param LEVEL  an int represents level must > 0
     * @param radius a double value must > 0
     */
    Track(int LEVEL, double radius) {
        this.LEVEL = LEVEL;
        this.radius = radius;
        checkRep();
    }

    public int getLevel() {
        return LEVEL;
    }

    public double getRadius() {
        return radius;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Track track = (Track) o;

        if (LEVEL != track.LEVEL) return false;
        return Double.compare(track.radius, radius) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = LEVEL;
        temp = Double.doubleToLongBits(radius);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}

/**
 * this class serves for getLevel or similar methods,
 * cannot be used out of this package
 */
class Record {
    private String person;
    private int distance = 0;

    Record(String person) {
        this.person = person;
    }

    Record(String person, int distance) {
        this.person = person;
        this.distance = distance;
    }

    String getPerson() {
        return person;
    }

    int getDistance() {
        return distance;
    }
}
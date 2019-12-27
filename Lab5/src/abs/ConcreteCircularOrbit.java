package abs;

import MyException.CircularOrbitExceotion.ObjectNotExistException;
import MyException.CircularOrbitExceotion.TrackDidExistException;
import MyException.CircularOrbitExceotion.NoSuchLevelOfTrackException;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ConcreteCircularOrbit<L extends PhysicalObject,
        E extends PhysicalObject> implements CircularOrbit<L, E>, Iterable<E> {

    /**
     * a map relate level an int with track object.
     */
    protected Map<Integer, Track> tracks = new HashMap<>();

    /**
     * a map relate track and a list of object on it.
     */
    protected Map<Integer, List<E>> objectsOnTrack = new HashMap<>();

    /**
     * logger for this class.
     */
    private Logger log = Logger.getLogger(ConcreteCircularOrbit.class);
    /**
     * center object of a concrete orbit.
     */
    private L centerObject = null;
    /**
     * all objects on track in this orbit model.
     */
    private Set<E> allObjects = new HashSet<>();
    /**
     * a map relate object on track with its position in this orbit model.
     */
    private Map<E, Position> positionMap = new HashMap<>();
    /**
     * a map stands for relation.
     */
    private Map<String, Map<String, Double>> relationMap = new HashMap<>();

    /**
     * factory to create a track.
     *
     * @param level  level of track
     * @param radius radius of track
     * @return a new track
     */
    public static Track createTrack(final int level, final double radius) {
        return new Track(level, radius);
    }

    /**
     * check RI.
     */
    private void checkRep() {
        assert true;
    }

    @Override
    public final L addCenterObject(final L center) {
        L old = this.centerObject;
        this.centerObject = center;
        return old;
    }

    @Override
    public final boolean addTrack(final int newLevel, final double radius)
            throws TrackDidExistException {
        if (newLevel <= 0 || radius <= 0) {
            throw new IllegalArgumentException("轨道号和半径必须大于0");
        }

        if (tracks.get(newLevel) != null) {
            throw new TrackDidExistException("轨道"
                    + newLevel + "已存在");     // contains this level
        }

        // no this level, create a new track
        Track newTrack = createTrack(newLevel, radius);

        tracks.put(newLevel, newTrack);    // == null -> true
//        objectsOnTrack.put(newTrack, new ArrayList<>());
        objectsOnTrack.put(newLevel, new ArrayList<>());
        checkRep();

        return true;
    }

    /**
     * remove a track from this orbit model.
     * what will be removed:
     * 1. all objects on this track.
     * 2. all relations connected with the objects on this track.
     * 3. track itself.
     *
     * @param level level of track to remove
     * @return true iff success
     */
    @Override
    public final boolean removeTrack(final int level)
            throws NoSuchLevelOfTrackException {
        if (!containsLevel(level)) {
            throw new NoSuchLevelOfTrackException(level + "号轨道不存在。");
        }

        // remove everything associated with this track

        // 移除轨道上的物体，移除关系交给移除物体的方法
        // 移除轨道本身
        // 比轨道大的都要向内移动

        // 移除轨道物体，包括了移除关系
        List<E> objects = objectsOnTrack.remove(level);
        for (E o : objects) {
            try {
                removeObject(o);
            } catch (ObjectNotExistException e) {
                e.printStackTrace();
                log.error("尝试移除一个不存在的物体");
            }
        }

        // 移除轨道本身
        tracks.remove(level);

        checkRep();

        return true;
    }

    @Override
    public final boolean addRelation(final E from,
                                     final E to,
                                     final double weight)
            throws ObjectNotExistException {
        if (!containsObject(from) || !containsObject(to)) {
            throw new ObjectNotExistException("关系双方都必须存在");
        }
        Map<String, Double> relationWithE = relationMap.get(from.getName());
        if (relationWithE != null) {
            relationWithE.put(to.getName(), weight);
        } else {
            Map<String, Double> newMap = new HashMap<>();
            newMap.put(to.getName(), weight);
            relationMap.put(from.getName(), newMap);
        }

        checkRep();
        return true;
    }

    @Override
    public final boolean addRelationWithCenter(final E to,
                                               final double weight) {

        Map<String, Double> relationWithL =
                relationMap.get(centerObject.getName());
        if (relationWithL == null) {
            relationMap.put(centerObject.getName(), new HashMap<>());
            this.addRelationWithCenter(to, weight);
        } else {
            relationWithL.put(to.getName(), weight);
        }

        checkRep();

        return true;

    }

    @Override
    public final boolean removeRelation(final E p1, final E p2)
            throws ObjectNotExistException {
        if (p1 == centerObject) {
            this.relationMap.get(p1.getName()).remove(p2.getName());
            return true;
        }
        if (!containsObject(p1)) {
            throw new ObjectNotExistException("关系来源(p1)不存在");
        } else if (!containsObject(p2)) {
            throw new ObjectNotExistException("关系目标(p2)不存在");
        } else {
            relationMap.remove(p1.getName());
            return true;
        }
    }

    @Override
    public final boolean addToTrack(final E object,
                                    final int trackLevel,
                                    final double radius,
                                    final double angle)
            throws NoSuchLevelOfTrackException {
        if (!containsLevel(trackLevel)) {
            throw new NoSuchLevelOfTrackException(trackLevel + "号轨道不存在。");
        }
        positionMap.put(object,
                new Position(trackLevel,
                        radius,
                        angle));    // 记录位置
//            objectsOnTrack.get(toAdd).add(object);      // 添加到轨道
        objectsOnTrack.get(trackLevel).add(object);
        allObjects.add(object);                     // 添加到所有物体的列表
        checkRep();
        return true;
    }

    @Override
    public final boolean removeObject(final E object)
            throws ObjectNotExistException {
        if (!containsObject(object)) {
            throw new ObjectNotExistException();
        }

        // 记录这个物体的层数，用于后面移除操作
        int level = positionMap.get(object).getLevel();

        // 从track中移除掉
        List<E> objects = objectsOnTrack.get(level);
        assert objects != null;
        objects.remove(object);

        positionMap.remove(object);  // 移除物体的坐标

        allObjects.remove(object);  // 移除它的存在

        // 移除关系
        String nameOfThisObject = object.getName();
        // 1. 移除它指出去的关系
        relationMap.remove(nameOfThisObject);

        // 2. 移除指向它的关系
        Iterator<Map.Entry<String, Map<String, Double>>> iterator =
                relationMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Map<String, Double>> entry = iterator.next();
            for (String name : entry.getValue().keySet()) {
                if (nameOfThisObject.equals(name)) {
                    iterator.remove();
                    break;
                }
            }
        }
        return true;
    }

    /**
     * returns an unmodifiable list.
     *
     * @param level level to find
     * @return objects on @level (unmodifiable list)
     */
    @Override
    public final List<E> getObjectsByLevel(final int level) {
        if (!containsLevel(level)) {
            return new ArrayList<>();
        }
        // level exists
        List<E> objects = objectsOnTrack.get(level);
        return Collections.unmodifiableList(objects);
    }

    @Override
    public final L getCenterObject() {
        return this.centerObject;
    }

    @Override
    public final Position getPosition(final E object) {
        return positionMap.get(object);
    }

    @Override
    public final int trackCount() {
        return this.tracks.size();
    }

    @Override
    public final int getDistanceBetween(final String name1,
                                        final String name2) {
        Set<String> visitedSet = new HashSet<>();

        // old-stable
        LinkedList<Record> queue = new LinkedList<>();

        Record r1 = new Record(name1);

        queue.addLast(r1);

        // 广度优先搜索
        while (!queue.isEmpty()) {
            Record sourceRecord = queue.pollFirst();

            // 遍历当前出队顶点所有的邻接点
//            assert sourceRecord != null;
            Set<String> visitingRow =
                    this.targets(sourceRecord.person).keySet();
            for (String p : visitingRow) {
                // 只有在未访问时才进行访问操作
                if (!visitedSet.contains(p)) {
                    visitedSet.add(p);      // 标记已访问
                    // 因为是第一次访问，所以队列中一定没有这个person，
                    // 新建一个record对象，并设置距离，然后进队
                    Record newVisited =
                            new Record(p, sourceRecord.distance + 1);
                    queue.addLast(newVisited);
                    if (name2.equals(p)) {
                        return newVisited.distance;
                    }
                }
            }
        }

        return -1;
    }

    @Override
    public final Track getTrack(final int level) {
        return this.tracks.get(level);
    }

    /**
     * get level of an object.
     *
     * @param object the object you want to get level
     * @return level if the object is on the track, or returns -1
     */
    @Override
    public final int getLevelByObject(final E object) {
        if (!containsObject(object)) {
            return -1;
        }
        return positionMap.get(object).getLevel();
    }

    /**
     * judge if the level is in this orbit.
     *
     * @param level level
     * @return true iff the orbit contains the level of track
     */
    public final boolean containsLevel(final int level) {
        return tracks.keySet().contains(level);
    }

    /**
     * get a map of relation sources by a name,
     * which means all names in the returned map
     * has a relation pointing to the passed in argument.
     *
     * @param name name you want to get sources
     * @return map contains string and weight(a double) stands for sources
     */
    public final Map<String, Double> sources(final String name) {
        Map<String, Double> returnRelationMap = new HashMap<>();

        for (Map.Entry<String, Map<String, Double>> map
                : this.relationMap.entrySet()) {
            Map<String, Double> targets = map.getValue();
            if (targets.keySet().contains(name)) {
                returnRelationMap.put(map.getKey(), targets.get(name));
            }
        }

        return returnRelationMap;
    }

    /**
     * get targets.
     *
     * @param name name you want to get relation targets
     * @return a map contains string and weight stands for targets
     */
    public final Map<String, Double> targets(final String name) {
        Map<String, Double> map = relationMap.get(name);

        if (map == null) {
            return new HashMap<>();
        } else {
            return map;
        }

    }

    /**
     * judge if the orbit contains an object.
     *
     * @param object object to want to judge
     * @return true iff the orbit contains the object
     */
    public final boolean containsObject(final E object) {
        return allObjects.contains(object);
    }

    /**
     * change an object's position in this orbit model.
     *
     * @param object      the object you want to change position
     * @param newPosition new position
     * @return old position, null if the object is null
     * @throws ObjectNotExistException if the object
     *                                 passed in does not in this orbit
     */
    public final Position changePosition(final E object,
                                         final Position newPosition)
            throws ObjectNotExistException {
        if (object == null) {
            return null;
        }
        if (!containsObject(object)) {
            throw new ObjectNotExistException();
        }

        // now the object must be in the orbit/track
        int oldLevel = getLevelByObject(object);
        int newLevel = newPosition.getLevel();
        Position old = positionMap.get(object);

        if (oldLevel != newLevel) {
            // remove from old track
            objectsOnTrack.get(oldLevel).remove(object);
            if (!containsLevel(newLevel)) {
                try {
                    // if level of track not exist, add it
                    addTrack(newLevel, newLevel);
                } catch (TrackDidExistException e) {
                    log.info("自动创建" + newLevel + "层轨道");
                }
            }
            // add to new track
            objectsOnTrack.get(newLevel).add(object);
            positionMap.put(object, newPosition);       // change position
        }

        return old;
    }

    @Override
    public final Iterator<E> iterator() {
        return new TrackObjectIterator();
    }

    @Override
    public void writeBackWithPrintWriter() throws IOException {
        throw new UnsupportedOperationException("ConcreteCircularOrbit 不支持此操作");
    }

    @Override
    public void writeBackWithStream() throws IOException {
        throw new UnsupportedOperationException("ConcreteCircularOrbit 不支持此操作");
    }

    @Override
    public void writeBackWithBuffer() throws IOException {
        throw new UnsupportedOperationException("ConcreteCircularOrbit 不支持此操作");
    }

    private class TrackObjectIterator implements Iterator<E> {
        /**
         * next iterate index.
         */
        private int nextIndex = 0;

        /**
         * current level.
         */
        private int level = 1;

        /**
         * current objects on the level.
         */
        private List<E> currentTrackObjects = objectsOnTrack.get(1);

        @Override
        public boolean hasNext() {
            int tracksCount = trackCount();
            return level < tracksCount
                    && nextIndex < currentTrackObjects.size();
        }

        @Override
        public E next() {
            if (nextIndex >= currentTrackObjects.size()) {
                this.level += 1;
                List<E> newTrackObjects =
                        objectsOnTrack.get(level);
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

/**
 * track class for orbit model.
 */
final class Track {
    /**
     * track's level an int.
     */
    private final int level;

    /**
     * track's radius a double.
     */
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

    /**
     * level must > 0 and radius must > 0.
     *
     * @param level  an int represents level must > 0
     * @param radius a double value must > 0
     */
    Track(final int level, final double radius) {
        this.level = level;
        this.radius = radius;
        checkRep();
    }

    /**
     * check RI.
     */
    private void checkRep() {
        if (level <= 0 || radius <= 0) {
            throw new RuntimeException("check rep failed in Track. level or radius is not legal");
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Track track = (Track) o;

        if (level != track.level) {
            return false;
        }
        return Double.compare(track.radius, radius) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = level;
        temp = Double.doubleToLongBits(radius);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}

/**
 * this class serves for getLevel or similar methods,
 * cannot be used out of this package.
 */
class Record {
    /**
     * person in record.
     */
    String person;

    /**
     * distance.
     */
    int distance = 0;

    /**
     * record constructor.
     *
     * @param person the person you want to set in record
     */
    Record(final String person) {
        this.person = person;
    }

    /**
     * record constructor2.
     *
     * @param person   the person you want to set in record
     * @param distance the init distance
     */
    Record(final String person, final int distance) {
        this.person = person;
        this.distance = distance;
    }
}

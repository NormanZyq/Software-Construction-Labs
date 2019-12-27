package abs;

import MyException.CircularOrbitExceotion.ObjectDoesNotExistException;
import MyException.CircularOrbitExceotion.TrackDidExistException;
import MyException.CircularOrbitExceotion.NoSuchLevelOfTrackException;
import org.apache.log4j.Logger;

import java.util.*;

public class ConcreteCircularOrbit<L extends PhysicalObject, E extends PhysicalObject> implements CircularOrbit<L, E>, Iterable<E> {
	
	private Logger log = Logger.getLogger(ConcreteCircularOrbit.class);
	
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

//    private Set<ConcreteRelation<L, E>> relationsWithCenter = new HashSet<>();

//    private Set<ConcreteRelation<E, E>> relations = new HashSet<>();
	
	private Set<E> allObjects = new HashSet<>();
	
	protected Map<Track, List<E>> objectsOnTrack = new HashMap<>();
	
	protected Map<Integer, Track> tracks = new HashMap<>();
	
	private Map<E, Position> positionMap = new HashMap<>();
	
	private Map<String, Map<String, Double>> relationMap = new HashMap<>();
	
	void checkRep() {
	
	}
	
	@Override
	public L addCenterObject(L centerObject) {
		L old = this.centerObject;
		this.centerObject = centerObject;
//		relationMap.put(centerObject.getName(), new HashMap<>());
		return old;
	}
	
	@Override
	public boolean addTrack(int newLevel, double radius) throws TrackDidExistException {
		if (newLevel <= 0 || radius <= 0) throw new IllegalArgumentException("轨道号和半径必须大于0");
		
		if (tracks.get(newLevel) != null) throw new TrackDidExistException("轨道" + newLevel + "已存在");     // contains this level
		
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
	public boolean removeTrack(int level) throws NoSuchLevelOfTrackException {
		Track toRemove = tracks.get(level);
		if (toRemove != null) {
			// remove everything associated with this track
			
			// 移除轨道上的物体，移除关系交给移除物体的方法
			// 移除轨道本身
			// 比轨道大的都要向内移动
			
			// 移除轨道物体，包括了移除关系
			List<E> objects = objectsOnTrack.remove(toRemove);
			for (E o : objects) {
				try {
					removeObject(o);
				} catch (ObjectDoesNotExistException e) {
					e.printStackTrace();
					log.error("尝试移除一个不存在的物体");
				}
			}
			
			// 移除轨道本身
			tracks.remove(level);
			
			checkRep();
			
			return true;
		}
		
		checkRep();
		
		throw new NoSuchLevelOfTrackException(level + "号轨道不存在。");
	}
	
	@Override
	public boolean addRelation(E from, E to, double weight) throws ObjectDoesNotExistException {
		if (!containsObject(from) || !containsObject(to)) throw new ObjectDoesNotExistException("关系双方都必须存在");
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
	public boolean addRelationWithCenter(E to, double weight) {
		
		Map<String, Double> relationWithL = relationMap.get(centerObject.getName());
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
	public boolean removeRelation(E p1, E p2) throws ObjectDoesNotExistException {
		if (p1 == centerObject) {
			this.relationMap.get(p1.getName()).remove(p2.getName());
			return true;
		}
		if (!containsObject(p1)) {
			throw new ObjectDoesNotExistException("关系来源(p1)不存在");
		} else if (!containsObject(p2)) {
			throw new ObjectDoesNotExistException("关系目标(p2)不存在");
		} else {
			relationMap.remove(p1.getName());
			return true;
		}
	}
	
	@Override
	public boolean addToTrack(E object, int trackLevel, double radius, double angle) throws NoSuchLevelOfTrackException {
		Track toAdd = tracks.get(trackLevel);
		if (toAdd != null) {
			positionMap.put(object, new Position(trackLevel, radius, angle));	// 记录位置
			objectsOnTrack.get(toAdd).add(object);								// 添加到轨道
			allObjects.add(object);												// 添加到所有物体的列表
			checkRep();
			return true;
		}
		checkRep();
		throw new NoSuchLevelOfTrackException(trackLevel + "号轨道不存在。");
	}
	
	@Override
	public boolean addToTrack(E object, Position position) throws NoSuchLevelOfTrackException {
		return addToTrack(object, position.getLevel(), position.getRadius(), position.getAngle());
	}
	
	@Override
	public boolean removeObject(E object) throws ObjectDoesNotExistException {
		if (!containsObject(object)) throw new ObjectDoesNotExistException();
		
		// 记录这个物体的层数，用于后面移除操作
		int level = positionMap.get(object).getLevel();
		
		assert level > 0;	// 从map中来，level不可能是-1或0
		
		// 从track中移除掉
		List<E> objects = objectsOnTrack.get(tracks.get(level));
		assert objects != null;
        objects.remove(object);
		
		positionMap.remove(object);  // 移除物体的坐标
		
		allObjects.remove(object);  // 移除它的存在
		
		// 移除关系
		String nameOfThisObject = object.getName();
		// 1. 移除它指出去的关系
		relationMap.remove(nameOfThisObject);
		
		// 2. 移除指向它的关系
		Iterator<Map.Entry<String, Map<String, Double>>> iterator = relationMap.entrySet().iterator();
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
	public int getDistanceBetween(String name1, String name2) {
		Set<String> visitedSet = new HashSet<>();
		LinkedList<Record> queue = new LinkedList<>();
		
		Record r1 = new Record(name1);
		
		queue.addLast(r1);
		
		// 广度优先搜索
		while (!queue.isEmpty()) {
			Record sourceRecord = queue.pollFirst();
			
			// 遍历当前出队顶点所有的邻接点
			assert sourceRecord != null;
			Set<String> visitingRow = this.targets(sourceRecord.person).keySet();
			for (String p : visitingRow) {
				// 只有在未访问时才进行访问操作
				if (!visitedSet.contains(p)) {
					visitedSet.add(p);      // 标记已访问
					// 因为是第一次访问，所以队列中一定没有这个person，新建一个record对象，并设置距离，然后进队
					Record newVisited = new Record(p, sourceRecord.distance + 1);
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
	public Track getTrack(int level) {
		return this.tracks.get(level);
	}
	
	/**
	 * get level of an object
	 * @param object   the object you want to get level
	 * @return	level if the object is on the track, or returns -1
	 */
	@Override
	public int getLevelByObject(E object) {
		if (!containsObject(object)) return -1;
		return positionMap.get(object).getLevel();
	}
	
	/**
	 * judge if the level is in this orbit
	 * @param level	level
	 * @return		true iff the orbit contains the level of track
	 */
	public boolean containsLevel(int level) {
		return tracks.keySet().contains(level);
	}
	
	/**
	 * get a map of relation sources by a name,
	 * which means all names in the returned map
	 * has a relation pointing to the passed in argument
	 *
	 * @param name 		name you want to get sources
	 * @return			map contains string and weight(a double) stands for sources
	 */
	public Map<String, Double> sources(String name) {
		Map<String, Double> relationMap = new HashMap<>();
		
		for (Map.Entry<String, Map<String, Double>> map : this.relationMap.entrySet()) {
			Map<String, Double> targets = map.getValue();
			if (targets.keySet().contains(name)) {
				relationMap.put(map.getKey(), targets.get(name));
			}
		}
		
		return relationMap;
	}
	
	/**
	 * get targets
	 * @param name	name you want to get relation targets
	 * @return		a map contains string and weight stands for targets
	 */
	public Map<String, Double> targets(String name) {
		Map<String, Double> map = relationMap.get(name);
		
		if (map == null) {
			return new HashMap<>();
		} else {
			return map;
		}
		
	}
	
	/**
	 * judge if the orbit contains an object
	 * @param object	object to want to judge
	 * @return			true iff the orbit contains the object
	 */
	public boolean containsObject(E object) {
		return allObjects.contains(object);
	}
	
	/**
	 * change an object's position in this orbit model
	 * @param object			the object you want to change position
	 * @param newPosition		new position
	 * @throws ObjectDoesNotExistException	if the object passed in does not in this orbit
	 * @return					old position, null if the object is null
	 */
	public Position changePosition(E object, Position newPosition) throws ObjectDoesNotExistException {
		if (object == null) return null;
		if (!containsObject(object)) throw new ObjectDoesNotExistException();
		
		// now the object must be in the orbit/track
		int oldLevel = getLevelByObject(object);
		int newLevel = newPosition.getLevel();
		Position old = positionMap.get(object);
		
		if (oldLevel != newLevel) {
			objectsOnTrack.get(tracks.get(oldLevel)).remove(object);        // remove from old track
			if (!containsLevel(newLevel)) {
				try {
					addTrack(newLevel, newLevel);     // if level of track not exist, add it
				} catch (TrackDidExistException e) {
					log.info("自动创建" + newLevel + "层轨道");
				}
			}
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
	String person;
	int distance = 0;
	
	Record(String person) {
		this.person = person;
	}
	
	Record(String person, int distance) {
		this.person = person;
		this.distance = distance;
	}
	
	int getDistance() {
		return distance;
	}
}
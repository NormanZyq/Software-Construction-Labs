package Application.SocialNetworkCircle;

import abs.*;
import javafx.geometry.Pos;

import java.util.*;

/**
 * This class is a social network circle model,
 * uses concrete circular orbit as its basis to implement some functions
 */
public class SocialNetworkCircle implements OrbitAble {

    /*
        AF
        this class represents a social net work circle model

        RI


        safety from exposure
        no method returns original mutable values
        The basic implementation is implemented by the concrete circular orbit,
        And that class is not a dangerous class so there is no no dangerous exposure from that class
        There is only one extended variable in this class.
        It can't be returned directly, instead it is returned by some methods safely.
     */


    public static SocialNetworkCircle empty() {
        return new SocialNetworkCircle();
    }

    private ConcreteCircularOrbit<SocialPerson, SocialPerson> orbit = new ConcreteCircularOrbit<>();

    private Map<String, SocialPerson> nameMap = new HashMap<>();

    private Random random = new Random(System.currentTimeMillis());

    private List<String> failed = new ArrayList<>();

    private void checkRep() {

    }

    /**
     * empty constructor
     */
    public SocialNetworkCircle() {
    }

    /**
     * add center user method, when the center user in this
     * orbit model is null (or empty), the user will be added directly,
     * then the method returns true
     * <p>
     * if the center person isn't null, returns false and nothing happens
     *
     * @param person the person you want to make as center
     * @return true iff success
     */
    public boolean addCenterUser(SocialPerson person) {
        SocialPerson old = orbit.addCenterObject(person);

        if (old == null) {
            checkRep();

            nameMap.put(person.getName(), person);

            return true;
        } else {
            orbit.addCenterObject(old);

            checkRep();

            return false;
        }

    }

    /**
     * constructor with center person
     *
     * @param centerUser the person you want to make as center person
     */
    public SocialNetworkCircle(SocialPerson centerUser) {
        orbit.addCenterObject(centerUser);
        nameMap.put(centerUser.getName(), centerUser);
    }

    /**
     * @param p1     person 1 as source of a relation
     * @param p2     person 2 as target of a relation
     * @param weight weight
     * @return true
     */
    private boolean addRelation(SocialPerson p1, SocialPerson p2, double weight) {
        if (p1 == orbit.getCenterObject()) {
            if (!orbit.containsLevel(1)) orbit.addTrack(1, 1);
            orbit.addRelation(p2, weight);
            if (orbit.containsObject(p2)) {
                int level1 = orbit.getLevelByObject(p2);
                orbit.addRelation(p1, p2, weight);
                int level2 = calculateLevel(p2.getName());
                if (level2 < level1) {
                    // 从旧的位置里移除，加到新的里
//                    orbit.removeFromTrack(p2);
                    changePosition(p2, new Position(level2, level2, Math.abs((int) (random.nextDouble() * 1767) % 360)));
                }
            } else {
                orbit.addToTrack(p2, 1, 1, Math.abs((int) (random.nextDouble() * 1767) % 360));
                nameMap.put(p2.getName(), p2);
            }
        } else if (orbit.containsObject(p1)) {
            if (orbit.containsObject(p2)) {
                // p1 and p2 both are in the orbit
                int level1 = orbit.getLevelByObject(p2);
                orbit.addRelation(p1, p2, weight);
                int level2 = calculateLevel(p2.getName());
                if (level2 < level1) {
                    // 从旧的位置里移除，加到新的里
//                    orbit.removeFromTrack(p2);
                    changePosition(p2, new Position(level2, level2, Math.abs((int) (random.nextDouble() * 1767) % 360)));
                }
//                if (!orbit.containsLevel(level)) orbit.addTrack(level, level);
            } else {
                // p2 is firstly be added
                orbit.addRelation(p1, p2, weight);
                int level = calculateLevel(p2.getName());
                if (!orbit.containsLevel(level)) orbit.addTrack(level, level);
                orbit.addToTrack(p2, level, level, random.nextDouble() % 360);
                nameMap.put(p2.getName(), p2);
            }

        }
        return true;
    }

    /**
     * add relation by names
     *
     * @param name1  person 1's name
     * @param name2  person 2's name
     * @param weight weight of the relation
     * @return true
     */
    public boolean addRelation(String name1, String name2, double weight) {
        SocialPerson p1 = nameMap.get(name1);
        SocialPerson p2 = nameMap.get(name2);
        if (p1 == null && p2 == null) {
            failed.add(name1);
            failed.add(name2);
            return false;
        }

        if (p1 == null) p1 = PhysicalObject.newPerson(name1, 20, 'M');
        if (p2 == null) p2 = PhysicalObject.newPerson(name2, 20, 'M');
        return addRelation(p1, p2, weight);
    }

    /**
     * remove a people relation
     *
     * @param p1
     * @param p2
     * @return
     */
    public boolean removeRelation(SocialPerson p1, SocialPerson p2) {
        boolean b = orbit.removeRelation(p1, p2);
        if (b) {
            int newLevel = calculateLevel(p2.getName());
            if (newLevel == -1) {
                removePerson(p2.getName());
                return true;
            } else {
                orbit.changePosition(p2, new Position(newLevel, newLevel, Math.abs((int) (random.nextDouble() * 1767) % 360)));
            }
        }
        checkRep();
        return b;
    }

    public boolean removeRelation(String name1, String name2) {
        SocialPerson p1 = nameMap.get(name1);
        if (p1 == null) return false;
        SocialPerson p2 = nameMap.get(name2);
        if (p2 == null) return false;
        return removeRelation(p1, p2);
    }

    /**
     * @return center user
     */
    public SocialPerson getCentralUser() {
        return orbit.getCenterObject();
    }

    /**
     * calculate which level should the person with name be in
     *
     * @param name the person's name you want to calculate the level
     * @return level if succeeded find the level, -1 if name does not exist
     */
    public int calculateLevel(String name) {
        return orbit.getDistance(orbit.getCenterObject().getName(), name);
    }

    /**
     * get a relation map
     *
     * @param person the person you want to get his/her relation
     * @return a relation map, containing string as name, double as weight
     */
    public Map<String, Double> getRelationByPerson(SocialPerson person) {
        return orbit.targets(person.getName());
    }

    /**
     * use name to get his/her relation map like the getRelationByPerson(SocialPerson person) method
     *
     * @param name name of a person you want to get the relation
     * @return a relation map, containing string as name, double as weight
     */
    public Map<String, Double> getRelationByPerson(String name) {
        SocialPerson person = nameMap.get(name);
        if (person == null) return new HashMap<>();
        return getRelationByPerson(person);
    }

    public List<SocialPerson> getObjectsByLevel(int level) {
        return orbit.getObjectsByLevel(level);
    }

    public Position getPosition(SocialPerson person) {
        return orbit.getPosition(person);
    }

    public Position getPosition(String name) {
        return orbit.getPosition(nameMap.get(name));
    }

    public Set<String> getPeopleNameSet() {
        return this.nameMap.keySet();
    }


    /**
     * get track count
     *
     * @return track count
     */
    public int trackCount() {
        return orbit.trackCount();
    }

    /**
     * @param person
     * @param newPosition
     * @return
     */
    private Position changePosition(SocialPerson person, Position newPosition) {
        return orbit.changePosition(person, newPosition);
    }

    public Position changePosition(String name, Position newPosition) {
        return orbit.changePosition(nameMap.get(name), newPosition);
    }

    /**
     * calculate diffusion
     *
     * @param person person you want to calculate
     * @return the diffusion of person
     */
    private double calculateDiffusion(SocialPerson person) {
        double diffusion = 0;
        Map<String, Double> map = getRelationByPerson(person);
        for (double weight : map.values()) {
            diffusion += weight;
        }
        return diffusion;
    }

    /**
     * calculate diffusion with name
     *
     * @param name name of person
     * @return diffusion
     */
    public double calculateDiffusion(String name) {
        SocialPerson person = nameMap.get(name);
        if (name == null) return -1;
        return calculateDiffusion(person);
    }

    /**
     * this private method is protected by the overloaded method
     *
     * @param p1 person1
     * @param p2 person2
     * @return physical distance
     */
    private double getPhysicalDistance(SocialPerson p1, SocialPerson p2) {
        Position position1 = orbit.getPosition(p1);
        Position position2 = orbit.getPosition(p2);

        return Math.cos(position1.getLevel() * position1.getAngle()) * (position2.getLevel() + position2.getAngle());
    }

    public double getPhysicalDistance(String name1, String name2) {
        SocialPerson p1 = nameMap.get(name1);
        if (p1 == null) return -1;
        SocialPerson p2 = nameMap.get(name2);
        if (p2 == null) return -1;
        return getPhysicalDistance(p1, p2);
    }

    /**
     * calculate logical distance
     *
     * @param name1 name1 of person
     * @param name2 name2 of person
     * @return logical distance
     */
    public int getLogicalDistance(String name1, String name2) {
        return orbit.getDistance(name1, name2);
    }

    /**
     * remove person from track, won't remove relations
     *
     * @param name name of a person
     * @return true iff success
     */
    public boolean removePerson(String name) {
        SocialPerson person = nameMap.get(name);
        if (person == null) return false;
        nameMap.remove(name);
        orbit.removeFromTrack(person);
        return true;
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
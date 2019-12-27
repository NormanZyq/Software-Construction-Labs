package application.SocialNetworkCircle;

import MyException.CircularOrbitExceotion.ObjectNotExistException;
import MyException.CircularOrbitExceotion.TrackDidExistException;
import MyException.CircularOrbitExceotion.NoSuchLevelOfTrackException;
import MyException.SocialNetworkCircleException.PersonNotExistException;
import abs.*;
import org.apache.log4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;


/**
 * This class is a social network circle model,
 * uses concrete circular orbit as its basis to implement some functions.
 */
public final class SocialNetworkCircle implements OrbitAble {

    /**
     * logger.
     */
    private Logger log = Logger.getLogger(SocialNetworkCircle.class);

    /*
        AF
        this class represents a social net work circle model

        RI
        nameMap.keySet should contain the exactly people in orbit

        safety from exposure
        no method returns original mutable values
        The basic implementation is implemented by the concrete circular orbit,
        And that class is not a dangerous class so there is
         no no dangerous exposure from that class
        There is only one extended variable in this class.
        It can't be returned directly, instead it is returned by some methods safely.
     */

    /**
     * delegate to ConcreteCircularOrbit to implement social network circle.
     */
    private ConcreteCircularOrbit<SocialPerson, SocialPerson> orbit = new ConcreteCircularOrbit<>();

    /**
     * a map relates name and social person object.
     */
    private Map<String, SocialPerson> nameMap = new HashMap<>();

    /**
     * random.
     */
    private Random random = new Random(System.currentTimeMillis());

    /**
     * empty constructor.
     */
    public SocialNetworkCircle() {
    }

    /**
     * constructor with center person.
     *
     * @param centerUser the person you want to make as center person
     */
    public SocialNetworkCircle(final SocialPerson centerUser) {
        orbit.addCenterObject(centerUser);
        nameMap.put(centerUser.getName(), centerUser);
    }

    /**
     * create an empty social network circle.
     *
     * @return an empty SocialNetworkCircle object
     */
    public static SocialNetworkCircle empty() {
        return new SocialNetworkCircle();
    }

    /**
     * check RI.
     */
    private void checkRep() {
        for (String name : nameMap.keySet()) {
            assert getPosition(name).getLevel() == calculateLevel(name);
        }
    }

    /**
     * add center user method, when the center user in this
     * orbit model is null (or empty), the user will be added directly,
     * then the method returns true.
     * <p>
     * if the center person isn't null, returns false and nothing happens
     *
     * @param person the person you want to make as center
     * @return true iff success
     */
    public boolean addCenterUser(final SocialPerson person) {
        SocialPerson old = orbit.addCenterObject(person);

        if (old == null) {
            nameMap.put(person.getName(), person);
            return true;
        } else {
            orbit.addCenterObject(old);
            return false;
        }

    }

    /**
     * add a relation between p1 and p2.
     *
     * @param p1     person 1 as source of a relation
     * @param p2     person 2 as target of a relation
     * @param weight weight
     * @return true
     * @throws ObjectNotExistException throws when p1 or p2 is not in the orbit
     */
    public boolean addRelation(final SocialPerson p1,
                               final SocialPerson p2,
                               final double weight)
            throws ObjectNotExistException {
        if (p1 == orbit.getCenterObject()) {
            // p1 is center
            if (!orbit.containsLevel(1)) {      // make sure level 1 is in the orbit
                try {
                    orbit.addTrack(1, 1);
                } catch (TrackDidExistException e) {
                    e.printStackTrace();
                    log.info("已存在" + 1 + "号轨道");
                }
            }
            if (orbit.containsObject(p2)) {
                // p1是中心，p2已存在
                int level1 = orbit.getLevelByObject(p2);
                orbit.addRelationWithCenter(p2, weight);
                if (level1 != 1) {
                    // 从旧的位置里移除，加到新的里
                    changePosition(p2, new Position(1, 1, Math.abs((random.nextInt() * 1767) % 360)));
                }
            } else {
                // p1是中心，p2不存在
                try {
                    // 先添加p2这个人
                    orbit.addToTrack(p2,
                            1,
                            1,
                            Math.abs((random.nextInt() * 1767) % 360));
                    nameMap.put(p2.getName(), p2);
                } catch (NoSuchLevelOfTrackException e) {
                    e.printStackTrace();
                    log.error("仍然尝试向不存在的轨道添加物体");
                    assert false;
                }
            }
            // add relation with center
            // 现在p2一定存在
            orbit.addRelationWithCenter(p2, weight);
        } else if (orbit.containsObject(p1)) {
            // 轨道里有p1
            if (orbit.containsObject(p2)) {
                // p1 and p2 both are in the orbit
                int level1 = orbit.getLevelByObject(p2);
                orbit.addRelation(p1, p2, weight);

                int level2 = orbit.getLevelByObject(p1) + 1;

//                int level2 = calculateLevel(p2.getName());

                if (level2 < level1) {
//                    log.info(level2);
                    // 从旧的位置里移除，加到新的里
                    changePosition(p2,
                            new Position(level2,
                                    level2,
                                    Math.abs((random.nextInt() * 1767) % 360)));
                }
            } else {
                // p2 is firstly be added
                try {
                    int level2 = orbit.getLevelByObject(p1) + 1;
                    if (!orbit.containsLevel(level2)) {
                        orbit.addTrack(level2, level2);
                    }
                    orbit.addToTrack(p2, level2, 1, 1);
                    nameMap.put(p2.getName(), p2);
                    orbit.addRelation(p1, p2, weight);
//                    log.info("p2 not exist, successfully added to track at level 1");
//                    addRelation(p1, p2, weight);
                } catch (NoSuchLevelOfTrackException e) {
                    log.info("Dealing with NoSuchLevelOfTrackException...");
                    if (!orbit.containsLevel(1)) {
                        log.info("Auto add level 1");
                        try {
                            orbit.addTrack(1, 1);
                            log.info("Successfully auto added track 1");
                        } catch (TrackDidExistException ignored) {
                        }
                    }
                } catch (TrackDidExistException ignored) {
                }
            }
        } else {
            // 轨道里没p1
            throw new PersonNotExistException("名称为" + p1.getName() + "的人物不存在，因其作为起点，所以不能添加关系");
        }
//        checkRep();
        return true;
    }

    /**
     * add relation by names.
     *
     * @param name1  person 1's name
     * @param name2  person 2's name
     * @param weight weight of the relation
     * @return true
     * @throws PersonNotExistException when name1 is not exist,
     *                                 this exception will be thrown
     */
    public boolean addRelation(final String name1,
                               final String name2,
                               final double weight)
            throws PersonNotExistException {
        SocialPerson p1 = nameMap.get(name1);
        SocialPerson p2 = nameMap.get(name2);
//        if (p1 == null && p2 == null) {
//            failed.add(name1);
//            failed.add(name2);
//            return false;
//        }

        if (p1 == null) {
            p1 = PhysicalObjectPool.getInstance().person(name1, 20, 'M');
        }
        if (p2 == null) {
            p2 = PhysicalObjectPool.getInstance().person(name2, 20, 'M');
        }
        try {
            return addRelation(p1, p2, weight);
        } catch (ObjectNotExistException e) {
            throw new PersonNotExistException();
        }
    }

    /**
     * remove a people relation's detail implementation.
     *
     * @param p1 relation source
     * @param p2 relation target
     * @return true iff succeeded in removing
     * @throws PersonNotExistException throws when p1 or p2 is not in
     *                                 the orbit, or
     */
    private boolean removeRelation(final SocialPerson p1,
                                   final SocialPerson p2)
            throws PersonNotExistException {
        boolean b;
        try {
            b = orbit.removeRelation(p1, p2);
        } catch (ObjectNotExistException e) {
            throw new PersonNotExistException("输入的人名不存在");
        }
        if (b) {
            int newLevel = calculateLevel(p2.getName());
            if (newLevel == -1) {
                removePerson(p2.getName());
                return true;
            } else {
                try {
                    orbit.changePosition(p2,
                            new Position(newLevel,
                                    newLevel,
                                    Math.abs((random.nextInt() * 1767) % 360)));
                } catch (ObjectNotExistException e) {
                    throw new PersonNotExistException("输入的人名不存在");
                }
            }
        }
        return b;
    }

    /**
     * remove a people relation.
     *
     * @param name1 relation source
     * @param name2 relation target
     * @return true iff succeeded in removing
     * @throws PersonNotExistException throws when p1 or p2 is not exist
     */
    public boolean removeRelation(final String name1,
                                  final String name2)
            throws PersonNotExistException {
        SocialPerson p1 = nameMap.get(name1);
        if (p1 == null) {
            throw new PersonNotExistException();
        }
        SocialPerson p2 = nameMap.get(name2);
        if (p2 == null) {
            throw new PersonNotExistException();
        }
        try {
            return removeRelation(p1, p2);
        } catch (ObjectNotExistException e) {
            throw new PersonNotExistException();
        }
    }

    /**
     * @return center user.
     */
    public SocialPerson getCentralUser() {
        return orbit.getCenterObject();
    }

    /**
     * calculate which level should the person with name be in.
     *
     * @param name the person's name you want to calculate the level
     * @return level if succeeded find the level, -1 if name does not exist
     */
    public int calculateLevel(final String name) {
        return orbit.getDistanceBetween(orbit.getCenterObject().getName(), name);
    }

    /**
     * get a relation map.
     *
     * @param person the person you want to get his/her relation
     * @return a relation map, containing
     * string as name, double as weight
     */
    public Map<String, Double> getRelationByPerson(final SocialPerson person) {
        return orbit.targets(person.getName());
    }

    /**
     * use name to get his/her relation map like
     * the getRelationByPerson(SocialPerson person) method.
     *
     * @param name name of a person you want to get the relation
     * @return a relation map, containing string as name, double as weight
     */
    public Map<String, Double> getRelationByPerson(final String name) {
        SocialPerson person = nameMap.get(name);
        if (person == null) {
            return new HashMap<>();
        }
        return getRelationByPerson(person);
    }

    /**
     * get a list of social person by level.
     *
     * @param level level you want to get people
     * @return a list of social person objects on level
     */
    public List<SocialPerson> getObjectsByLevel(final int level) {
        return orbit.getObjectsByLevel(level);
    }

    /**
     * get position by person object.
     *
     * @param person person you want to get position
     * @return position object
     */
    public Position getPosition(final SocialPerson person) {
        return orbit.getPosition(person);
    }

    /**
     * get position by person object.
     *
     * @param name person's name you want to get position
     * @return position object
     */
    public Position getPosition(final String name) {
        return orbit.getPosition(nameMap.get(name));
    }

    /**
     * get people names' set.
     *
     * @return an unmodifiable set of people names
     */
    public Set<String> getPeopleNameSet() {
        return Collections.unmodifiableSet(this.nameMap.keySet());
    }


    /**
     * get track count.
     *
     * @return track count
     */
    public int trackCount() {
        return orbit.trackCount();
    }

    /**
     * change position.
     *
     * @param person      person object you want to change his position
     * @param newPosition new position
     * @return old position
     * @throws ObjectNotExistException throws when the
     *                                 passed in person is not
     *                                 exist in the orbit model
     */
    private Position changePosition(final SocialPerson person,
                                    final Position newPosition)
            throws ObjectNotExistException {
        return orbit.changePosition(person, newPosition);
    }

    /**
     * change position.
     *
     * @param name        person's name you want to change his position
     * @param newPosition new position
     * @return old position
     * @throws ObjectNotExistException throws when the
     *                                 passed in person is not
     *                                 exist in the orbit model
     */
    public Position changePosition(final String name,
                                   final Position newPosition)
            throws ObjectNotExistException {
        return orbit.changePosition(nameMap.get(name), newPosition);
    }

    /**
     * calculate diffusion.
     *
     * @param person person you want to calculate
     * @return the diffusion of person
     */
    private double calculateDiffusion(final SocialPerson person) {
        double diffusion = 0;
        Map<String, Double> map = getRelationByPerson(person);
        for (double weight : map.values()) {
            diffusion += weight;
        }
        return diffusion;
    }

    /**
     * calculate diffusion with name.
     *
     * @param name name of person
     * @return diffusion
     */
    public double calculateDiffusion(final String name) {
        SocialPerson person = nameMap.get(name);
        if (name == null) {
            return -1;
        }
        return calculateDiffusion(person);
    }

    /**
     * this private method is protected by the overloaded method.
     *
     * @param p1 person1
     * @param p2 person2
     * @return physical distance
     */
    private double getPhysicalDistance(final SocialPerson p1,
                                       final SocialPerson p2) {
        Position position1 = orbit.getPosition(p1);
        Position position2 = orbit.getPosition(p2);

        return Math.abs(Math.cos(position1.getLevel()
                * position1.getAngle())
                * (position2.getLevel()
                + position2.getAngle()));
    }

    /**
     * get physical distance.
     *
     * @param name1 from
     * @param name2 to
     * @return physical distance
     */
    public double getPhysicalDistance(final String name1, final String name2) {
        SocialPerson p1 = nameMap.get(name1);
        if (p1 == null) {
            return -1;
        }
        SocialPerson p2 = nameMap.get(name2);
        if (p2 == null) {
            return -1;
        }
        return getPhysicalDistance(p1, p2);
    }

    /**
     * calculate logical distance.
     *
     * @param name1 name1 of person
     * @param name2 name2 of person
     * @return logical distance
     */
    public int getLogicalDistance(final String name1, final String name2) {
        return orbit.getDistanceBetween(name1, name2);
    }

    /**
     * remove person from orbit model.
     *
     * @param name name of a person
     * @return true iff success
     * @throws PersonNotExistException throws
     *                                 when no person's
     *                                 name is @code name
     */
    public boolean removePerson(final String name)
            throws PersonNotExistException {
        SocialPerson person = nameMap.get(name);
        if (person == null) {
            throw new PersonNotExistException("名称为" + name + "的人物不存在");
        }
        nameMap.remove(name);
        try {
            orbit.removeObject(person);
        } catch (ObjectNotExistException e) {
            e.printStackTrace();
            throw new PersonNotExistException();
        }
        return true;
    }

    /**
     * get relation map of sources while the target is @code name.
     *
     * @param name name of a person you want to get sources
     * @return relation map containing string and double
     */
    public Map<String, Double> sources(final String name) {
        return orbit.sources(name);
    }

    public void writeBackWithPrintWriter() throws FileNotFoundException {
        long time1 = System.currentTimeMillis();
        PrintWriter p = new PrintWriter("src/txt/New SocialNetworkCircle_PrintWriter.txt");
        p.println(String.format(
                "CentralUser ::= <%s,%d,%c>\n",
                getCentralUser().getName(),
                getCentralUser().getAge(),
                getCentralUser().getSex()));
        // print Friends
        for (SocialPerson person : nameMap.values()) {
            if (person == orbit.getCenterObject()) {
                continue;
            }
            p.println(String.format(
                    "Friend ::= <%s, %d, %c>\n",
                    person.getName(),
                    person.getAge(),
                    person.getSex()));
        }
        // print relations
        for (int level = 1, size = trackCount(); level <= size; level++) {
            List<SocialPerson> people = getObjectsByLevel(level);

            for (SocialPerson person : people) {
                Map<String, Double> relations = getRelationByPerson(person);
                for (Map.Entry<String, Double> entry : relations.entrySet()) {
                    p.println(String.format("SocialTie ::= <%s, %s, %.3f>\n",
                            person.getName(),
                            entry.getKey(),
                            entry.getValue()));
                }
            }

        }
        long time2 = System.currentTimeMillis();

        log.info("准备数据+使用PrintWriter写出文件SocialNetworkCircle共花费时间：" + (time2 - time1));

        p.flush();
        p.close();
    }

    @Override
    public void writeBackWithStream() throws IOException {
        long time1 = System.currentTimeMillis();

        FileOutputStream stream = new FileOutputStream("src/txt/New SocialNetworkCircle_Stream.txt");
        StringBuilder sb = new StringBuilder();

        sb.append(String.format(
                "CentralUser ::= <%s,%d,%c>\n",
                getCentralUser().getName(),
                getCentralUser().getAge(),
                getCentralUser().getSex()));

        for (SocialPerson person : nameMap.values()) {
            if (person == orbit.getCenterObject()) {
                continue;
            }
            sb.append(String.format(
                    "Friend ::= <%s, %d, %c>\n",
                    person.getName(),
                    person.getAge(),
                    person.getSex()));
        }

        // print relations
        for (int level = 1, size = trackCount(); level <= size; level++) {
            List<SocialPerson> people = getObjectsByLevel(level);

            for (SocialPerson person : people) {
                Map<String, Double> relations = getRelationByPerson(person);
                for (Map.Entry<String, Double> entry : relations.entrySet()) {
                    sb.append(String.format("SocialTie ::= <%s, %s, %.3f>\n",
                            person.getName(),
                            entry.getKey(),
                            entry.getValue()));
                }
            }

        }

        long time2 = System.currentTimeMillis();
        stream.write(sb.toString().getBytes(StandardCharsets.UTF_8));
        long time3 = System.currentTimeMillis();

        log.info("使用Stream写出文件SocialNetworkCircle共花费时间：" + (time3 - time2));
        log.info("准备数据+写出文件SocialNetworkCircle共花费时间：" + (time3 - time1));

        stream.flush();
        stream.close();
    }

    @Override
    public void writeBackWithBuffer() throws IOException {
        long time1 = System.currentTimeMillis();

        OutputStream output = new FileOutputStream("src/txt/New SocialNetworkCircle_Buffer.txt");
        BufferedOutputStream stream = new BufferedOutputStream(output);

        StringBuilder sb = new StringBuilder();

        sb.append(String.format(
                "CentralUser ::= <%s,%d,%c>\n",
                getCentralUser().getName(),
                getCentralUser().getAge(),
                getCentralUser().getSex()));

        for (SocialPerson person : nameMap.values()) {
            if (person == orbit.getCenterObject()) {
                continue;
            }
            sb.append(String.format(
                    "Friend ::= <%s, %d, %c>\n",
                    person.getName(),
                    person.getAge(),
                    person.getSex()));
        }

        // print relations
        for (int level = 1, size = trackCount(); level <= size; level++) {
            List<SocialPerson> people = getObjectsByLevel(level);

            for (SocialPerson person : people) {
                Map<String, Double> relations = getRelationByPerson(person);
                for (Map.Entry<String, Double> entry : relations.entrySet()) {
                    sb.append(String.format("SocialTie ::= <%s, %s, %.3f>\n",
                            person.getName(),
                            entry.getKey(),
                            entry.getValue()));
                }
            }

        }

        long time2 = System.currentTimeMillis();
        stream.write(sb.toString().getBytes(StandardCharsets.UTF_8));
        long time3 = System.currentTimeMillis();


        log.info("使用Buffered Stream写出文件SocialNetworkCircle共花费时间：" + (time3 - time2));
        log.info("准备数据+写出文件SocialNetworkCircle共花费时间：" + (time3 - time1));

        output.flush();
        stream.flush();
        stream.close();
        output.close();

    }

}
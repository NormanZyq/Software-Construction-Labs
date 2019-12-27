package Application.TrackGame;

import MyException.CircularOrbitExceotion.ObjectDoesNotExistException;
import MyException.TrackGameException.AthleteDidExistException;
import MyException.CircularOrbitExceotion.TrackDidExistException;
import MyException.CircularOrbitExceotion.NoSuchLevelOfTrackException;
import MyException.TrackGameException.AthleteDoesNotExistException;
import abs.*;
import abs.center.EmptyObject;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.metadata.Table;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.sun.istack.internal.NotNull;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.*;

/**
 * This class represents a track game, which contains some tracks,
 * athletes and arrangement
 */
public class TrackGame implements OrbitAble {
    private Logger log = Logger.getLogger(TrackGame.class);

    public static TrackGame init(Set<Athlete> athletes, int game, int numberOfTracks) {
        return new TrackGame(athletes, game, numberOfTracks);
    }

    public static TrackGame empty() {
        return new TrackGame();
    }

    private ConcreteCircularOrbit<EmptyObject, Athlete> orbit = new ConcreteCircularOrbit<>();

    /*
        AF
        this class represents a race

        RI
        no same athlete in a same group

        safety from exposure:
        the class does not expose a mutable value directly, instead, it's a copied version
        all mutator are under control, each operation will be followed with a checkRep()


     */

    private void checkRep() {
        // no center object, numberOfTrack is in 4-10;
        // each track contains at most 1 athlete
    }

    /**
     * all athletes set
     */
    private Set<Athlete> athletes = new HashSet<>();

    /**
     * type of game,
     * can only be chosen in 100/200/400
     */
    private int game = 0;

    private int numberOfTracks = 0;

    private Map<String, Athlete> nameMap = new HashMap<>();

    private Map<Integer, Athlete> groupMap = new HashMap<>();

    private List<Arrangement> arrangements = new ArrayList<>();

    private String currentArrangementMethod = null;

    /**
     * empty constructor
     */
    public TrackGame() {
    }

    /**
     * Constructor with some initial arguments
     *
     * @param athletes       A set off athletes
     * @param game           Game count
     * @param numberOfTracks Number of tracks
     */
    public TrackGame(Set<Athlete> athletes, int game, int numberOfTracks) {
        for (Athlete athlete : athletes) {
            try {
                this.addAthlete(athlete);
            } catch (AthleteDidExistException e) {
                e.printStackTrace();
                log.info("自动添加过程出错，运动员有重名，已被忽略");
            }
        }
        this.game = game;
        this.numberOfTracks = numberOfTracks;
        try {
            autoAddTrack();
        } catch (TrackDidExistException e) {
//            e.printStackTrace();
            log.info("自动添加轨道中尝试添加已存在的轨道");
        }
    }

    /**
     * add an athlete with only athlete object and default track
     *
     * @param athlete The athlete you want to add
     * @return True if succeeded in adding, otherwise false
     */
    public boolean addAthlete(Athlete athlete) throws AthleteDidExistException {
        currentArrangementMethod = null;
        return this.addAthlete(athlete, 1, 0);
    }

    /**
     * Add an athlete with track and angle
     *
     * @param athlete The athlete you want to add
     * @param track   The track of the athlete should be in
     * @param angle   The initial angle of the athlete
     * @return True is succeeded in adding, otherwise false
     */
    public boolean addAthlete(Athlete athlete, int track, int angle) throws AthleteDidExistException {
        boolean b = athletes.add(athlete);

        if (nameMap.keySet().contains(athlete.getName())) {
            // contains an athlete with same name
            throw new AthleteDidExistException("名称为" + athlete.getName() + "的运动员已存在");
        } else {
            nameMap.put(athlete.getName(), athlete);

            if (!orbit.containsLevel(track)) {
                try {
                    orbit.addTrack(track, track);
                    log.info("自动添加" + track + "号跑道");
                } catch (TrackDidExistException ignored) { }
            }

            // now must contain this level of track
            try {
                orbit.addToTrack(athlete, track, track, angle);
            } catch (NoSuchLevelOfTrackException e) {
                log.error("仍然尝试向不存在的轨道添加物体");
            }

            currentArrangementMethod = null;
            checkRep();
            return b;
        }

    }

    /**
     * Auto add tracks with the variable track count
     */
    private void autoAddTrack() throws TrackDidExistException {
        for (int i = 1; i <= numberOfTracks; i++) {
            orbit.addTrack(i, i);
        }
    }

    /**
     * remove an athlete
     *
     * @param athlete the athlete want to remove
     * @return True if succeeded in removing, otherwise false
     */
    public boolean removeAthlete(Athlete athlete) throws AthleteDoesNotExistException {
        try {
            orbit.removeObject(athlete);
            athletes.remove(athlete);
            currentArrangementMethod = null;
            checkRep();
            return true;
        } catch (ObjectDoesNotExistException e) {
			throw new AthleteDoesNotExistException("名称是" + athlete.getName() + "的运动员不存在");
        }
    }

    public void removeAthlete(String name) throws AthleteDoesNotExistException {
        Athlete athlete = nameMap.get(name);
        if (athlete == null) return;      // does not exist
        // the athlete with passed in name must exist
        removeAthlete(athlete);
    }

    /**
     * change athlete's position
     *
     * @param athleteName The name of the athlete
     * @param level       The new level of the athlete should be in
     * @param radius      The new radius of this athlete
     * @param angle       The new angle of this athlete
     * @return True if succeeded in changing the position, Otherwise false
     */
    public boolean changeAthletePosition(String athleteName, int level, double radius, double angle) {
        Athlete athleteToChange = nameMap.get(athleteName);
        if (athleteToChange == null) {
            // not exist, no changes
            checkRep();
            return false;
        }
//        Position p = new Position(level, radius, angle);
        if (orbit.getLevelByObject(athleteToChange) == -1) {
            // no such athlete
            checkRep();
            return false;
        }
        currentArrangementMethod = null;
        checkRep();
        return true;
    }

    public boolean exchangePosition(String name1, String name2) {
        Athlete a1 = nameMap.get(name1);
        Athlete a2 = nameMap.get(name2);
        if (a1 == null || a2 == null) return false;

        // must exist

        Arrangement arrangement1 = null;
        Arrangement arrangement2 = null;
        // 两个地方要改，一个是group map，另一个是arrangements
        List<Arrangement> inLoop = this.arrangements;
        for (int i = 0, arrangements1Size = inLoop.size(); i < arrangements1Size; i++) {
            Arrangement arrangement = inLoop.get(i);
            if (name1.equals(arrangement.getAthlete())) {
                arrangement1 = arrangement;
                continue;
            }
            if (name2.equals(arrangement.getAthlete())) {
                arrangement2 = arrangement;
            }
        }

        assert arrangement2 != null;
        assert arrangement1 != null;

        int nG1 = arrangement2.getGroup();
        int nG2 = arrangement1.getGroup();

        int nR1 = arrangement2.getRaceNumber();
        int nR2 = arrangement1.getRaceNumber();

        arrangement1.setGroup(nG1);
        arrangement2.setGroup(nG2);

        arrangement1.setRaceNumber(nR1);
        arrangement2.setRaceNumber(nR2);

        currentArrangementMethod = "Customized";
        return true;
    }

    /**
     * arrange a game
     *
     * @param arranger The arranger you are going to use
     */
    public void arrange(TrackArranger arranger) {
        arranger.arrange(this);
    }

    /**
     * 获得运动员set
     *
     * @return A copied version of the athletes set
     */
    public Set<Athlete> getAthletes() {
        return Collections.unmodifiableSet(athletes);
    }

    /**
     * 获得比赛场数
     *
     * @return Returns the game
     */
    public int getGame() {
        return game;
    }

    /**
     * 获得轨道个数
     *
     * @return Returns the number of the tracks
     */
    public int getNumberOfTracks() {
        return numberOfTracks;
    }

    /**
     * Get an athlete's position by the athlete
     *
     * @param athlete the athlete you want to get position
     * @return position
     */
    public Position getAthletePosition(Athlete athlete) {
        return orbit.getPosition(athlete);
    }

    /**
     * get the arrangement list
     *
     * @return Return a copied version of the arrangement
     */
    public List<Arrangement> getArrangements() {
        return Collections.unmodifiableList(this.arrangements);
    }

    /**
     * 设置安排结果
     * 包外无法访问
     *
     * @param arrangements The arrangement
     */
    void setArrangements(List<Arrangement> arrangements) {
        this.arrangements = arrangements;
        // set group map
        for (Arrangement arrangement : arrangements) {
            groupMap.put(arrangement.getGroup(), nameMap.get(arrangement.getAthlete()));
        }
    }

    public String writeArrangementToFile() throws IOException {
        String filename = "Customized Arrangement.xlsx";
        if ("Random".equals(currentArrangementMethod)) {
            filename = "Random Arrangement.xlsx";
        } else if ("Faster-Later".equals(currentArrangementMethod)) {
            filename = "Faster-Later Arrangement.xlsx";
        }
        try (OutputStream out = new FileOutputStream(filename)) {
            ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX);
            Sheet sheet1 = new Sheet(1, 0);
            sheet1.setSheetName("Arrangement Table");

            // 准备表头
            List<List<String>> head = new ArrayList<>();
            List<String> nameList = new ArrayList<>();
            List<String> ageList = new ArrayList<>();
            List<String> numberList = new ArrayList<>();
            List<String> countryList = new ArrayList<>();
            List<String> bestScoreList = new ArrayList<>();
            List<String> groupList = new ArrayList<>();
            List<String> trackNumberList = new ArrayList<>();
            nameList.add("Name");
            ageList.add("Age");
            numberList.add("Number");
            countryList.add("Country");
            bestScoreList.add("Best Score");
            groupList.add("Group");
            trackNumberList.add("Track");
            head.add(nameList);
            head.add(ageList);
            head.add(numberList);
            head.add(countryList);
            head.add(bestScoreList);
            head.add(groupList);
            head.add(trackNumberList);

            // 创建表格
            Table table = new Table(1);
            table.setHead(head);    // 设置表头

            // 准备内容
            List<List<String>> data = new ArrayList<>();

            for (Arrangement arrangement : arrangements) {
                List<String> item = new ArrayList<>();
                Athlete currentAthlete = nameMap.get(arrangement.getAthlete());

                item.add(currentAthlete.getName());
                item.add(String.valueOf(currentAthlete.getAge()));
                item.add(String.valueOf(currentAthlete.getNumber()));
                item.add(currentAthlete.getCountry());
                item.add(String.valueOf(currentAthlete.getBestScoreInYear()));
                item.add(String.valueOf(arrangement.getGroup()));
                item.add(String.valueOf(arrangement.getRaceNumber()));

                data.add(item);

            }

            writer.write0(data, sheet1, table);
            writer.finish();
            return filename;
        }
    }

    public void setCurrentArrangementMethod(String currentArrangementMethod) {
        this.currentArrangementMethod = currentArrangementMethod;
    }

    /**
     * add a new track
     *
     * @param level
     * @param radius
     * @return
     */
    public boolean addTrack(int level, double radius) throws TrackDidExistException {
        boolean b = orbit.addTrack(level, radius);
        if (b) numberOfTracks++;
        checkRep();
        return b;
    }

    /**
     * remove a track from track game
     * @param level
     * @return
     */
    public boolean removeTrack(int level) throws NoSuchLevelOfTrackException {
        if (!orbit.containsLevel(level))
            throw new NoSuchLevelOfTrackException(level + "层跑道不存在");  // level does not exist

        // level exist

        // remove them from name map
        List<Athlete> athleteList = orbit.getObjectsByLevel(level);
        for (Athlete athlete : athleteList) {
            nameMap.remove(athlete.getName());
        }

        // remove them from group map
        Set<Integer> groupSet = groupMap.keySet();
        for (int group : groupSet) {
            Athlete athleteInGroupMap = groupMap.get(group);
            for (Athlete athleteToRemove : athleteList) {
                if (athleteInGroupMap == athleteToRemove) {
                    groupMap.remove(group);
                }
            }
        }

        // remove from arrangements
        for (Iterator<Arrangement> iterator = arrangements.iterator(); iterator.hasNext(); ) {
            Arrangement arrangement = iterator.next();
            for (Athlete athlete : athleteList) {
                String name = arrangement.getAthlete();
                if (name.equals(athlete.getName())) {
                    iterator.remove();
                }
            }
        }

        // remove from orbit
        try {
            orbit.removeTrack(level);
        } catch (NoSuchLevelOfTrackException ex) {
            throw new RuntimeException();
        }
        
        currentArrangementMethod = "Customized";

        return true;
    }
}

class Arrangement {
    private final String athleteName;
    private int raceNumber;
    private int group;

    Arrangement(@NotNull String athlete, int raceNumber, int group) {
        this.athleteName = athlete;
        this.raceNumber = raceNumber;
        this.group = group;
    }

    public void setRaceNumber(int raceNumber) {
        this.raceNumber = raceNumber;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    String getAthlete() {
        return athleteName;
    }

    int getRaceNumber() {
        return raceNumber;
    }

    int getGroup() {
        return group;
    }
}

/**
 * This class is an arranger which used to arrange a game in a random way
 */
class RandomArranger extends TrackArranger {
    /**
     * random arrange
     * group from 1 to n, n is decided by tracks.
     * size / tracks 向上取整 = n
     *
     * @param trackGame track game
     */
    @Override
    void arrange(TrackGame trackGame) {
        List<Athlete> copied = new ArrayList<>(trackGame.getAthletes());

        int size = copied.size();
        int tracks = trackGame.getNumberOfTracks();

//        int groupCount;
//        groupCount = size % tracks == 0 ? size / tracks : size / tracks + 1;

        List<Arrangement> arrangement = new ArrayList<>();

        // index
        int i = 1;

        Random r = new Random(System.currentTimeMillis());      // random & seed
        while (i <= size) {
            int random = Math.abs(r.nextInt() % (size - i + 1));   // random

            // get athlete
            Athlete current = copied.get(random);

            // calculate current group
            int currentGroup = i % tracks == 0 ? i / tracks : i / tracks + 1;

            arrangement.add(new Arrangement(current.getName(), (i % tracks) + 1, currentGroup));        // add to arrangement
            copied.remove(random);      // remove it from copied
            i++;
        }

        // finish arranging
        trackGame.setCurrentArrangementMethod("Random");
        trackGame.setArrangements(arrangement);
    }
}

/**
 * This class is an arranger which used to arrange a game in:
 * The stronger athlete will be in the game later
 * So it is called stronger later Arranger
 */
class FasterLater extends TrackArranger {
    @SuppressWarnings("Duplicates")
    @Override
    void arrange(TrackGame trackGame) {
        List<Athlete> copied = new ArrayList<>(trackGame.getAthletes());

        int size = copied.size();
        int tracks = trackGame.getNumberOfTracks();

        // 创建两个整数List，存放赛道
        List<Integer> left = new ArrayList<>();
        List<Integer> right = new ArrayList<>();
        int leftSize = tracks / 2;
        int rightSize = tracks / 2 + 1;
        for (int i = leftSize; i >= 1; i--) {
            left.add(i);
        }
        for (int i = rightSize; i <= tracks; i++) {
            right.add(i);
        }

        Collections.reverse(left);
        Collections.reverse(right);


        /*
            越厉害的人越晚出场（group越后）
            越厉害的赛道越靠中间
         */

        copied.sort(Comparator.comparingDouble(Athlete::getBestScoreInYear));   // 耗时越低的越在前面

        Collections.reverse(copied);

        List<Arrangement> arrangements = new ArrayList<>();

        // 现在要安排两件事：一是赛道，二是group，并且组优先
        // 赛道要从中间开始，然后向右，再回左
        int i = 1;
        boolean isEven = true;
        boolean leftVisited = false;
        boolean rightVisited = false;
        int trackIndex = 0;    // 左比右至多少1
//        int groupCount = size % tracks == 0 ? size / tracks : size / tracks + 1;
        while (i <= size) {
            // get athlete
            Athlete current = copied.get(i - 1);

            // calculate current group
            int currentGroup = i % tracks == 0 ? i / tracks : i / tracks + 1;

            Arrangement arrangement;
            int index;

            if (trackIndex == leftSize || trackIndex == rightSize) {
                trackIndex = 0;
                leftVisited = false;
                rightVisited = false;
                continue;
            }

            if (!rightVisited) {
                // visit right
                index = right.get(trackIndex);
                rightVisited = true;
            } else if (!leftVisited) {
                // visit left
                index = left.get(trackIndex);
                leftVisited = true;
            } else {
                // all visited
                trackIndex++;
                leftVisited = false;
                rightVisited = false;
                continue;
            }

            arrangement = new Arrangement(current.getName(), index, currentGroup);
            arrangements.add(arrangement);

            i++;
        }

        trackGame.setCurrentArrangementMethod("Faster-Later");
        trackGame.setArrangements(arrangements);

    }
}


















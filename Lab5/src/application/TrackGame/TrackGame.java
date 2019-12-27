package application.TrackGame;

import MyException.CircularOrbitExceotion.ObjectNotExistException;
import MyException.TrackGameException.AthleteDidExistException;
import MyException.CircularOrbitExceotion.TrackDidExistException;
import MyException.CircularOrbitExceotion.NoSuchLevelOfTrackException;
import MyException.TrackGameException.AthleteDoesNotExistException;
import abs.ConcreteCircularOrbit;
import abs.OrbitAble;
import abs.PhysicalObject;
import abs.Position;
import abs.center.EmptyObject;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.metadata.Table;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.sun.istack.internal.NotNull;
import org.apache.log4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * This class represents a track game, which contains some tracks,
 * athletes and arrangement.
 */
public final class TrackGame implements OrbitAble {

    /*
        AF
        this class represents a race

        RI
        no same athlete in a same group
        no center object, numberOfTrack is in 4-10;
        each track contains at most 1 athlete

        safety from exposure:
        the class does not expose a mutable value directly,
        instead, it's a copied version
        all mutator are under control, each operation will
         be followed with a checkRep()
     */

    /**
     * logger.
     */
    private Logger log = Logger.getLogger(TrackGame.class);

    /**
     * delegate to ConcreteCircularOrbit to implement track game.
     */
    private ConcreteCircularOrbit<EmptyObject, Athlete> orbit = new ConcreteCircularOrbit<>();

    /**
     * all athletes set.
     */
    private Set<Athlete> athletes = new HashSet<>();

    /**
     * type of game,
     * can only be chosen in 100/200/400.
     */
    private int game = 0;

    /**
     * number of track.
     */
    private int numberOfTracks = 0;

    /**
     * a name map.
     */
    private Map<String, Athlete> nameMap = new HashMap<>();

    /**
     * group map.
     */
    private Map<Integer, Athlete> groupMap = new HashMap<>();

    /**
     * arrangements.
     */
    private List<Arrangement> arrangements = new ArrayList<>();

    /**
     * a mark of current arrangement method.
     */
    private String currentArrangementMethod = null;

    /**
     * empty constructor.
     */
    public TrackGame() {
        orbit.addCenterObject(new EmptyObject(""));
    }

    /**
     * Constructor with some initial arguments.
     *
     * @param athletes       A set off athletes
     * @param game           Game count
     * @param numberOfTracks Number of tracks
     */
    public TrackGame(final Set<Athlete> athletes,
                     final int game,
                     final int numberOfTracks) {
        orbit.addCenterObject(new EmptyObject(""));
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
     * init method.
     *
     * @param athletes       init athletes
     * @param game           100/200/400
     * @param numberOfTracks number of track
     * @return TrackGame object
     */
    public static TrackGame init(final Set<Athlete> athletes,
                                 final int game,
                                 final int numberOfTracks) {
        return new TrackGame(athletes, game, numberOfTracks);
    }

    /**
     * create a new empty track game object.
     *
     * @return a new empty track game object
     */
    public static TrackGame empty() {
        return new TrackGame();
    }

    /**
     * check RI.
     */
    private void checkRep() {
        assert orbit.getCenterObject().getClass() == EmptyObject.class;
    }

    /**
     * add an athlete with only athlete object and default track.
     *
     * @param athlete The athlete you want to add
     * @return True if succeeded in adding, otherwise false
     * @throws AthleteDidExistException throws when
     *                                  athlete's name is already in orbit
     */
    public boolean addAthlete(final Athlete athlete)
            throws AthleteDidExistException {
        currentArrangementMethod = null;
        return this.addAthlete(athlete, 1, 0);
    }

    /**
     * Add an athlete with track and angle.
     *
     * @param athlete The athlete you want to add
     * @param track   The track of the athlete should be in
     * @param angle   The initial angle of the athlete
     * @return True is succeeded in adding, otherwise false
     * @throws AthleteDidExistException throws when throws
     *                                  when athlete's name is already in orbit
     */
    public boolean addAthlete(final Athlete athlete,
                              final int track,
                              final int angle)
            throws AthleteDidExistException {
        boolean b = athletes.add(athlete);

        if (nameMap.keySet().contains(athlete.getName())) {
            // contains an athlete with same name
            throw new AthleteDidExistException("名称为"
                    + athlete.getName() + "的运动员已存在");
        } else {
            nameMap.put(athlete.getName(), athlete);

            if (!orbit.containsLevel(track)) {
                try {
                    orbit.addTrack(track, track);
                    log.info("自动添加" + track + "号跑道");
                } catch (TrackDidExistException ignored) {
                }
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
     * Auto add tracks with the variable track count.
     *
     * @throws TrackDidExistException throws when adding a existed track
     */
    private void autoAddTrack() throws TrackDidExistException {
        for (int i = 1; i <= numberOfTracks; i++) {
            orbit.addTrack(i, i);
        }
    }

    /**
     * remove an athlete.
     *
     * @param athlete the athlete want to remove
     * @return True if succeeded in removing, otherwise false
     * @throws AthleteDoesNotExistException throws when
     *                                      athlete is not in the orbit
     */
    public boolean removeAthlete(final Athlete athlete)
            throws AthleteDoesNotExistException {
        try {
            orbit.removeObject(athlete);
            athletes.remove(athlete);
            currentArrangementMethod = null;
            checkRep();
            return true;
        } catch (ObjectNotExistException e) {
            throw new AthleteDoesNotExistException("名称是"
                    + athlete.getName() + "的运动员不存在");
        }
    }

    /**
     * remove the athlete called @code name.
     *
     * @param name name
     * @throws AthleteDoesNotExistException if name does not exist
     */
    public void removeAthlete(final String name)
            throws AthleteDoesNotExistException {
        Athlete athlete = nameMap.get(name);
        if (athlete == null) {
            throw new AthleteDoesNotExistException("名称为" + name + "的运动员不存在");      // does not exist
        }
        // the athlete with passed in name must exist
        removeAthlete(athlete);
    }

    /**
     * change athlete's position.
     *
     * @param athleteName The name of the athlete
     * @param level       The new level of the athlete should be in
     * @param radius      The new radius of this athlete
     * @param angle       The new angle of this athlete
     * @return True if succeeded in changing the position, Otherwise false
     */
    public boolean changeAthletePosition(final String athleteName,
                                         final int level,
                                         final double radius,
                                         final double angle) {
        Athlete athleteToChange = nameMap.get(athleteName);
        if (athleteToChange == null) {
            // not exist, no changes
            checkRep();
            return false;
        }
        if (orbit.getLevelByObject(athleteToChange) == -1) {
            // no such athlete
            checkRep();
            return false;
        }
        currentArrangementMethod = null;
        checkRep();
        return true;
    }

    /**
     * exchange name1's position with name2.
     *
     * @param name1 person1'name
     * @param name2 person2'name
     * @return true iff succeeding exchanging
     */
    public boolean exchangePosition(final String name1, final String name2) {
        Athlete a1 = nameMap.get(name1);
        Athlete a2 = nameMap.get(name2);
        if (a1 == null || a2 == null) {
            return false;
        }

        // must exist

        Arrangement arrangement1 = null;
        Arrangement arrangement2 = null;
        // 两个地方要改，一个是group map，另一个是arrangements
        List<Arrangement> inLoop = this.arrangements;
        for (int i = 0,
             arrangements1Size = inLoop.size();
             i < arrangements1Size; i++) {
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
     * arrange a game.
     *
     * @param arranger The arranger you are going to use
     */
    public void arrange(final TrackArranger arranger) {
        arranger.arrange(this);
    }

    /**
     * 获得运动员set.
     *
     * @return A copied version of the athletes set
     */
    public Set<Athlete> getAthletes() {
        return Collections.unmodifiableSet(athletes);
    }

    /**
     * 获得比赛场数.
     *
     * @return Returns the game
     */
    public int getGame() {
        return game;
    }

    /**
     * 获得轨道个数.
     *
     * @return Returns the number of the tracks
     */
    public int getNumberOfTracks() {
        return numberOfTracks;
    }

    /**
     * Get an athlete's position by the athlete.
     *
     * @param athlete the athlete you want to get position
     * @return position
     */
    public Position getAthletePosition(final Athlete athlete) {
        return orbit.getPosition(athlete);
    }

    /**
     * get the arrangement list.
     *
     * @return Return a copied version of the arrangement
     */
    public List<Arrangement> getArrangements() {
        return Collections.unmodifiableList(this.arrangements);
    }

    /**
     * 设置安排结果.
     * 包外无法访问
     *
     * @param arrangements The arrangement
     */
    void setArrangements(final List<Arrangement> arrangements) {
        this.arrangements = arrangements;
        // set group map
        for (Arrangement arrangement : arrangements) {
            groupMap.put(arrangement.getGroup(),
                    nameMap.get(arrangement.getAthlete()));
        }
    }

    /**
     * write current arrangement to file.
     *
     * @return filename
     * @throws IOException throws when any wrong interrupts writing
     */
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

    /**
     * set current arrangement name.
     *
     * @param currentArrangementMethod name of an arrangement
     */
    void setCurrentArrangementMethod(final String currentArrangementMethod) {
        this.currentArrangementMethod = currentArrangementMethod;
    }

    /**
     * add a new track.
     *
     * @param level  track's level
     * @param radius track's radius
     * @return true iff success
     * @throws TrackDidExistException throws when level of track
     *                                is exist
     */
    public boolean addTrack(final int level, final double radius)
            throws TrackDidExistException {
        boolean b = orbit.addTrack(level, radius);
        if (b) {
            numberOfTracks++;
        }
        checkRep();
        return b;
    }

    /**
     * remove a track from track game.
     *
     * @param level level of track that you want to remove
     * @return true iff success
     * @throws NoSuchLevelOfTrackException throws when level of
     *                                     track is not exist
     */
    public boolean removeTrack(final int level)
            throws NoSuchLevelOfTrackException {
        if (!orbit.containsLevel(level)) {
            // level does not exist
            throw new NoSuchLevelOfTrackException(level + "层跑道不存在");
        }

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
        for (Iterator<Arrangement> iterator = arrangements.iterator();
             iterator.hasNext(); ) {
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

    @Override
    public void writeBackWithPrintWriter() throws IOException {
        long time1 = System.currentTimeMillis();
        PrintWriter p = new PrintWriter("src/txt/New TrackGame_PrintWriter.txt");

        p.println("Game ::= " + game);
        p.println("NumOfTracks ::= " + numberOfTracks);
        for (Athlete athlete : athletes) {
            p.println(String.format(
                    "Athlete ::= <%s,%d,%s,%d,%.2f>",
                    athlete.getName(),
                    athlete.getNumber(),
                    athlete.getCountry(),
                    athlete.getAge(),
                    athlete.getBestScoreInYear()));
        }

        long time2 = System.currentTimeMillis();

        log.info("准备数据并使用PrintWriter写出文件TrackGame共花费时间：" + (time2 - time1));

        p.flush();
        p.close();
    }

    @Override
    public void writeBackWithStream() throws IOException {
        long time1 = System.currentTimeMillis();
        FileOutputStream stream = new FileOutputStream("src/txt/New TrackGame_Stream.txt");

        StringBuilder sb = new StringBuilder();

        sb.append("Game ::= ").append(game).append('\n').
                append("NumOfTracks ::= ").
                append(numberOfTracks).append('\n');

        for (Athlete athlete : athletes) {
            sb.append(String.format(
                    "Athlete ::= <%s,%d,%s,%d,%.2f>\n",
                    athlete.getName(),
                    athlete.getNumber(),
                    athlete.getCountry(),
                    athlete.getAge(),
                    athlete.getBestScoreInYear()));
        }
        long time2 = System.currentTimeMillis();

        stream.write(sb.toString().getBytes(StandardCharsets.UTF_8));
        long time3 = System.currentTimeMillis();

        log.info("准备数据+写出文件TrackGame共花费时间：" + (time3 - time1));
        log.info("仅使用Stream写出文件TrackGame共花费时间：" + (time3 - time2));

        stream.flush();
        stream.close();
    }

    @Override
    public void writeBackWithBuffer() throws IOException {
        long time1 = System.currentTimeMillis();
        OutputStream output = new FileOutputStream("src/txt/New TrackGame_Buffer.txt");
        BufferedOutputStream stream = new BufferedOutputStream(output);

        StringBuilder sb = new StringBuilder();

        sb.append("Game ::= ").append(game).append('\n').
                append("NumOfTracks ::= ").
                append(numberOfTracks).append('\n');

        for (Athlete athlete : athletes) {
            sb.append(String.format(
                    "Athlete ::= <%s,%d,%s,%d,%.2f>\n",
                    athlete.getName(),
                    athlete.getNumber(),
                    athlete.getCountry(),
                    athlete.getAge(),
                    athlete.getBestScoreInYear()));
        }

        long time2 = System.currentTimeMillis();
        stream.write(sb.toString().getBytes(StandardCharsets.UTF_8));
        long time3 = System.currentTimeMillis();

        log.info("准备数据+写出文件TrackGame共花费时间：" + (time3 - time1));
        log.info("仅使用Buffered Stream写出文件TrackGame共花费时间：" + (time3 - time2));

        output.flush();
        stream.flush();
        stream.close();
        output.close();

    }
}

/**
 * uses as arrangement in track game.
 * mutable
 */
class Arrangement {
    /**
     * athlete name.
     */
    private final String athleteName;

    /**
     * race number.
     */
    private int raceNumber;

    /**
     * group number.
     */
    private int group;

    /**
     * arrangement constructor.
     *
     * @param athlete    athlete's name
     * @param raceNumber race number
     * @param group      group number
     */
    Arrangement(@NotNull final String athlete,
                final int raceNumber,
                final int group) {
        this.athleteName = athlete;
        this.raceNumber = raceNumber;
        this.group = group;
    }

    /**
     * get athlete.
     *
     * @return athlete
     */
    String getAthlete() {
        return athleteName;
    }

    /**
     * get race number.
     *
     * @return race number
     */
    int getRaceNumber() {
        return raceNumber;
    }

    /**
     * set a new race number.
     *
     * @param raceNumber race number
     */
    void setRaceNumber(final int raceNumber) {
        this.raceNumber = raceNumber;
    }

    /**
     * get group.
     *
     * @return group
     */
    int getGroup() {
        return group;
    }

    /**
     * set group number.
     *
     * @param group group number
     */
    void setGroup(final int group) {
        this.group = group;
    }
}

/**
 * This class is an arranger which used to arrange a game in a random way.
 */
final class RandomArranger extends TrackArranger {
    /**
     * random arrange
     * group from 1 to n, n is decided by tracks.
     * size / tracks 向上取整 = n
     *
     * @param trackGame track game
     */
    @Override
    void arrange(final TrackGame trackGame) {
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

            // add to arrangement
            arrangement.add(new Arrangement(current.getName(),
                    (i % tracks) + 1, currentGroup));
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
 * The stronger athlete will be in the game later.
 * So it is called stronger later Arranger
 */
final class FasterLater extends TrackArranger {
    /**
     * this arrange method will arrange the passed in trackGame
     * as "Faster-Later" method.
     *
     * @param trackGame track game you want to arrange
     */
    @SuppressWarnings("Duplicates")
    @Override
    void arrange(final TrackGame trackGame) {
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

        // 耗时越低的越在前面
        copied.sort(Comparator.comparingDouble(Athlete::getBestScoreInYear));

        Collections.reverse(copied);

        List<Arrangement> arrangements = new ArrayList<>();

        // 现在要安排两件事：一是赛道，二是group，并且组优先
        // 赛道要从中间开始，然后向右，再回左
        int i = 1;
        boolean isEven = true;
        boolean leftVisited = false;
        boolean rightVisited = false;
        int trackIndex = 0;    // 左比右至多少1
        // int groupCount = size % tracks == 0 ? size / tracks : size / tracks + 1;
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


















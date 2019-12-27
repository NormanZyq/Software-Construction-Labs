package abs.Parser;

import abs.PhysicalObjectPool;
import application.AtomStructure.AtomStructure;
import application.AtomStructure.Electron;
import application.AtomStructure.Nuclear;
import MyException.CircularOrbitExceotion.MisArgumentException;
import MyException.CircularOrbitExceotion.NoSuchLevelOfTrackException;
import MyException.CircularOrbitExceotion.OrbitFileParseException;
import MyException.CircularOrbitExceotion.TrackDidExistException;
import abs.ConcreteCircularOrbit;
import abs.PhysicalObject;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class AtomStructureScannerParser extends AtomStructureParser {
    /**
     * logger.
     */
    private Logger log = Logger.getLogger(AtomStructureScannerParser.class);

    /**
     * parse a atom structure file.
     *
     * @param file file you want to parse
     * @return after parsing, a new atom structure
     * object will be return
     * @throws FileNotFoundException   if the file does not exist,
     *                                 throw this exception
     * @throws OrbitFileParseException if anything goes wrong during
     *                                 parsing, this exception will be thrown
     */
    @Override
    public ConcreteCircularOrbit<Nuclear, Electron> parse(final File file)
            throws FileNotFoundException, OrbitFileParseException {

        Scanner scanner = new Scanner(file, "UTF-8");
        AtomStructure orbit = new AtomStructure();

        // -----------解析------------
        String extract1 = "\\s+::=\\s+";
        // extract line 1
        String line1 = scanner.nextLine();
        String label = line1.split(extract1)[1];

        orbit.addCenterObject(PhysicalObjectPool.getInstance().nuclear(label));

        String line2 = scanner.nextLine();

//        int count;
        try {
//            count = Integer.parseInt(line2.split(extract1)[1]);
            Integer.parseInt(line2.split(extract1)[1]);
        } catch (IndexOutOfBoundsException ex) {
            scanner.close();
            throw new MisArgumentException("缺失参数");
        }

        String line3 = scanner.nextLine();
        String text = line3.split(extract1)[1];

        final Pattern pattern = Pattern.compile("\\d+/\\d+;*");

        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            String[] pair = matcher.group().replaceAll(";", "").split("/");
            int level;
            int number;
            try {
                level = Integer.parseInt(pair[0]);
                number = Integer.parseInt(pair[1]);
            } catch (IndexOutOfBoundsException ex) {
                throw new OrbitFileParseException("参数数量错误");
            } catch (NumberFormatException ex) {
                throw new OrbitFileParseException("文件存在不合法参数");
            }
            if (number < 0) {
                throw new OrbitFileParseException("电子数量不可为负数");
            }

            // add new track
            try {
                orbit.addTrack(level, level);   // radius 此时没有使用
                // to generate random angle
                Random random = new Random(System.currentTimeMillis());
                for (int i = 0; i < number; i++) {
                    // loop adding objects

                    // random angle
                    double randomAngle =
                            Math.abs((random.nextInt() * 1767) % 360);
                    orbit.addToTrack(PhysicalObjectPool.getInstance().electron(),
                            level, 0.0, randomAngle);
                }
            } catch (NoSuchLevelOfTrackException e) {
                e.printStackTrace();
                throw new RuntimeException(file.getName() + "解析失败");
            } catch (TrackDidExistException ignored) {
            }
        }
        scanner.close();

//        log.info(file.getName() + "文件读取完成");

        return orbit;
    }
}

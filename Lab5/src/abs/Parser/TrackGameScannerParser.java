package abs.Parser;

import abs.PhysicalObjectPool;
import application.TrackGame.Athlete;
import application.TrackGame.TrackGame;
import MyException.CircularOrbitExceotion.OrbitFileParseException;
import abs.OrbitAble;
import abs.PhysicalObject;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public final class TrackGameScannerParser extends FileParser {

    /**
     * logger for this class.
     */
    private Logger log = Logger.getLogger(TrackGameScannerParser.class);

    // preparation variables
    /**
     * all read athletes.
     */
    private Set<Athlete> athletes = new HashSet<>();

    /**
     * 100/400/800.
     */
    private int game = 0;

    /**
     * number of track.
     */
    private int num = 0;

    @Override
    public OrbitAble<?, ?> parse(final File file)
            throws FileNotFoundException, OrbitFileParseException {

        Scanner scanner = new Scanner(file, "UTF-8");

        // -----------解析------------
        String extract1 = "\\s+::=\\s+";

        int currentLineNumber = 0;

        long time1 = System.currentTimeMillis();
        // 解析每一行
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            currentLineNumber++;
            if ("".equals(line.trim())) {
//                log.info("line" + currentLineNumber + "is empty, skipped.");
                continue;
            }

            String[] args = line.split(extract1);

            String arg0 = args[0].trim();

            String arg1;
            try {
                arg1 = args[1].trim();
            } catch (IndexOutOfBoundsException ex) {
                log.info("文件第" + currentLineNumber + "不合规范");
                throw new OrbitFileParseException("文件第"
                        + currentLineNumber + "行不合规范");
            }

            switch (arg0) {
                case "Athlete":
                    // 解析运动员信息
                    String info = arg1;

                    // 替换尖括号为空字符
                    info = info.replaceAll("[<>]", "");

                    String[] split = info.split(",");   // 按,分割每一条信息
                    // split.length 必须为5，否则gg
                    try {
                        Athlete athlete = PhysicalObjectPool.getInstance().athlete(split[0],
                                Integer.parseInt(split[1]),
                                split[2],
                                Integer.parseInt(split[3]),
                                Double.parseDouble(split[4]));
                        athletes.add(athlete);  // add this athletes to set
                    } catch (NumberFormatException ex) {
                        throw new OrbitFileParseException("文件第"
                                + currentLineNumber + "行参数错误");
                    }

                    break;
                case "Game":
                    game = Integer.parseInt(arg1);
                    break;
                case "NumOfTracks":
                    num = Integer.parseInt(arg1);
                    break;
                default:
                    throw new OrbitFileParseException("文件第"
                            + currentLineNumber + "行出现了未知标签");
            }
        }
        long time2 = System.currentTimeMillis();

        // end reading file
        scanner.close();

        log.info(file.getName() + "文件读取完毕，花费时间：" + (time2 - time1));

        return TrackGame.init(athletes, game, num);

    }
}

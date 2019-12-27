package abs.Parser;

import MyException.CircularOrbitExceotion.OrbitFileParseException;
import abs.OrbitAble;
import abs.PhysicalObject;
import abs.PhysicalObjectPool;
import application.TrackGame.Athlete;
import application.TrackGame.TrackGame;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("Duplicates")
public final class TrackGameReaderParser extends TrackGameParser {

    /**
     * logger.
     */
    private Logger log = Logger.getLogger(TrackGameReaderParser.class);

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
    public OrbitAble<?, ?> parse(File file) throws IOException, IllegalArgumentException, OrbitFileParseException {
        FileReader reader = new FileReader(file);
        StringBuilder sb = new StringBuilder();

        // -----------解析------------
        String extract1 = "\\s+::=\\s+";


        int ch;
        long time1 = System.currentTimeMillis();
        while ((ch = reader.read()) != -1) {
            sb.append((char) ch);
        }

        long time2 = System.currentTimeMillis();
        log.info(file.getName() + "文件读取完成，花费时间：" + (time2 - time1));

        // start parsing
        int currentLineNumber = 0;

        String[] lines = sb.toString().split("\n");

        for (String line : lines) {
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

        reader.close();

        long time3 = System.currentTimeMillis();
        log.info(file.getName() + "文件解析完成，花费时间：" + (time3 - time2));
        log.info(file.getName() + "文件读入并解析总共花费时间：" + (time3 - time1));

        return TrackGame.init(athletes, game, num);
    }
}

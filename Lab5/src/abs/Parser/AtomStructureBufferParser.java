package abs.Parser;

import MyException.CircularOrbitExceotion.NoSuchLevelOfTrackException;
import MyException.CircularOrbitExceotion.OrbitFileParseException;
import MyException.CircularOrbitExceotion.TrackDidExistException;
import abs.OrbitAble;
import abs.PhysicalObject;
import abs.PhysicalObjectPool;
import application.AtomStructure.AtomStructure;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class AtomStructureBufferParser extends AtomStructureParser {

    private InputStream input;

    private InputStreamReader reader;

    private BufferedReader buffered;

    /**
     * logger.
     */
    private Logger log = Logger.getLogger(AtomStructureBufferParser.class);

    @Override
    public OrbitAble<?, ?> parse(final File file) throws IOException, IllegalArgumentException, OrbitFileParseException {
        input = new FileInputStream(file);
        reader = new InputStreamReader(input);
        buffered = new BufferedReader(reader);

        AtomStructure orbit = new AtomStructure();

        // atom lines
        String[] lines = new String[3];

        int i = 0;
        while (i < 3) {
            String line = buffered.readLine();
            if ("".equals(line.trim())) {
                continue;
            }
            lines[i] = line;
            i++;
        }

        // got lines, then parse
        String extract1 = "\\s+::=\\s+";
        String center;

        int lineCount = 1;
        try {
            center = lines[0].split(extract1)[1];
            orbit.addCenterObject(PhysicalObjectPool.getInstance().nuclear(center));
            i++;
            Integer.parseInt(lines[1].split(extract1)[1]);
            i++;
            String text = lines[2].split(extract1)[1];

            Pattern pattern = Pattern.compile("\\d+/\\d+;*");
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
                    for (int j = 0; j < number; j++) {
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

        } catch (IndexOutOfBoundsException ex) {
            close();
            throw new OrbitFileParseException("文件第" + i + "行不合规范");
        }


        close();
        return orbit;
    }

    private void close() throws IOException {
        buffered.close();
        reader.close();
        input.close();
    }

}

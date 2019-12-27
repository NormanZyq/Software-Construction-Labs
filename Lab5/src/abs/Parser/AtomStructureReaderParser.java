package abs.Parser;

import MyException.CircularOrbitExceotion.MisArgumentException;
import MyException.CircularOrbitExceotion.NoSuchLevelOfTrackException;
import MyException.CircularOrbitExceotion.OrbitFileParseException;
import MyException.CircularOrbitExceotion.TrackDidExistException;
import abs.OrbitAble;
import abs.PhysicalObject;
import abs.PhysicalObjectPool;
import application.AtomStructure.AtomStructure;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class AtomStructureReaderParser extends AtomStructureParser {
    @Override
    public OrbitAble<?, ?> parse(File file) throws IOException, IllegalArgumentException, OrbitFileParseException {
        FileReader reader = new FileReader(file);
        StringBuilder sb = new StringBuilder();

        // read file to the sb
        int ch;
        while ((ch = reader.read()) != -1) {
            sb.append((char) ch);
        }

        // construct
        AtomStructure orbit = new AtomStructure();

        // parse
        String extract1 = "\\s+::=\\s+";

        // split
        String[] lines = sb.toString().split("\n");

        try {
            String line1 = lines[0];
            String label = line1.split(extract1)[1];
            // line1 ok
            orbit.addCenterObject(PhysicalObjectPool.getInstance().nuclear(label));
        } catch (IndexOutOfBoundsException ex) {
            reader.close();
            throw new MisArgumentException("第1行缺失参数");
        }

        try {
            // line 2
            String line2 = lines[1];
            String a = line2.split(extract1)[1];
        } catch (IndexOutOfBoundsException ex) {
            reader.close();
            throw new MisArgumentException("第2行缺失参数");
        }

        try {
            String line3 = lines[2];
            Pattern pattern = Pattern.compile("\\d+/\\d+;*");
            Matcher matcher = pattern.matcher(line3);
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

        } catch (IndexOutOfBoundsException ex) {
            reader.close();
            throw new MisArgumentException("第3行缺失参数");
        }


        reader.close();
        return orbit;
    }
}

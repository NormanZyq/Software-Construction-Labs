package abs.Parser;

import Application.AtomStructure.AtomStructure;
import Application.AtomStructure.Electron;
import Application.AtomStructure.Nuclear;
import MyException.CircularOrbitExceotion.MisArgumentException;
import MyException.CircularOrbitExceotion.OrbitFileParseException;
import MyException.CircularOrbitExceotion.TrackDidExistException;
import MyException.CircularOrbitExceotion.NoSuchLevelOfTrackException;
import abs.ConcreteCircularOrbit;
import abs.PhysicalObject;
import org.apache.log4j.Logger;
import sun.rmi.runtime.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AtomStructureParser extends FileParser {
	
	private Logger log = Logger.getLogger(AtomStructureParser.class);

    @Override
    public ConcreteCircularOrbit<Nuclear, Electron> parse(File file) throws FileNotFoundException, OrbitFileParseException {
    	
        int lineCount = 0;
        Scanner scanner = new Scanner(file, "UTF-8");
        AtomStructure orbit = new AtomStructure();

        // -----------解析------------
        String extract1 = "\\s+::=\\s+";
        // extract line 1
        String line1 = scanner.nextLine();
        String label = line1.split(extract1)[1];

        orbit.addCenterObject(PhysicalObject.newProton(label));

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
            if (number < 0) throw new OrbitFileParseException("电子数量不可为负数");

            // add new track
            try {
                orbit.addTrack(level, level);   // radius 此时没有使用
                Random random = new Random(System.currentTimeMillis());   // to generate random angle
                for (int i = 0; i < number; i++) {
                    // loop adding objects
                    double randomAngle = Math.abs((int) (random.nextInt() * 1767) % 360);   // random
                    orbit.addToTrack(PhysicalObject.newElectron(""), level, 0.0, randomAngle);
                }
            } catch (NoSuchLevelOfTrackException e) {
                e.printStackTrace();
                throw new RuntimeException(file.getName() + "解析失败");
            } catch (TrackDidExistException ignored) { }
        }
        scanner.close();
	
		log.info(file.getName() + "文件读取完成");
		
        return orbit;
    }
}

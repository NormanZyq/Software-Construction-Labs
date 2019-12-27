package abs.Parser;

import Application.AtomStructure.AtomStructure;
import Application.AtomStructure.Electron;
import Application.AtomStructure.Nuclear;
import abs.ConcreteCircularOrbit;
import abs.PhysicalObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AtomStructureParser extends FileParser {

    @Override
    public ConcreteCircularOrbit<Nuclear, Electron> parse(File file) throws FileNotFoundException {

        Scanner scanner = new Scanner(file);
        AtomStructure orbit = new AtomStructure();

        // -----------解析------------
        String extract1 = "\\s+::=\\s+";
        // extract line 1
        String line1 = scanner.nextLine();
        String label = line1.split(extract1)[1];

        orbit.addCenterObject(PhysicalObject.newProton(label));

        String line2 = scanner.nextLine();
        int count = Integer.parseInt(line2.split(extract1)[1]);

        String line3 = scanner.nextLine();
        String text = line3.split(extract1)[1];

        final Pattern pattern = Pattern.compile("\\d+/\\d+;*");

        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {

            String[] pair = matcher.group().replaceAll(";", "").split("/");

            int level = Integer.parseInt(pair[0]);

            int number = Integer.parseInt(pair[1]);

            // add new track
            orbit.addTrack(level, level);   // radius 此时没有使用

            Random random = new Random(System.currentTimeMillis());   // to generate random angle
            for (int i = 0; i < number; i++) {
                // loop adding objects
                double randomAngle = Math.abs((int)(random.nextDouble() * 1767) % 360);   // random
                orbit.addToTrack(PhysicalObject.newElectron(""), level, 0.0, randomAngle);
            }
        }
        scanner.close();
        return orbit;
    }
}

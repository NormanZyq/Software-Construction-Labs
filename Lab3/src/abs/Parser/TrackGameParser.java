package abs.Parser;

import Application.TrackGame.Athlete;
import Application.TrackGame.TrackGame;
import abs.ConcreteCircularOrbit;
import abs.OrbitAble;
import abs.PhysicalObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class TrackGameParser extends FileParser {


    // preparations
    private Set<Athlete> athletes = new HashSet<>();
    private int game = 0;
    private int num = 0;

    @Override
    public OrbitAble<?, ?> parse(File file) throws FileNotFoundException {

        Scanner scanner = new Scanner(file);

        // -----------解析------------
        String extract1 = "\\s+::=\\s+";

        // 解析每一行
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if ("".equals(line.trim())) continue;
            String[] args = line.split(extract1);

            String arg0 = args[0].trim();
            String arg1 = args[1].trim();

            switch (arg0) {
                case "Athlete":
                    // 解析运动员信息
                    String info = arg1;

                    // 替换尖括号为空字符
                    info = info.replaceAll("[<>]", "");

                    String[] split = info.split(",");   // 按,分割每一条信息
                    // split.length 必须为5，否则gg
                    Athlete athlete = PhysicalObject.newAthlete(split[0],
                            Integer.parseInt(split[1]),
                            split[2],
                            Integer.parseInt(split[3]),
                            Double.parseDouble(split[4]));
                    athletes.add(athlete);  // add this athletes to set
                    break;
                case "Game":
                    game = Integer.parseInt(arg1);
                    break;
                case "NumOfTracks":
                    num = Integer.parseInt(arg1);
                    break;
                default:
                    throw new RuntimeException("File contents illegal.");
            }
        }
        // end reading file
        scanner.close();
        TrackGame empty = TrackGame.init(athletes, game, num);
        return empty;

    }
}

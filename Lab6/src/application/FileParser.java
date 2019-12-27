package application;

import abs.Monkey;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileParser {

    private int n = 0;

    private int h = 0;

    private List<Monkey> monkeys = new LinkedList<>();

    public void parse(String filepath) throws FileNotFoundException {
        File file = new File(filepath);
        Scanner fileScanner = new Scanner(file);

        // arguments
        this.n = Integer.parseInt(fileScanner.nextLine().split("=")[1]);
        this.h = Integer.parseInt(fileScanner.nextLine().split("=")[1]);

        Matcher matcher;
        String extract = "\\d+,\\d+,[RL]->[RL],\\d+";   // regex
        Pattern pattern = Pattern.compile(extract);

        // scan all lines in the file
        while (fileScanner.hasNextLine()) {
            String text = fileScanner.nextLine();

            matcher = pattern.matcher(text);

            if (matcher.find()) {
                // split the line into many parts of monkey's arguments
                String[] split = matcher.group().split(",");

                // these are 5 arguments
                int group = Integer.parseInt(split[0]);     // group
                int id = Integer.parseInt(split[1]);        // id
                String direction = split[2];                // direction
                int speed = Integer.parseInt(split[3]);     // speed

                Monkey monkey = new Monkey(id, direction, speed, group);
                monkeys.add(monkey);
            }
        }
        fileScanner.close();
    }

    public int getN() {
        return n;
    }

    public int getH() {
        return h;
    }

    public List<Monkey> getMonkeys() {
        return monkeys;
    }
}

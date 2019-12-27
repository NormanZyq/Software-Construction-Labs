package abs.Parser;

import Application.SocialNetworkCircle.SocialNetworkCircle;
import Application.SocialNetworkCircle.SocialPerson;
import abs.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class SocialNetworkCircleParser extends FileParser {

    private SocialPerson center = null;

    private Map<String, SocialPerson> nameMap = new HashMap<>();
    private List<Relation<String, String>> relationsByName = new ArrayList<>();

    @Override
    public OrbitAble<?, ?> parse(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);

        String extract1 = "\\s+::=\\s+";

        // parse every line
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (!"".equals(line.trim())) {
                // not init line

                // get args
                String[] args = line.split(extract1);

                String arg0 = args[0];      // operation
                String arg1 = args[1];      // content

                // deal with arg1
                String info = arg1;
                info = info.replaceAll("[<>\\s]", "");    // replace < > and space characters
                String[] split = info.split(",");     // split with coma

                switch (arg0) {
                    case "SocialTie":
                        // new Relation
                        Relation<String, String> relation =
                                new ConcreteRelation<>(split[0], split[1], Double.parseDouble(split[2]));
                        relationsByName.add(relation);
//                        Relation<String, String> relation2 =
//                                new ConcreteRelation<>(split[1], split[0], Double.parseDouble(split[2]));
//                        relationsByName.add(relation2);
                        break;

                    case "Friend":
                        // now the split.length should be 3, or gg
                        // split[2] is a string with one char, charAt(0) must work
                        SocialPerson person = PhysicalObject.newPerson(split[0],
                                Integer.parseInt(split[1]),
                                split[2].charAt(0));
//                        friends.add(person);
                        nameMap.put(split[0], person);
                        break;

                    case "CentralUser":
                        // now the split.length should be 3, or gg
                        // split[2] is a string with one char, charAt(0) must work
                        center = PhysicalObject.newPerson(split[0],
                                Integer.parseInt(split[1]),
                                split[2].charAt(0));
                        nameMap.put(split[0], center);
                        break;

                    default:
                        throw new RuntimeException("SocialNetworkCircle file is illegal.");
                }
            }
        }


        if (center == null) throw new RuntimeException("SocialNetworkCircle file is illegal. No center person.");

        // construct circle
        SocialNetworkCircle circle = new SocialNetworkCircle(center);   // init
        String centerName = center.getName();       // center object name

        int i = 0;

        // add tracks and relations
        for (Relation<String, String> relation : relationsByName) {
            circle.addRelation(relation.getSource(), relation.getTarget(), relation.getWeight());
            i++;
        }

        // 删除多余轨道
//        for ()

        scanner.close();
        return circle;

    }

}

package abs.Parser;

import Application.SocialNetworkCircle.SocialNetworkCircle;
import Application.SocialNetworkCircle.SocialPerson;
import MyException.CircularOrbitExceotion.OrbitFileParseException;
import MyException.SocialPersonDoesNotExistExistException;
import abs.ConcreteRelation;
import abs.OrbitAble;
import abs.PhysicalObject;
import abs.Relation;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class SocialNetworkCircleParser extends FileParser {

    private Logger log = Logger.getLogger(SocialNetworkCircleParser.class);

    private SocialPerson center = null;

    private Map<String, SocialPerson> nameMap = new HashMap<>();
    private List<Relation<String, String>> relationsByName = new ArrayList<>();

    @Override
    public OrbitAble<?, ?> parse(File file) throws FileNotFoundException, OrbitFileParseException {
        Scanner scanner = new Scanner(file, "UTF-8");

        String extract1 = "\\s+::=\\s+";

        int currentLineNumber = 0;

        long time1 = System.currentTimeMillis();
        // parse every line
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            currentLineNumber++;
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

                try {
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
                            throw new OrbitFileParseException("文件第" + currentLineNumber + "行出现了未知标签");
                    }
                } catch (IndexOutOfBoundsException ex) {
                    throw new OrbitFileParseException(file.getName() + "第" + currentLineNumber + "参数缺失");
                }

            }
        }

        long time2 = System.currentTimeMillis();
        scanner.close();
        log.info(file.getName() + "文件读取完成，花费时间：" + (time2 - time1));

        if (center == null) throw new OrbitFileParseException("未从该文件中读取到中心人物");

        // construct circle
        SocialNetworkCircle circle = new SocialNetworkCircle(center);   // init

//		int i = 0;

        // add tracks and relations

        for (Relation<String, String> relation : relationsByName) {
            // i++;
            try {
                circle.addRelation(relation.getSource(), relation.getTarget(), relation.getWeight());
            } catch (SocialPersonDoesNotExistExistException e) {
                log.info("Parse中存在不合法的关系，已被忽略");
            }
        }

        long time3 = System.currentTimeMillis();
        log.info("轨道计算完成，花费时间：" + (time3 - time2));

        return circle;

    }

}

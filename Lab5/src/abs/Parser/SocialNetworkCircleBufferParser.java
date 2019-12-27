package abs.Parser;

import MyException.CircularOrbitExceotion.OrbitFileParseException;
import MyException.SocialNetworkCircleException.PersonNotExistException;
import abs.*;
import application.SocialNetworkCircle.SocialNetworkCircle;
import application.SocialNetworkCircle.SocialPerson;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class SocialNetworkCircleBufferParser extends SocialNetworkCircleParser {

    /**
     * logger.
     */
    private Logger log = Logger.getLogger(SocialNetworkCircleBufferParser.class);

    /**
     * center person.
     */
    private SocialPerson center = null;

    /**
     * name map used to store object with his name.
     */
    private Map<String, SocialPerson> nameMap = new HashMap<>();

    /**
     * relations by name.
     */
    private List<Relation<String, String>> relationsByName = new ArrayList<>();

    @Override
    public OrbitAble<?, ?> parse(File file) throws IOException, IllegalArgumentException, OrbitFileParseException {
        InputStream input = new FileInputStream(file);
        InputStreamReader reader = new InputStreamReader(input);
        BufferedReader buffered = new BufferedReader(reader);

        String extract1 = "\\s+::=\\s+";

        int currentLineNumber = 0;

        long time1 = System.currentTimeMillis();

        // start parsing
        while (true) {
            String line = buffered.readLine();
            if (line == null) {
                // EOF
                break;
            }
            if ("".equals(line.trim())) {
                continue;
            }
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
                            SocialPerson person = PhysicalObjectPool.getInstance().person(split[0],
                                    Integer.parseInt(split[1]),
                                    split[2].charAt(0));
//                        friends.add(person);
                            nameMap.put(split[0], person);
                            break;

                        case "CentralUser":
                            // now the split.length should be 3, or gg
                            center = PhysicalObjectPool.getInstance().person(split[0],
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
        buffered.close();
        reader.close();
        input.close();
        log.info(file.getName() + "文件读取并解析完成，花费时间：" + (time2 - time1));

        if (center == null) {
            throw new OrbitFileParseException("未从该文件中读取到中心人物");
        }

        // construct circle
        SocialNetworkCircle circle = new SocialNetworkCircle(center);   // init


        // add tracks and relations

        for (Relation<String, String> relation : relationsByName) {
            try {
                circle.addRelation(relation.getSource(), relation.getTarget(), relation.getWeight());
            } catch (PersonNotExistException e) {
                log.info("Parse中存在不合法的关系，已被忽略");
            }
        }

        long time3 = System.currentTimeMillis();
        log.info("轨道计算完成，花费时间：" + (time3 - time2));

        return circle;


    }
}

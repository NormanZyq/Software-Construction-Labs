package abs.Parser;

import MyException.CircularOrbitExceotion.OrbitFileParseException;
import MyException.SocialNetworkCircleException.PersonNotExistException;
import abs.*;
import application.SocialNetworkCircle.SocialNetworkCircle;
import application.SocialNetworkCircle.SocialPerson;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("Duplicates")
public final class SocialNetworkCircleReaderParser extends SocialNetworkCircleParser {

    /**
     * logger.
     */
    private Logger log = Logger.getLogger(SocialNetworkCircleReaderParser.class);

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
        FileReader reader = new FileReader(file);
        StringBuilder sb = new StringBuilder();

        // reg
        String extract1 = "\\s+::=\\s+";

        int ch;
        long time1 = System.currentTimeMillis();
        while ((ch = reader.read()) != -1) {
            sb.append((char) ch);
        }

        // end of reading file
        long time2 = System.currentTimeMillis();
        log.info("文件读取完毕，耗时" + (time2 - time1));

        // start parsing
        int currentLineNumber = 0;
        String[] lines = sb.toString().split("\\n");

        for (String line : lines) {
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
                            // split[2] is a string with one char, charAt(0) must work
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
        reader.close();

        long time3 = System.currentTimeMillis();

        log.info(file.getName() + "文件解析完成，耗时" + (time3 - time2));
        log.info(file.getName() + "文件读入并解析总共花费时间：" + (time3 - time1));

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

        long time4 = System.currentTimeMillis();
        log.info("轨道计算完成，花费时间：" + (time4 - time3));

        return circle;

    }
}


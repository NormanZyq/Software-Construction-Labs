package abs.Parser;

import abs.ConcreteCircularOrbit;
import abs.OrbitAble;

import java.io.File;
import java.io.FileNotFoundException;

public abstract class FileParser {

//    String defaultRegex = "[a-z]+\\s*::=\\s*";

    public static OrbitAble<?, ?> parseFile(FileParser parser, File file) throws FileNotFoundException {
        return parser.parse(file);
    }

    public abstract OrbitAble<?, ?> parse(File file) throws FileNotFoundException;

}

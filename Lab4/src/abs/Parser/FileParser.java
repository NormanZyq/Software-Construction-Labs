package abs.Parser;

import MyException.CircularOrbitExceotion.OrbitFileParseException;
import abs.OrbitAble;

import java.io.File;
import java.io.FileNotFoundException;

public abstract class FileParser {

    // String defaultRegex = "[a-z]+\\s*::=\\s*";
    
    public static OrbitAble<?, ?> parseFile(FileParser parser, File file) throws FileNotFoundException, IllegalArgumentException, OrbitFileParseException {
        return parser.parse(file);
    }

    public abstract OrbitAble<?, ?> parse(File file) throws FileNotFoundException, IllegalArgumentException, OrbitFileParseException;

}

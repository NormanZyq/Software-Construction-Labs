package abs.Parser;

import MyException.CircularOrbitExceotion.OrbitFileParseException;
import abs.OrbitAble;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public abstract class FileParser {
    /**
     * parse a file and create an orbit model.
     *
     * @param parser file parser
     * @param file   file you want to parse
     * @return orbit model
     * @throws FileNotFoundException    when file is not found,
     *                                  throw this exception
     * @throws IllegalArgumentException when there's an
     *                                  illegal argument in the file, it will be thrown
     * @throws OrbitFileParseException  other mistakes
     *                                  happened in a file will cause this exception
     */
    public static OrbitAble<?, ?> parseFile(final FileParser parser,
                                            final File file)
            throws FileNotFoundException,
            IllegalArgumentException,
            OrbitFileParseException {
        try {
            return parser.parse(file);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * parse file.
     *
     * @param file file you want to parse
     * @return an orbit model
     * @throws FileNotFoundException    when file is not found,
     *                                  throw this exception
     * @throws IllegalArgumentException when there's an
     *                                  illegal argument in the file, it will be thrown
     * @throws OrbitFileParseException  other mistakes
     *                                  happened in a file will cause this exception
     */
    public abstract OrbitAble<?, ?> parse(File file)
            throws IOException,
            IllegalArgumentException,
            OrbitFileParseException;

}

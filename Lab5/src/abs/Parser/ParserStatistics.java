package abs.Parser;

import MyException.CircularOrbitExceotion.OrbitFileParseException;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;

public class ParserStatistics {

    /**
     * logger.
     */
    private static Logger log = Logger.getLogger(ParserStatistics.class);

    public static void main(String[] args) throws FileNotFoundException, OrbitFileParseException {
        // parse atom
        log.info("------ Now parsing Atomic Structure ------");
        File atom = new File("src/txt/AtomicStructure_Medium.txt");
        // with scanner
        log.info("------ Using Scanner ------");
        long t1 = System.currentTimeMillis();
        FileParser.parseFile(new AtomStructureScannerParser(), atom);
        long t2 = System.currentTimeMillis();
        log.info("------ Delta of Atom with Scanner = " + (t2 - t1) + " ------");

        // with reader
        log.info("------ Using Reader ------");
        t1 = System.currentTimeMillis();
        FileParser.parseFile(new AtomStructureReaderParser(), atom);
        t2 = System.currentTimeMillis();
        log.info("------ Delta of Atom with Reader = " + (t2 - t1) + " ------");

        // with buffered reader
        log.info("------ Using Buffered Reader ------");
        t1 = System.currentTimeMillis();
        FileParser.parseFile(new AtomStructureBufferParser(), atom);
        t2 = System.currentTimeMillis();
        log.info("------ Delta of Atom with Buffered Reader = " + (t2 - t1) + " ------");

        // parse track game (extra large)
        log.info("------ Now parsing Track Game ------");
        File trackGame = new File("src/txt/TrackGame_ExtraLarge.txt");

        // with scanner
        log.info("------ Using Scanner ------");
        t1 = System.currentTimeMillis();
        FileParser.parseFile(new TrackGameScannerParser(), trackGame);
        t2 = System.currentTimeMillis();
        log.info("------ Delta of Track with Scanner = " + (t2 - t1) + " ------");

        // with reader
        log.info("------ Using Reader ------");
        t1 = System.currentTimeMillis();
        FileParser.parseFile(new TrackGameReaderParser(), trackGame);
        t2 = System.currentTimeMillis();
        log.info("------ Delta of Track with Reader = " + (t2 - t1) + " ------");

        // with buffered reader
        log.info("------ Using Buffered Reader ------");
        t1 = System.currentTimeMillis();
        FileParser.parseFile(new TrackGameBufferParser(), trackGame);
        t2 = System.currentTimeMillis();
        log.info("------ Delta of Track with Buffered Reader = " + (t2 - t1) + " ------");

        // parse social network circle (extra larger)
        log.info("------ Now parsing Social Network Circle ------");
        File social = new File("src/txt/SocialNetworkCircle_ExtraLarge.txt");
        // with reader
        log.info("------ Using Reader ------");
        t1 = System.currentTimeMillis();
        FileParser.parseFile(new SocialNetworkCircleReaderParser(), social);
        t2 = System.currentTimeMillis();
        log.info("------ Delta of Social with Reader = " + (t2 - t1) + " ------");


        // with scanner
        log.info("------ Using Scanner ------");
        t1 = System.currentTimeMillis();
        FileParser.parseFile(new SocialNetworkCircleScannerParser(), social);
        t2 = System.currentTimeMillis();
        log.info("------ Delta of Social with Scanner = " + (t2 - t1) + " ------");


        // with buffered reader
        log.info("------ Using Buffered Reader ------");
        t1 = System.currentTimeMillis();
        FileParser.parseFile(new SocialNetworkCircleBufferParser(), social);
        t2 = System.currentTimeMillis();
        log.info("------ Delta of Social with Buffered Reader = " + (t2 - t1) + " ------");

    }
}

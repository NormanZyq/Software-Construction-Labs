package abs.Writer;

import MyException.CircularOrbitExceotion.OrbitFileParseException;
import abs.Parser.AtomStructureBufferParser;
import abs.Parser.FileParser;
import abs.Parser.SocialNetworkCircleBufferParser;
import abs.Parser.TrackGameBufferParser;
import application.AtomStructure.AtomStructure;
import application.SocialNetworkCircle.SocialNetworkCircle;
import application.TrackGame.TrackGame;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class WriterStatistics {

    /**
     * logger.
     */
    private static Logger log = Logger.getLogger(WriterStatistics.class);

    public static void main(String[] args) throws IOException, OrbitFileParseException {
        AtomStructure a = (AtomStructure) FileParser.parseFile(new AtomStructureBufferParser(), new File("src/txt/AtomicStructure_Medium.txt"));
        TrackGame b = (TrackGame) FileParser.parseFile(new TrackGameBufferParser(), new File("src/txt/TrackGame_ExtraLarge.txt"));
        SocialNetworkCircle c = (SocialNetworkCircle) FileParser.parseFile(new SocialNetworkCircleBufferParser(), new File("src/txt/SocialNetworkCircle_ExtraLarge.txt"));

        // atom
        // use print writer
        OrbitFileWriter.writeBack(a, new UsePrintWriter());

        // use stream
        OrbitFileWriter.writeBack(a, new UseStream());

        // use buffer
        OrbitFileWriter.writeBack(a, new UseBuffer());

        // track game
        // print writer
        OrbitFileWriter.writeBack(b, new UsePrintWriter());

        OrbitFileWriter.writeBack(b, new UseStream());

        OrbitFileWriter.writeBack(b, new UseBuffer());

        // social
        // print writer
        OrbitFileWriter.writeBack(c, new UsePrintWriter());

        OrbitFileWriter.writeBack(c, new UseStream());

        OrbitFileWriter.writeBack(c, new UseBuffer());
    }

}


























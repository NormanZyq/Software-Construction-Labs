import MyException.CircularOrbitExceotion.OrbitFileParseException;
import abs.Parser.AtomStructureScannerParser;
import abs.Parser.FileParser;
import abs.Parser.SocialNetworkCircleBufferParser;
import abs.Parser.SocialNetworkCircleScannerParser;

import java.io.File;
import java.io.FileNotFoundException;

public class TestGarbageCollection {
    public static void main(String[] args) throws FileNotFoundException, OrbitFileParseException {
        File social = new File("src/txt/SocialNetworkCircle_ExtraLarge.txt");
        for (int i = 0; i < 10; i++) {
            FileParser.parseFile(new SocialNetworkCircleBufferParser(), social);
        }

        System.out.println("----------------------------------------------------------------------");

        for (int i = 0; i < 10; i++) {
            FileParser.parseFile(new SocialNetworkCircleScannerParser(), social);
        }


    }
}

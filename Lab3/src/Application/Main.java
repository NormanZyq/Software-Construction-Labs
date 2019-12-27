package Application;

import Application.SocialNetworkCircle.SocialNetworkCircle;
import Application.TrackGame.TrackArranger;
import Application.TrackGame.TrackGame;
import abs.CircularOrbit;
import abs.Parser.FileParser;
import abs.Parser.SocialNetworkCircleParser;
import abs.Parser.TrackGameParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


public class Main {
    public static void main(String[] args) throws FileNotFoundException {
//        AtomStructure a = (AtomStructure) CircularOrbit.parseFile(new AtomStructureParser(), "src/txt/AtomicStructure.txt");
//        AtomStructure b = (AtomStructure) CircularOrbit.parseFile(new AtomStructureParser(), "src/txt/AtomicStructure_Medium.txt");

        SocialNetworkCircle d = (SocialNetworkCircle) CircularOrbit.parseFile(new SocialNetworkCircleParser(), "src/txt/SocialNetworkCircle_Larger.txt");

//        TrackGame c = (TrackGame) FileParser.parseFile(new TrackGameParser(), new File("src/txt/AtomicStructure.txt"));
//        c.arrange(TrackArranger.randomArranger());
//        try {
//            c.writeArrangementToFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        c.arrange(TrackArranger.fasterLaterArranger());
//        try {
//            c.writeArrangementToFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}

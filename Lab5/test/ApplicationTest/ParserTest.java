package ApplicationTest;

import MyException.CircularOrbitExceotion.MisArgumentException;
import MyException.CircularOrbitExceotion.OrbitFileParseException;
import abs.Parser.AtomStructureScannerParser;
import abs.Parser.FileParser;
import abs.Parser.SocialNetworkCircleScannerParser;
import abs.Parser.TrackGameScannerParser;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;

public class ParserTest {
	
	@Test
	public void parseAtom() {
		try {
            FileParser.parseFile(new AtomStructureScannerParser(), new File("test/txt/AtomicStructure.txt"));
			assert true;
		} catch (FileNotFoundException | OrbitFileParseException e) {
			assert false;
		}
		
		try {
            FileParser.parseFile(new AtomStructureScannerParser(), new File("test/txt/AtomicStructureFail.txt"));
			assert false;
		} catch (FileNotFoundException e) {
			assert false;
		} catch (OrbitFileParseException | MisArgumentException e) {
			assert true;
		}
	}
	
	@Test
	public void parseSocialNetwork() {
		try {
            FileParser.parseFile(new SocialNetworkCircleScannerParser(), new File("src/txt/SocialNetworkCircle_ExtraLarge.txt"));
			assert true;
		} catch (FileNotFoundException | OrbitFileParseException e) {
			assert false;
		}
		
		try {
            FileParser.parseFile(new SocialNetworkCircleScannerParser(), new File("test/txt/SocialNetworkCircleFail1.txt"));
			assert false;
		} catch (FileNotFoundException e) {
			assert false;
		} catch (OrbitFileParseException e) {
			assert true;
		}
		
		try {
            FileParser.parseFile(new SocialNetworkCircleScannerParser(), new File("test/txt/SocialNetworkCircleFail1.txt"));
			assert false;
		} catch (FileNotFoundException e) {
			assert false;
		} catch (OrbitFileParseException e) {
			assert true;
		}
		
		try {
            FileParser.parseFile(new SocialNetworkCircleScannerParser(), new File("test/txt/SocialNetworkCircleFail2.txt"));
			assert false;
		} catch (FileNotFoundException e) {
			assert false;
		} catch (OrbitFileParseException e) {
			assert true;
		}
		
		
	}
	
	@Test
	public void parseTrackGame() {
		try {
            FileParser.parseFile(new TrackGameScannerParser(), new File("src/txt/TrackGame_ExtraLarge.txt"));
			assert true;
		} catch (FileNotFoundException | OrbitFileParseException e) {
			assert false;
		}
		
		try {
            FileParser.parseFile(new TrackGameScannerParser(), new File("test/txt/TrackGameFail1.txt"));
			assert false;
		} catch (FileNotFoundException e) {
			assert false;
		} catch (OrbitFileParseException e) {
			assert true;
		}
		
		try {
            FileParser.parseFile(new TrackGameScannerParser(), new File("test/txt/TrackGameFail2.txt"));
			assert false;
		} catch (FileNotFoundException | OrbitFileParseException e) {
			assert false;
		} catch (NumberFormatException e) {
			assert true;
		}
		
		try {
            FileParser.parseFile(new TrackGameScannerParser(), new File("test/txt/TrackGameFail3.txt"));
			assert false;
		} catch (FileNotFoundException e) {
			assert false;
		} catch (OrbitFileParseException e) {
			assert true;
		}
	}
	
}

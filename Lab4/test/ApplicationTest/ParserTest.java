package ApplicationTest;

import Application.AtomStructure.AtomStructure;
import MyException.CircularOrbitExceotion.MisArgumentException;
import MyException.CircularOrbitExceotion.OrbitFileParseException;
import abs.Parser.AtomStructureParser;
import abs.Parser.FileParser;
import abs.Parser.SocialNetworkCircleParser;
import abs.Parser.TrackGameParser;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.MissingFormatArgumentException;

import static org.junit.Assert.*;

public class ParserTest {
	
	@Test
	public void parseAtom() {
		try {
			FileParser.parseFile(new AtomStructureParser(), new File("test/txt/AtomicStructure.txt"));
			assert true;
		} catch (FileNotFoundException | OrbitFileParseException e) {
			assert false;
		}
		
		try {
			FileParser.parseFile(new AtomStructureParser(), new File("test/txt/AtomicStructureFail.txt"));
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
			FileParser.parseFile(new SocialNetworkCircleParser(), new File("test/txt/SocialNetworkCircle.txt"));
			assert true;
		} catch (FileNotFoundException | OrbitFileParseException e) {
			assert false;
		}
		
		try {
			FileParser.parseFile(new SocialNetworkCircleParser(), new File("test/txt/SocialNetworkCircleFail1.txt"));
			assert false;
		} catch (FileNotFoundException e) {
			assert false;
		} catch (OrbitFileParseException e) {
			assert true;
		}
		
		try {
			FileParser.parseFile(new SocialNetworkCircleParser(), new File("test/txt/SocialNetworkCircleFail1.txt"));
			assert false;
		} catch (FileNotFoundException e) {
			assert false;
		} catch (OrbitFileParseException e) {
			assert true;
		}
		
		try {
			FileParser.parseFile(new SocialNetworkCircleParser(), new File("test/txt/SocialNetworkCircleFail2.txt"));
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
			FileParser.parseFile(new TrackGameParser(), new File("test/txt/TrackGame.txt"));
			assert true;
		} catch (FileNotFoundException | OrbitFileParseException e) {
			assert false;
		}
		
		try {
			FileParser.parseFile(new TrackGameParser(), new File("test/txt/TrackGameFail1.txt"));
			assert false;
		} catch (FileNotFoundException e) {
			assert false;
		} catch (OrbitFileParseException e) {
			assert true;
		}
		
		try {
			FileParser.parseFile(new TrackGameParser(), new File("test/txt/TrackGameFail2.txt"));
			assert false;
		} catch (FileNotFoundException | OrbitFileParseException e) {
			assert false;
		} catch (NumberFormatException e) {
			assert true;
		}
		
		try {
			FileParser.parseFile(new TrackGameParser(), new File("test/txt/TrackGameFail3.txt"));
			assert false;
		} catch (FileNotFoundException e) {
			assert false;
		} catch (OrbitFileParseException e) {
			assert true;
		}
	}
	
}

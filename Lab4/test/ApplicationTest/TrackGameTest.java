package ApplicationTest;

import Application.TrackGame.Athlete;
import Application.TrackGame.TrackArranger;
import Application.TrackGame.TrackGame;
import MyException.CircularOrbitExceotion.NoSuchLevelOfTrackException;
import MyException.CircularOrbitExceotion.OrbitFileParseException;
import MyException.CircularOrbitExceotion.TrackDidExistException;
import MyException.TrackGameException.AthleteDidExistException;
import MyException.TrackGameException.AthleteDoesNotExistException;
import abs.Parser.FileParser;
import abs.Parser.TrackGameParser;
import abs.PhysicalObject;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class TrackGameTest {
    private TrackGame newInstance(int athleteCount) {
        return TrackGame.init(createAthleteSet(athleteCount), 100, 5);
    }

    private TrackGame empty() {
        return TrackGame.empty();
    }

    private Set<Athlete> createAthleteSet(int count) {
        Set<Athlete> athletes = new HashSet<>();
        for (int i = 0; i < count; i++) {
            Athlete a = new Athlete("Athlete" + i, i + 1, "Country" + i, 36, (double)(System.currentTimeMillis() % 13));
            athletes.add(a);
        }

        return athletes;
    }

    /**
     * when adding a new athlete, it should return true
     * when adding an athlete who's existed, returns false
     */
    @Test
    public void testAddAthlete() throws AthleteDidExistException {
        TrackGame trackGame = empty();
        Athlete a = PhysicalObject.newAthlete("A", 1, "USA", 36, 12);
        assertTrue(trackGame.addAthlete(a));
        assertTrue(trackGame.getAthletes().contains(a));
        try {
            trackGame.addAthlete(a);
            assert false;
        } catch (AthleteDidExistException ex) {
            assert true;
        }
    }

    /**
     * when removing an existed athlete, it should return true
     * when removing a new, returns false
     */
    @Test
    public void testRemoveAthlete() throws AthleteDidExistException {
        TrackGame trackGame = empty();
        Athlete a = PhysicalObject.newAthlete("A", 1, "USA", 36, 12);
        trackGame.addAthlete(a);
        try {
            trackGame.removeAthlete(a);
            assert true;
        } catch (AthleteDoesNotExistException e) {
            assert false;
        }
        try {
            trackGame.removeAthlete(a);
            assert false;
        } catch (AthleteDoesNotExistException e) {
            assert true;
        }
        assertFalse(trackGame.getAthletes().contains(a));
    }
    
    @Test
    public void testRemoveAthlete2() throws AthleteDidExistException {
        TrackGame trackGame = empty();
        Athlete a = PhysicalObject.newAthlete("A", 1, "USA", 36, 12);
        trackGame.addAthlete(a);
        try {
            trackGame.removeAthlete(a.getName());
            assert true;
        } catch (AthleteDoesNotExistException e) {
            assert false;
        }
        try {
            trackGame.removeAthlete(a.getName());
            assert false;
        } catch (AthleteDoesNotExistException e) {
            assert true;
        }
        assertFalse(trackGame.getAthletes().contains(a));
    }

    @Test
    public void testChangeAthletePosition() throws AthleteDidExistException {
        TrackGame trackGame = empty();
        Athlete a = PhysicalObject.newAthlete("A", 1, "USA", 36, 12);
        trackGame.addAthlete(a);
    }

    @Test
    public void testGetGame() {
        assertEquals(100, newInstance(1).getGame());
    }

    @Test
    public void testNumberOfTracks() {
        assertEquals(5, newInstance(1).getNumberOfTracks());
    }

    @Test
    public void testGetAthletes() {
        assertEquals(6, newInstance(6).getAthletes().size());
    }
    
    @Test
    public void testArrangement() throws AthleteDidExistException {
        TrackGame trackGame = newInstance(12);
        
        trackGame.arrange(TrackArranger.randomArranger());
    
        trackGame.arrange(TrackArranger.fasterLaterArranger());
    }
    
    @Test
    public void testExchangePosition() throws AthleteDidExistException {
        TrackGame trackGame = newInstance(2);
    
        trackGame.arrange(TrackArranger.randomArranger());
    
        assertTrue(trackGame.exchangePosition("Athlete0", "Athlete1"));
        assertFalse(trackGame.exchangePosition("Athlete2", "Athlete1"));
    
    }
    
    @Test
    public void testAddTrack() throws FileNotFoundException, OrbitFileParseException {
        TrackGame trackGame = (TrackGame) FileParser.parseFile(new TrackGameParser(), new File("test/txt/TrackGame.txt"));
        try {
            assertTrue(trackGame.addTrack(2, 2));
        } catch (TrackDidExistException e) {
            assert false;
        }
    }
    
    @Test
    public void testRemoveTrack() throws FileNotFoundException, OrbitFileParseException, TrackDidExistException {
        TrackGame trackGame = (TrackGame) FileParser.parseFile(new TrackGameParser(), new File("test/txt/TrackGame.txt"));
        trackGame.arrange(TrackArranger.randomArranger());
       
    }
    
    

}

package ApplicationTest;

import Application.TrackGame.Athlete;
import Application.TrackGame.TrackGame;
import abs.PhysicalObject;
import org.junit.Test;

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
    public void testAddAthlete() {
        TrackGame trackGame = empty();
        Athlete a = PhysicalObject.newAthlete("A", 1, "USA", 36, 12);
        assertTrue(trackGame.addAthlete(a));
        assertTrue(trackGame.getAthletes().contains(a));
        assertFalse(trackGame.addAthlete(a));
    }

    /**
     * when removing an existed athlete, it should return true
     * when removing a new, returns false
     */
    @Test
    public void testRemoveAthlete() {
        TrackGame trackGame = empty();
        Athlete a = PhysicalObject.newAthlete("A", 1, "USA", 36, 12);
        trackGame.addAthlete(a);
        assertTrue(trackGame.removeAthlete(a));
        assertFalse(trackGame.removeAthlete(a));
        assertFalse(trackGame.getAthletes().contains(a));
    }

    @Test
    public void testChangeAthletePosition() {
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


}

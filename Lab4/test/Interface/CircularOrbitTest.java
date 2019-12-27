package Interface;

import MyException.CircularOrbitExceotion.ObjectDoesNotExistException;
import MyException.CircularOrbitExceotion.TrackDidExistException;
import MyException.CircularOrbitExceotion.NoSuchLevelOfTrackException;
import abs.PhysicalObject;
import org.junit.Test;

/**
 * this class is a template for all tests about circular orbit model
 * @param <L>       center object's type
 * @param <E>       track object's type
 */
public interface CircularOrbitTest<L extends PhysicalObject, E extends PhysicalObject> extends OrbitableTest {
    
    @Test
    void testEmpty();
    
    /**
     * test for addCenterUser method
     */
    @Test
    void testAddCenterObject();

    /**
     * test get center object
     */
    @Test
    void testGetCenterObject();

    /**
     * test add a track to the orbit model
     */
    @Test
    void testAddTrack() throws TrackDidExistException;

    /**
     * test remove a track from the orbit model
     */
    @Test
    void testRemoveTrack() throws TrackDidExistException, NoSuchLevelOfTrackException;

    /**
     * test add a relation to the orbit model
     */
    @Test
    void testAddRelation() throws TrackDidExistException, NoSuchLevelOfTrackException, ObjectDoesNotExistException;

    /**
     * test add an object to a track in an orbit model
     */
    @Test
    void testAddToTrack() throws TrackDidExistException, NoSuchLevelOfTrackException;
    
}

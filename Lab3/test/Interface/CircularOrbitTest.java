package Interface;

import abs.CircularOrbit;
import abs.ConcreteCircularOrbit;
import abs.PhysicalObject;
import org.junit.Test;

import java.io.FileNotFoundException;

/**
 * this class is a template for all tests about circular orbit model
 * @param <L>       center object's type
 * @param <E>       track object's type
 */
public interface CircularOrbitTest<L extends PhysicalObject, E extends PhysicalObject> extends OrbitableTest {
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
    void testAddTrack();

    /**
     * test remove a track from the orbit model
     */
    @Test
    void testRemoveTrack();

    /**
     * test add a relation to the orbit model
     */
    @Test
    void testAddRelation();

    /**
     * test add an object to a track in an orbit model
     */
    @Test
    void testAddToTrack();

}

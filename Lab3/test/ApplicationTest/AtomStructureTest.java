package ApplicationTest;

import Application.AtomStructure.AtomStructure;
import Application.AtomStructure.Electron;
import Application.AtomStructure.Nuclear;
import Interface.CircularOrbitTest;
import abs.PhysicalObject;
import org.junit.Test;

import static org.junit.Assert.*;

public class AtomStructureTest implements CircularOrbitTest {
    private AtomStructure newInstance() {
        return AtomStructure.empty();
    }

    /**
     * test add a center object
     * 1. when now the old center object is null, return value should be null
     * 2. when the old center is not null, return the old center object
     */
    @Test
    @Override
    public void testAddCenterObject() {
        AtomStructure instance = newInstance();

        //1
        Nuclear nuclear = PhysicalObject.newProton("Cu");
        // add a new center object and the old is null, returns null
        assertNull(instance.addCenterObject(nuclear));

        // 2
        Nuclear nuclear2 = PhysicalObject.newProton("Cu");
        // add a new center but the old isn't null, returns old
        assertEquals(nuclear, instance.addCenterObject(nuclear2));
    }

    @Test
    @Override
    public void testGetCenterObject() {
        AtomStructure instance = newInstance();
        Nuclear nuclear = PhysicalObject.newProton("Cu");
        instance.addCenterObject(nuclear);

        assertEquals(instance.getCenterObject(), nuclear);
    }

    /**
     * this test contains 3 parts
     * 1. add new track should succeed
     * 2. add a track on same level should fail
     * 3. add an illegal track level or radius should fail
     */
    @Test
    @Override
    public void testAddTrack() {
        AtomStructure instance = newInstance();
        Nuclear nuclear = PhysicalObject.newProton("Cu");
        instance.addCenterObject(nuclear);

        assertTrue(instance.addTrack(1, 1));    // true --- success

        assertFalse(instance.addTrack(1, 2));

        assertFalse(instance.addTrack(-1, 0));

        assertEquals(1, instance.trackCount());

    }

    /**
     * test for removing a track
     */
    @Test
    @Override
    public void testRemoveTrack() {
        AtomStructure instance = newInstance();
        Nuclear nuclear = PhysicalObject.newProton("Cu");
        instance.addCenterObject(nuclear);

        instance.addTrack(1, 1);

        assertTrue(instance.removeTrack(1));

        assertFalse(instance.removeTrack(1));
    }

    /**
     * todo strategy
     *
     */
    @Test
    @Override
    public void testAddRelation() {
        AtomStructure instance = newInstance();
        Nuclear nuclear = PhysicalObject.newProton("Cu");
        instance.addCenterObject(nuclear);

        instance.addTrack(1, 1);
        instance.addTrack(2, 2);

        Electron e1 = PhysicalObject.newElectron("1");
        instance.addToTrack(e1, 1, 0.0, 0);
        Electron e2 = PhysicalObject.newElectron("2");
        instance.addToTrack(e2, 2, 0, 0);

        assertTrue(instance.addRelation(e1, e2, 100));



    }

    /**
     * this test contains two parts
     * 1. when adding an object to an existed track, ret should be true
     *      then on the particular track should contain that object
     * 2. when adding to an not-existed track, false
     *      then can't find the object on the particular level of track
     *
     */
    @Test
    @Override
    public void testAddToTrack() {
        AtomStructure instance = newInstance();
        Nuclear nuclear = PhysicalObject.newProton("Cu");
        instance.addCenterObject(nuclear);

        instance.addTrack(1, 1);

        Electron inModel = PhysicalObject.newElectron("1");
        assertTrue(instance.addToTrack(inModel, 1, 0.0, 0));
        assertTrue(instance.getObjectsByLevel(1).contains(inModel));

        Electron outModel = PhysicalObject.newElectron("2");
        assertFalse(instance.addToTrack(outModel, 2, 0.0, 0));
        assertFalse(instance.getObjectsByLevel(2).contains(outModel));
    }

}

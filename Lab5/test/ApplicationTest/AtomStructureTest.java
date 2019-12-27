package ApplicationTest;

import abs.PhysicalObjectPool;
import application.AtomStructure.AtomStructure;
import application.AtomStructure.Electron;
import application.AtomStructure.Nuclear;
import MyException.AtomStructureException.ElectronNotExistException;
import MyException.CircularOrbitExceotion.ObjectNotExistException;
import MyException.CircularOrbitExceotion.TrackDidExistException;
import MyException.CircularOrbitExceotion.NoSuchLevelOfTrackException;
import Interface.CircularOrbitTest;
import abs.PhysicalObject;
import org.junit.Test;

import static org.junit.Assert.*;

public class AtomStructureTest implements CircularOrbitTest {
	private AtomStructure newInstance() {
		return AtomStructure.empty();
	}
	
	@Test
	@Override
	public void testEmpty() {
		assertEquals(0, newInstance().trackCount());
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
		Nuclear nuclear = PhysicalObjectPool.getInstance().nuclear("Cu");
		// add a new center object and the old is null, returns null
		assertNull(instance.addCenterObject(nuclear));
		
		// 2
		Nuclear nuclear2 = PhysicalObjectPool.getInstance().nuclear("Cu");
		// add a new center but the old isn't null, returns old
		assertEquals(nuclear, instance.addCenterObject(nuclear2));
	}
	
	@Test
	@Override
	public void testGetCenterObject() {
		AtomStructure instance = newInstance();
		Nuclear nuclear = PhysicalObjectPool.getInstance().nuclear("Cu");
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
	public void testAddTrack() throws TrackDidExistException {
		AtomStructure instance = newInstance();
		Nuclear nuclear = PhysicalObjectPool.getInstance().nuclear("Cu");
		instance.addCenterObject(nuclear);
		
		assertTrue(instance.addTrack(1, 1));    // true --- success
		
		try {
			instance.addTrack(1, 2);
			assert false;
		} catch (TrackDidExistException ex) {
			assert true;
		}
		
		try {
			instance.addTrack(-1, 0);
			assert false;
		} catch (IllegalArgumentException ex) {
			assert true;
		}
		
		assertEquals(1, instance.trackCount());
	}
	
	/**
	 * test for removing a track
	 */
	@Test
	@Override
	public void testRemoveTrack() throws TrackDidExistException, NoSuchLevelOfTrackException {
		AtomStructure instance = newInstance();
		Nuclear nuclear = PhysicalObjectPool.getInstance().nuclear("Cu");
		instance.addCenterObject(nuclear);
		
		instance.addTrack(1, 1);
		
		assertTrue(instance.removeTrack(1));
	}
	
	/**
	 *
	 */
	@Test
	@Override
	public void testAddRelation() throws TrackDidExistException, NoSuchLevelOfTrackException {
		AtomStructure instance = newInstance();
		Nuclear nuclear = PhysicalObjectPool.getInstance().nuclear("Cu");
		instance.addCenterObject(nuclear);
		
		instance.addTrack(1, 1);
		instance.addTrack(2, 2);
		
		Electron e1 = PhysicalObjectPool.getInstance().electron();
		instance.addToTrack(e1, 1, 0.0, 0);
		Electron e2 = PhysicalObjectPool.getInstance().electron();
		instance.addToTrack(e2, 2, 0, 0);
		
		try {
			instance.addRelation(e1, e2, 100);
			assert true;
		} catch (ObjectNotExistException e) {
			assert false;
		}
		
	}
	
	/**
	 * this test contains two parts
	 * 1. when adding an object to an existed track, ret should be true
	 * then on the particular track should contain that object
	 * 2. when adding to an not-existed track, false
	 * then can't find the object on the particular level of track
	 */
	@Test
	@Override
	public void testAddToTrack() throws TrackDidExistException, NoSuchLevelOfTrackException {
		AtomStructure instance = newInstance();
		Nuclear nuclear = PhysicalObjectPool.getInstance().nuclear("Cu");
		instance.addCenterObject(nuclear);
		
		instance.addTrack(1, 1);
		
		Electron inModel = PhysicalObjectPool.getInstance().electron();
		assertTrue(instance.addToTrack(inModel, 1, 0.0, 0));
		assertTrue(instance.getObjectsByLevel(1).contains(inModel));
		
		Electron outModel = PhysicalObjectPool.getInstance().electron();
		try {
			instance.addToTrack(outModel, 2, 0.0, 0);
			assert false;
		} catch (NoSuchLevelOfTrackException ex) {
			assert true;
		}
		assertFalse(instance.getObjectsByLevel(2).contains(outModel));
	}
	
	@Test
	public void testTrans() throws TrackDidExistException, NoSuchLevelOfTrackException {
		AtomStructure instance = newInstance();
		Nuclear nuclear = PhysicalObjectPool.getInstance().nuclear("Cu");
		instance.addCenterObject(nuclear);
		
		instance.addTrack(1, 1);
		instance.addTrack(2, 2);
		instance.addTrack(3, 3);
		instance.addTrack(4, 4);
		instance.addTrack(5, 5);
		
		// to level 1
		for (int i = 0; i < 10; i++) {
			instance.addToTrack(PhysicalObjectPool.getInstance().electron(), 1, 1, 1);
		}
		
		// to level 2
		for (int i = 0; i < 5; i++) {
			instance.addToTrack(PhysicalObjectPool.getInstance().electron(), 2, 2, 2);
		}
		
		// to level 3
		for (int i = 0; i < 20; i++) {
			instance.addToTrack(PhysicalObjectPool.getInstance().electron(), 3, 3, 3);
		}
		
		
		// transit 1->4 with 5 electrons
		assertEquals(10, instance.getObjectsByLevel(1).size());
		instance.electronTransit(1, 4, 5);
		assertEquals(5, instance.getObjectsByLevel(4).size());
		assertEquals(5, instance.getObjectsByLevel(1).size());
		
		// transit 2->4 with 10 electrons (can only move 5)
		assertEquals(5, instance.getObjectsByLevel(1).size());
		instance.electronTransit(2, 4, 5);
		assertEquals(10, instance.getObjectsByLevel(4).size());
		assertEquals(0, instance.getObjectsByLevel(2).size());
	}
	
	@Test
	public void testRemoveFromTrack() throws NoSuchLevelOfTrackException, TrackDidExistException {
		AtomStructure instance = newInstance();
		Nuclear nuclear = PhysicalObjectPool.getInstance().nuclear("Cu");
		instance.addCenterObject(nuclear);
		
		instance.addTrack(1, 1);
		
		Electron electron = PhysicalObjectPool.getInstance().electron();
		instance.addToTrack(electron, 1, 0.0, 0);
		
		try {
			instance.removeElectronFromTrack(1);
			assert true;
		} catch (ElectronNotExistException e) {
			assert false;
		}
		
	}
}

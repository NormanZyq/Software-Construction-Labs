package ApplicationTest;

import Application.SocialNetworkCircle.SocialNetworkCircle;
import Application.SocialNetworkCircle.SocialPerson;
import Interface.OrbitableTest;
import MyException.CircularOrbitExceotion.ObjectDoesNotExistException;
import MyException.SocialPersonDoesNotExistExistException;
import abs.PhysicalObject;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class SocialNetworkCircleTest implements OrbitableTest {

    private SocialNetworkCircle newInstance() {
        return SocialNetworkCircle.empty();
    }
    
    @Test
    public void testConstructor() {
        try {
            new SocialNetworkCircle();
            SocialPerson p = PhysicalObject.newPerson("Bob", 19, 'M');
            new SocialNetworkCircle(p);
            assert true;
        } catch (Exception e) {
            assert false;
        }
        
    }

    /**
     * test add a center user
     * 1. when now the old center user is null, return value should be null
     * 2. when the old center is not null, return the old center user
     */
    @Test
    public void testAddCenterObject() {
        SocialNetworkCircle circle = newInstance();

        // 1
        SocialPerson p1 = PhysicalObject.newPerson("Bob", 20, 'M');
        assertTrue(circle.addCenterUser(p1));

        // 2
        SocialPerson p2 = PhysicalObject.newPerson("Mary", 21, 'F');
        assertFalse(circle.addCenterUser(p2));

    }

    /**
     * test add center user method
     */
    @Test
    public void testGetCenterObject() {
        SocialNetworkCircle instance = newInstance();
        SocialPerson p = PhysicalObject.newPerson("Bob", 18, 'M');
        instance.addCenterUser(p);
        assertEquals(p, instance.getCentralUser());

    }

    /**
     *
     * there are two get relation methods,
     * 1. get relation by person object
     * 2. get relation by person's name
     */
    @Test
    public void testAddRelation() throws ObjectDoesNotExistException {
        SocialNetworkCircle circle = newInstance();

//        SocialPerson center = PhysicalObject.newPerson("Center", 100, 'M');


        SocialPerson p1 = PhysicalObject.newPerson("Bob", 18, 'M');
        circle.addCenterUser(p1);
        SocialPerson p2 = PhysicalObject.newPerson("Mary", 21, 'F');
        SocialPerson p3 = PhysicalObject.newPerson("Tom", 33, 'M');
        SocialPerson p4 = PhysicalObject.newPerson("Pat", 44, 'F');

        circle.addRelation("Bob", "Mary", 0.9);
        circle.addRelation("Bob", "Tom", 0.8);
        circle.addRelation("Bob", "Pat", 0.7);

        // answer map
        Map<String, Double> answerMap = new HashMap<>();
        answerMap.put(p2.getName(), 0.9);
        answerMap.put(p3.getName(), 0.8);
        answerMap.put(p4.getName(), 0.7);

        // actual map
        Map<String, Double> relationMapByObject = circle.getRelationByPerson(p1);
        for (String name : relationMapByObject.keySet()) {
            assertEquals(answerMap.get(name), relationMapByObject.get(name), 1e-4);
        }

        Map<String, Double> relationMapByName = circle.getRelationByPerson(p1.getName());
        for (String name : relationMapByObject.keySet()) {
            assertEquals(answerMap.get(name), relationMapByName.get(name), 1e-4);
        }
    }
    
    @Test
    public void testAddRelation2() throws ObjectDoesNotExistException {
        SocialNetworkCircle circle = newInstance();
        
        SocialPerson center = PhysicalObject.newPerson("Center", 100, 'M');
        
        circle.addCenterUser(center);
        
        SocialPerson p1 = PhysicalObject.newPerson("Bob", 18, 'M');
        SocialPerson p2 = PhysicalObject.newPerson("Mary", 21, 'F');
        SocialPerson p3 = PhysicalObject.newPerson("Tom", 33, 'M');
        SocialPerson p4 = PhysicalObject.newPerson("Pat", 44, 'F');
    
        circle.addRelation(center, p1, 0.99);
        circle.addRelation(p1, p2, 0.9);
        circle.addRelation(p1, p3, 0.8);
        circle.addRelation(p1, p4, 0.7);
        
        // answer map
        Map<String, Double> answerMap = new HashMap<>();
        answerMap.put(p2.getName(), 0.9);
        answerMap.put(p3.getName(), 0.8);
        answerMap.put(p4.getName(), 0.7);
        
        // actual map
        Map<String, Double> relationMapByObject = circle.getRelationByPerson(p1);
        for (String name : relationMapByObject.keySet()) {
            assertEquals(answerMap.get(name), relationMapByObject.get(name), 1e-4);
        }
        
        Map<String, Double> relationMapByName = circle.getRelationByPerson(p1.getName());
        for (String name : relationMapByObject.keySet()) {
            assertEquals(answerMap.get(name), relationMapByName.get(name), 1e-4);
        }
    }
    
    @Test
    public void testAddRelation3() throws ObjectDoesNotExistException {
        SocialNetworkCircle circle = newInstance();
        
        SocialPerson center = PhysicalObject.newPerson("Center", 100, 'M');
        
        circle.addCenterUser(center);
        
        SocialPerson p1 = PhysicalObject.newPerson("Bob", 18, 'M');
        SocialPerson p2 = PhysicalObject.newPerson("Mary", 21, 'F');
        SocialPerson p3 = PhysicalObject.newPerson("Tom", 33, 'M');
        SocialPerson p4 = PhysicalObject.newPerson("Pat", 44, 'F');
        
        circle.addRelation(center, p1, 0.99);
        circle.addRelation(p1, p2, 0.9);
        circle.addRelation(p2, p3, 0.8);
        circle.addRelation(p3, p4, 0.7);
    
        assertEquals(4, circle.calculateLevel(p4.getName()));
        
        circle.addRelation(center, p4, 0.6);
        
        assertEquals(1, circle.calculateLevel(p4.getName()));
    }

    @Test
    public void testAddToTrack() {
        assertTrue(true);
    }

    @Test
    public void testParseFile() throws FileNotFoundException {
        assertTrue(true);
    }
    
    @Test
    public void testRemoveRelation() throws ObjectDoesNotExistException {
        SocialNetworkCircle circle = newInstance();
    
        SocialPerson center = PhysicalObject.newPerson("Center", 100, 'M');
    
        circle.addCenterUser(center);
    
        SocialPerson p1 = PhysicalObject.newPerson("Bob", 18, 'M');
        SocialPerson p2 = PhysicalObject.newPerson("Mary", 21, 'F');
        SocialPerson p3 = PhysicalObject.newPerson("Tom", 33, 'M');
        SocialPerson p4 = PhysicalObject.newPerson("Pat", 44, 'F');
    
        circle.addRelation(center, p1, 0.99);
        circle.addRelation(p1, p2, 0.9);
        circle.addRelation(p1, p3, 0.8);
        circle.addRelation(p1, p4, 0.7);
    
        try {
            circle.removeRelation("Bob", "Pat");
            assert true;
        } catch (SocialPersonDoesNotExistExistException e) {
            assert false;
        }
    
        try {
            circle.removeRelation("Bob2", "Pat");
            assert false;
        } catch (SocialPersonDoesNotExistExistException e) {
            assert true;
        }
    
        try {
            circle.removeRelation("Bob", "Pat2");
            assert false;
        } catch (SocialPersonDoesNotExistExistException e) {
            assert true;
        }
        
        // answer map
        Map<String, Double> answerMap = new HashMap<>();
        answerMap.put(p2.getName(), 0.9);
        answerMap.put(p3.getName(), 0.8);
    
        // actual map
        Map<String, Double> relationMapByObject = circle.getRelationByPerson(p1);
        for (String name : relationMapByObject.keySet()) {
            assertEquals(answerMap.get(name), relationMapByObject.get(name), 1e-4);
        }
    
        Map<String, Double> relationMapByName = circle.getRelationByPerson(p1.getName());
        for (String name : relationMapByObject.keySet()) {
            assertEquals(answerMap.get(name), relationMapByName.get(name), 1e-4);
        }
    }
    
    /**
     * 本测试主要测试移除关系后的位置变动
     */
    @Test
    public void testRemoveRelation2() throws ObjectDoesNotExistException {
        SocialNetworkCircle circle = newInstance();
        
        SocialPerson center = PhysicalObject.newPerson("Center", 100, 'M');
        
        circle.addCenterUser(center);
        
        SocialPerson p1 = PhysicalObject.newPerson("Bob", 18, 'M');
        SocialPerson p2 = PhysicalObject.newPerson("Mary", 21, 'F');
        
        circle.addRelation(center, p1, 0.99);
        circle.addRelation(p1, p2, 0.9);
        circle.addRelation(center, p2, 0.6);
    
        assertEquals(1, circle.calculateLevel(p2.getName()));
    
        circle.removeRelation(center.getName(), p2.getName());
        
        assertEquals(2, circle.calculateLevel(p2.getName()));
    }
    
    @Test
    public void testRemovePerson() throws ObjectDoesNotExistException {
        SocialNetworkCircle circle = newInstance();
    
        SocialPerson center = PhysicalObject.newPerson("Center", 100, 'M');
    
        circle.addCenterUser(center);
    
        SocialPerson p1 = PhysicalObject.newPerson("Bob", 18, 'M');
        SocialPerson p2 = PhysicalObject.newPerson("Mary", 21, 'F');
        SocialPerson p3 = PhysicalObject.newPerson("Tom", 33, 'M');
        SocialPerson p4 = PhysicalObject.newPerson("Pat", 44, 'F');
    
        circle.addRelation(center, p1, 0.99);
        circle.addRelation(p1, p2, 0.9);
        circle.addRelation(p1, p3, 0.8);
        circle.addRelation(p3, p4, 0.7);
    
        try {
            circle.removePerson("Bob");
            assert true;
        } catch (SocialPersonDoesNotExistExistException e) {
            e.printStackTrace();
            assert false;
        }
        
        try {
            circle.removePerson("Bob");
            assert false;
        } catch (SocialPersonDoesNotExistExistException e) {
            assert true;
        }
    }
    
    @Test
    public void testDistance() throws ObjectDoesNotExistException {
        SocialNetworkCircle circle = newInstance();
    
        SocialPerson center = PhysicalObject.newPerson("Center", 100, 'M');
    
        circle.addCenterUser(center);
    
        SocialPerson p1 = PhysicalObject.newPerson("Bob", 18, 'M');
        SocialPerson p2 = PhysicalObject.newPerson("Mary", 21, 'F');
        SocialPerson p3 = PhysicalObject.newPerson("Tom", 33, 'M');
        SocialPerson p4 = PhysicalObject.newPerson("Pat", 44, 'F');
    
        circle.addRelation(center, p1, 0.99);
        circle.addRelation(p1, p2, 0.9);
        circle.addRelation(p1, p3, 0.8);
        circle.addRelation(p3, p4, 0.7);
    
        assertEquals(1, circle.getLogicalDistance("Center", "Bob"));
    
        assertEquals(2, circle.getLogicalDistance("Bob", "Pat"));
    
        assertEquals(-1, circle.getLogicalDistance("Mary", "Pat"));
        
    }
    
    @Test
    public void testCalculateDiffusion() throws ObjectDoesNotExistException {
        SocialNetworkCircle circle = newInstance();
    
        SocialPerson center = PhysicalObject.newPerson("Center", 100, 'M');
    
        circle.addCenterUser(center);
    
        SocialPerson p1 = PhysicalObject.newPerson("Bob", 18, 'M');
        SocialPerson p2 = PhysicalObject.newPerson("Mary", 21, 'F');
        SocialPerson p3 = PhysicalObject.newPerson("Tom", 33, 'M');
        SocialPerson p4 = PhysicalObject.newPerson("Pat", 44, 'F');
    
        circle.addRelation(center, p1, 0.99);
        circle.addRelation(p1, p2, 0.9);
        circle.addRelation(p1, p3, 0.8);
        circle.addRelation(p3, p4, 0.7);
        
        assertEquals(1.7, circle.calculateDiffusion("Bob"), 1e-4);
    }
    
    @Test
    public void testSources() throws ObjectDoesNotExistException {
        SocialNetworkCircle circle = newInstance();
    
        SocialPerson center = PhysicalObject.newPerson("Center", 100, 'M');
    
        circle.addCenterUser(center);
    
        SocialPerson p1 = PhysicalObject.newPerson("Bob", 18, 'M');
        SocialPerson p2 = PhysicalObject.newPerson("Mary", 21, 'F');
        SocialPerson p3 = PhysicalObject.newPerson("Tom", 33, 'M');
        SocialPerson p4 = PhysicalObject.newPerson("Pat", 44, 'F');
    
        circle.addRelation(center, p1, 0.99);
        circle.addRelation(center, p2, 0.99);
        circle.addRelation(center, p3, 0.99);
        circle.addRelation(p1, p4, 0.99);
        circle.addRelation(p1, p3, 0.9);
        circle.addRelation(p2, p3, 0.8);
        circle.addRelation(p4, p3, 0.7);
        
        
    
        // answer map
        Map<String, Double> answerMap = new HashMap<>();
        answerMap.put(center.getName(), 0.99);
        answerMap.put(p1.getName(), 0.9);
        answerMap.put(p2.getName(), 0.8);
        answerMap.put(p4.getName(), 0.7);
    
        // actual map
        Map<String, Double> actualMap = circle.sources(p3.getName());
        
        assertEquals(answerMap, actualMap);
    }

}

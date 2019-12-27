package ApplicationTest;

import Application.SocialNetworkCircle.SocialNetworkCircle;
import Application.SocialNetworkCircle.SocialPerson;
import Interface.OrbitableTest;
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
    public void testAddRelation() {
        SocialNetworkCircle circle = newInstance();

        SocialPerson center = PhysicalObject.newPerson("Center", 100, 'M');

        circle.addCenterUser(center);

        SocialPerson p1 = PhysicalObject.newPerson("Bob", 18, 'M');
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
    public void testAddToTrack() {
        assertTrue(true);
    }

    @Test
    public void testParseFile() throws FileNotFoundException {
//        SocialNetworkCircle circle = (SocialNetworkCircle) FileParser.parseFile(new SocialNetworkCircleParser(), new File("src/txt/SocialNetworkCircle.txt"));
//        assertEquals("TommyWong", circle.getCentralUser().getName());
//
//
//        Map<String, Double> tommyWongAnswerMap = new HashMap<>();
//        tommyWongAnswerMap.put("LisaWong", 0.98);
//        tommyWongAnswerMap.put("TomWong", 0.2);
//        tommyWongAnswerMap.put("DavidChen", 0.342);
//
//        // tommy wong actual map
//        Map<String, Double> relationMap = circle.getRelationByPerson("TommyWong");
//        for (String name : relationMap.keySet()) {
//            assertEquals(tommyWongAnswerMap.get(name), relationMap.get(name), 1e-4);
//        }
//
//        Map<String, Double> tomWongAnswerMap = new HashMap<>();
//        tomWongAnswerMap.put("FrankLee", 0.71);
//
//        Map<String, Double> relationMap2 = circle.getRelationByPerson("TomWong");
//        for (String name : relationMap2.keySet()) {
//            assertEquals(tomWongAnswerMap.get(name), relationMap2.get(name), 1e-4);
//        }

    }


}

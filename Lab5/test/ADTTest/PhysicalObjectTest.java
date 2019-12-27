package ADTTest;

import abs.PhysicalObjectPool;
import application.SocialNetworkCircle.SocialPerson;
import application.TrackGame.Athlete;
import abs.PhysicalObject;
import org.junit.Test;

import static org.junit.Assert.*;

public class PhysicalObjectTest {
	@Test
	public void testNewProton() {
		assertEquals("Cu", PhysicalObjectPool.getInstance().nuclear("Cu").getName());
	}
	
	@Test
	public void testNewElectron() {
		assertEquals("", PhysicalObjectPool.getInstance().electron().getName());
	}
	
	@Test
	public void testNewAthlete() {
		Athlete a = PhysicalObjectPool.getInstance().athlete("Bob", 5, "USA", 18, 9.99);
		assertEquals("Bob", a.getName());
		assertEquals(5, a.getNumber());
		assertEquals("USA", a.getCountry());
		assertEquals(18, a.getAge());
		assertEquals(9.99, a.getBestScoreInYear(), 1e-4);
		
		assertEquals(a, new Athlete(a));
	}
	
	@Test
	public void testNewPerson() {
		SocialPerson p = PhysicalObjectPool.getInstance().person("Bob", 18, 'M');
		assertEquals("Bob", p.getName());
		assertEquals(18, p.getAge());
		assertEquals('M', p.getSex());
	}
}

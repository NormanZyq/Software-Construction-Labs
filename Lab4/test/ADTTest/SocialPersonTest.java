package ADTTest;

import Application.SocialNetworkCircle.SocialPerson;
import org.junit.Test;

import static org.junit.Assert.*;


public class SocialPersonTest {
	@Test
	public void testConstructor() {
		SocialPerson p = new SocialPerson("Bob", 18, 'M');
		assertEquals("Bob", p.getName());
		assertEquals(18, p.getAge());
		assertEquals('M', p.getSex());
	}
	
	@Test
	public void testCopy() {
		SocialPerson origin = new SocialPerson("Bob", 18, 'M');
		
		SocialPerson copy = new SocialPerson(origin);
		
		assertEquals("Bob", copy.getName());
		assertEquals(18, copy.getAge());
		assertEquals('M', copy.getSex());
	}
}

package util;

import org.junit.Test;

import static org.junit.Assert.*;

public class MonkeyGeneratorTest {

    @Test
    public void createMonkeys() {
        MonkeyGenerator generator = new MonkeyGenerator(5,
                20, 1, 3, 8, 20);
        assertEquals(20, generator.createMonkeys().size());
    }
}
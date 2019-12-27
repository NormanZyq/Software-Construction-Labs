package abs;

import abs.selector.CanAccessFirstLadderSelector;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class LadderTest {
    private int id = 1;

    private String direction = Monkey.Direction.L2R;

    private int speed = 5;

    private int group = 0;

    private Monkey defaultMonkey() {
        return new Monkey(id, direction, speed, group);
    }

    private Ladder ladder() {
        return new Ladder(20);
    }

    @Test
    public void notOccupied() {
        assertTrue(ladder().notOccupied());
    }

    @Test
    public void isLevelOccupied() {
        Monkey m = defaultMonkey();
        List<Ladder> ladders = new ArrayList<>();
        Ladder ladder = ladder();
        ladders.add(ladder);
        m.selectLadder(CanAccessFirstLadderSelector.getSelector(), ladders);

        assertTrue(ladder.isLevelOccupied(1));

        m.moveAhead();

        assertTrue(ladder.isLevelOccupied(6));
    }

    @Test
    public void getDirection() {
        Monkey m = defaultMonkey();
        List<Ladder> ladders = new ArrayList<>();
        Ladder ladder = ladder();
        ladders.add(ladder);
        m.selectLadder(CanAccessFirstLadderSelector.getSelector(), ladders);
        assertEquals(Monkey.Direction.L2R, ladder.getDirection());
    }

    @Test
    public void hasMonkey() {
        Monkey m = defaultMonkey();
        List<Ladder> ladders = new ArrayList<>();
        Ladder ladder = ladder();

        assertFalse(ladder.hasMonkey());

        ladders.add(ladder);
        m.selectLadder(CanAccessFirstLadderSelector.getSelector(), ladders);

        assertTrue(ladder.hasMonkey());
    }

    @Test
    public void barCount() {
        Ladder ladder = ladder();

        assertEquals(20, ladder.barCount());
    }

}
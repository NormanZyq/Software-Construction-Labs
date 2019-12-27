package abs;

import abs.selector.CanAccessFirstLadderSelector;
import abs.selector.LadderSelector;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class MonkeyTest {
    private int id = 1;

    private String direction = Monkey.Direction.L2R;

    private int speed = 5;

    private int group = 0;

    private Monkey defaultMonkey() {
        return new Monkey(id, direction, speed, group);
    }

    private List<Ladder> fiveLadders() {
        List<Ladder> ladders = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ladders.add(new Ladder(20));
        }
        return ladders;
    }

    private LadderSelector selector = CanAccessFirstLadderSelector.getSelector();


    @Test
    public void getDirection() {
        Monkey m = defaultMonkey();
        assertEquals(direction, m.getDirection());
    }

    @Test
    public void getSpeed() {
        Monkey m = defaultMonkey();
        assertEquals(speed, m.getSpeed());
    }

    @Test
    public void selectLadder() {
        Monkey m = defaultMonkey();
        List<Ladder> ladders = fiveLadders();
        m.selectLadder(selector, ladders);
        assertEquals(1, m.getCurrentLevel());
    }

    @Test
    public void moveAhead() {
        Monkey m = defaultMonkey();
        List<Ladder> ladders = fiveLadders();
        m.selectLadder(selector, ladders);
        m.moveAhead();
        assertEquals(6, m.getCurrentLevel());
    }

    @Test
    public void isOK() {
        Monkey m = defaultMonkey();
        List<Ladder> ladders = fiveLadders();
        m.selectLadder(selector, ladders);
        m.moveAhead();
        m.moveAhead();
        m.moveAhead();
        m.moveAhead();
        assertTrue(m.isOK());
    }

    @Test
    public void getCurrentLevel() {
        Monkey m = defaultMonkey();
        List<Ladder> ladders = fiveLadders();
        m.selectLadder(selector, ladders);
        assertEquals(1, m.getCurrentLevel());
    }

    @Test
    public void isOnLadder() {
        Monkey m = defaultMonkey();
        List<Ladder> ladders = fiveLadders();
        m.selectLadder(selector, ladders);
        assertTrue(m.isOnLadder());
    }

    @Test
    public void getGroup() {
        Monkey m = defaultMonkey();
        assertEquals(group, m.getGroup());
    }

    @Test
    public void getAliveTime() {
        final Monkey[] m = {null};

        new Thread(() -> m[0] = defaultMonkey()).start();
        try {
            Thread.sleep(1000);
            assertEquals(1, m[0].getAliveTime());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
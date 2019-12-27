package abs;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * this class stands for the ladder on the river.
 */
public final class Ladder {

    private final Map<Integer, Boolean> occupiedMap = new ConcurrentHashMap<>();

    private final Map<Monkey, Integer> positionMap = new ConcurrentHashMap<>();

    private final int barCount;

    private boolean occupied = false;

    private String direction = null;

    public Ladder(int barCount) {
        this.barCount = barCount;
        for (int i = 1; i <= barCount; i++) {
            occupiedMap.put(i, false);
        }
    }

    public synchronized boolean notOccupied() {
        return !occupied;
    }

    public synchronized boolean isLevelOccupied(int level) {
        return occupiedMap.get(level);
    }

    public String getDirection() {
        return direction;
    }

    public synchronized void putMonkey(Monkey monkey) {
        this.occupied = true;
        if (direction == null) {
            this.direction = monkey.getDirection();
        }
        assert !occupiedMap.get(1);
        assert positionMap.get(monkey) == null;

        occupiedMap.put(1, true);
        positionMap.put(monkey, 1);
    }

    public synchronized void makeUnoccupied() {
        this.occupied = false;
        this.direction = null;
        for (int i = 1; i <= barCount; i++) {
            occupiedMap.put(i, false);
        }
        positionMap.clear();
    }

    public synchronized int moveAhead(Monkey monkey) {
        Integer level = positionMap.get(monkey);
        int speed = monkey.getSpeed();
        if (level == null) {
            level = 1;
            positionMap.put(monkey, 1);
            occupiedMap.put(1, true);
        } else {
            // level not null
            // needs to judge if there is a monkey on the way to level+=speed
            int goal = level + speed;
            if (goal > barCount) {
                goal = barCount;
            }
            // 测试最远可以移动到哪里
            while (!canAcross(level, goal)) {
                goal--;
                if (level == goal) {
                    // 如果没有向前移动，需要返回一个标记
                    return -1;
                }
            }
            // mark occupied map in original level false
            occupiedMap.put(level, false);
            // new level, also the target level
            level = goal;
            // put new things to the maps
            positionMap.put(monkey, goal);
            occupiedMap.put(goal, true);
            synchronized (this) {
                if (goal == barCount) {
                    positionMap.remove(monkey);
                    occupiedMap.put(goal, false);
                    if (!hasMonkey()) {
                        this.makeUnoccupied();
                    }
                }
            }


        }
        return level;
    }

    public synchronized void leaveLadder(Monkey monkey) {
        int level = positionMap.get(monkey);
        if (level == barCount) {
            System.out.println(positionMap.remove(monkey));
            ;
            occupiedMap.put(level, false);
            if (!hasMonkey()) {
                this.makeUnoccupied();
                System.out.println("梯子" + this + "现在已经没有被占用");
            }
        } else {
            throw new RuntimeException("发生错误：猴子未达终点，不能离开梯子！");
        }
    }

    public synchronized boolean hasMonkey() {
        return !positionMap.isEmpty();
    }

    /**
     * judge if there is a monkey on the way from start to end.
     *
     * @param start start level
     * @param end   goal level
     * @return true iff no any other monkeys on the way from start to end
     */
    private synchronized boolean canAcross(int start, int end) {
        synchronized (occupiedMap) {
            int index = start + 1;
            while (index <= end) {
                if (isLevelOccupied(index)) {
                    return false;
                }
                index++;
            }
            return true;
        }

    }

    /**
     * get bar count.
     *
     * @return bat count in this ladder
     */
    public int barCount() {
        return barCount;
    }

    /**
     * get speed of this ladder.
     * <p>
     * notice: how to define speed of the ladder?
     * the minimum speed of the monkeys on this ladder,
     * that's the speed of the ladder
     *
     * @return the speed of the ladder
     */
    public synchronized int getSpeed() {
        Set<Monkey> monkeysOnLadder = positionMap.keySet();
        int speed = Integer.MAX_VALUE;
        for (Monkey monkey : monkeysOnLadder) {
            if (monkey.getSpeed() < speed) {
                speed = monkey.getSpeed();
            }
        }
        return speed;
    }
}

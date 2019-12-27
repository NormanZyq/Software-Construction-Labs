package abs;

import abs.selector.LadderSelector;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * class for Monkey,
 * this class represents the Monkey on the ladder,
 * and they are going to simulate passing river.
 */
public final class Monkey {

    // AF
    // this class represents the monkey wanting to
    // pass the river
    //
    // RI
    // selectedLadder == null and isOnLadder == true
    // don't happen at the same time


    /**
     * logger.
     */
    private final Logger log = Logger.getLogger(Monkey.class);

    /**
     * id of the monkey.
     */
    private final int id;

    /**
     * direction.
     */
    private final String direction;

    /**
     * speed.
     */
    private final int speed;

    /**
     * group of the monkey.
     * it determines when the monkey is going to be create and pass river.
     */
    private final int group;

    /**
     * a boolean represents if the monkey is on the ladder.
     */
    private boolean isOnLadder = false;

    /**
     * the ladder this monkey selected.
     */
    private Ladder selectedLadder = null;

    /**
     * the monkey's current level.
     */
    private int currentLevel = 0;

    /**
     * how long does this monkey alive.
     */
    private long alive = 0;

    /**
     * the timer used to trigger timing task.
     */
    private Timer timer = null;

    /**
     * a simple monkey constructor.
     *
     * @param id        id
     * @param direction direction
     * @param speed     speed
     */
    public Monkey(int id, String direction, int speed) {
        this.id = id;
        this.direction = direction;
        this.speed = speed;
        this.group = 0;
        countAliveTime();
    }

    /**
     * full monkey constructor.
     *
     * @param id        id
     * @param direction direction
     * @param speed     speed
     * @param group     group
     */
    public Monkey(int id, String direction, int speed, int group) {
        this.id = id;
        this.direction = direction;
        this.speed = speed;
        this.group = group;
        countAliveTime();
    }

    /**
     * start counting alive time.
     */
    private void countAliveTime() {
        if (timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    alive++;
                }
            }, 0, 1000);
        }
    }

    /**
     * get direction.
     *
     * @return direction of this monkey
     */
    public String getDirection() {
        return direction;
    }

    /**
     * get speed.
     *
     * @return speed of this monkey
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * select ladder by the selector you passed in.
     *
     * @param selector what kind of selecting method you want to use
     * @param ladders  ladder list, monkey will select a ladder from this list
     */
    public synchronized void selectLadder(LadderSelector selector,
                                          List<Ladder> ladders) {
        if (selectedLadder == null) {
            // only when no selected ladder then select a ladder
            Ladder ladder = LadderSelector.selectLadder(this,
                    selector,
                    ladders);
            if (ladder == null) {
//                log.info(this.toString()
//                        + "未选择到梯子，在岸边等待，因当前没有符合要求的梯子");
            } else {
                this.isOnLadder = true;
                this.selectedLadder = ladder;
                this.currentLevel = 1;
                ladder.putMonkey(this);
                log.info(this.toString() + "选择了 " + ladder + " 这把梯子");
            }
        } else {
            throw new RuntimeException(this.toString()
                    + "已经在梯子上了，不能反复选择！");
        }
    }

    /**
     * move ahead on a ladder.
     *
     * @return after moving, the new level this monkey is on
     */
    public synchronized int moveAhead() {
        // not ok, move ahead
        int returnedLevel = selectedLadder.moveAhead(this);
        if (returnedLevel == -1) {
//            log.info(this.toString() + "在梯子 " + selectedLadder + " 的第 "
//                    + this.currentLevel + " 根杆上，这一秒钟未移动");
            return this.currentLevel;
        } else {
            log.info(this.toString() + "在梯子 " + selectedLadder + " 上，方向是"
                    + direction + "，这一秒钟移动到了第 "
                    + returnedLevel + " 根杆上");
            this.currentLevel = returnedLevel;
//            if (this.currentLevel == this.selectedLadder.barCount()) {
//                isOK = true;
//            }
            return returnedLevel;
        }

    }

    /**
     * if the monkey has got to the opposite of the river,
     * calling the method will cause these effects:
     * 1. stop the timer which used to calculate the alive time of the monkey
     * 2. print a log
     * 3. return true
     *
     * @return true iff the monkey has got to the opposite of the river
     */
    public synchronized boolean isOK() {
        if (this.currentLevel == this.selectedLadder.barCount()) {
            timer.cancel();
            log.info(this.toString() + "到达河对岸，总共耗时 " + alive + " 秒");
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Monkey{" +
                "id=" + id +
                ", direction='" + direction + '\'' +
                ", speed=" + speed +
                '}' + "已经出生了 " + alive + " 秒。";
    }

    /**
     * get current level of the monkey.
     *
     * @return level
     */
    public int getCurrentLevel() {
        return currentLevel;
    }

    /**
     * recognize if the monkey is on a ladder.
     *
     * @return true iff this monkey is on the ladder
     */
    public synchronized boolean isOnLadder() {
        return isOnLadder;
    }

    /**
     * get the group.
     *
     * @return group
     */
    public int getGroup() {
        return group;
    }

    /**
     * get alive time.
     *
     * @return alive time
     */
    public long getAliveTime() {
        return alive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Monkey monkey = (Monkey) o;

        if (id != monkey.id) return false;
        if (speed != monkey.speed) return false;
        if (group != monkey.group) return false;
        if (isOnLadder != monkey.isOnLadder) return false;
        if (currentLevel != monkey.currentLevel) return false;
        if (!direction.equals(monkey.direction)) return false;
        return selectedLadder != null ? selectedLadder.equals(monkey.selectedLadder) : monkey.selectedLadder == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + direction.hashCode();
        result = 31 * result + speed;
        result = 31 * result + group;
        result = 31 * result + (selectedLadder != null ? selectedLadder.hashCode() : 0);
        return result;
    }

    /**
     * this inner static class stands
     * for the direction of the monkey and ladder.
     * implemented by static final String
     */
    public static class Direction {
        public static final String L2R = "L->R";

        public static final String R2L = "R->L";
    }

}

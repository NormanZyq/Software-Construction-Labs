package util;

import abs.Monkey;

import java.util.Random;

public class MonkeyUtils {

    /**
     * counter for monkeys.
     */
    private int allMonkeys = 0;

    private Random random = new Random(System.currentTimeMillis());

    private int maxSpeed;

    public MonkeyUtils(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    /**
     * create a monkey with particular arguments.
     *
     * @param id        id
     * @param direction L2R or R2L. @requires use from Monkey.Direction
     * @param speed     speed
     * @return a monkey object
     */
    @Deprecated
    public Monkey newMonkeyWithId(int id, String direction, int speed) {
        return new Monkey(id, direction, speed);
    }

    /**
     * create a monkey with particular arguments.
     *
     * @param direction L2R or R2L. @requires use from Monkey.Direction
     * @param speed     speed
     * @return a monkey object
     */
    @Deprecated
    public Monkey newMonkey(String direction, int speed) {
        allMonkeys++;
        return new Monkey(allMonkeys, direction, speed);
    }

    /**
     * random create a monkey with random speed and direction.
     *
     * @return random monkey object
     */
    public Monkey randomNewMonkey() {
        allMonkeys++;
        int speed = (Math.abs(random.nextInt()) % maxSpeed) + 1;
        int direction = Math.abs(random.nextInt());
        if (direction % 2 == 0) {
            return new Monkey(allMonkeys, Monkey.Direction.L2R, speed);
        } else {
            return new Monkey(allMonkeys, Monkey.Direction.R2L, speed);
        }
    }

    public Monkey randomNewMonkeyWithGroup(int group) {
        allMonkeys++;
        int speed = (Math.abs(random.nextInt()) % maxSpeed) + 1;
        int direction = Math.abs(random.nextInt());
        if (direction % 2 == 0) {
            return new Monkey(allMonkeys, Monkey.Direction.L2R, speed, group);
        } else {
            return new Monkey(allMonkeys, Monkey.Direction.R2L, speed, group);
        }
    }
}

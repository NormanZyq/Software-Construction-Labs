package util;

import abs.Monkey;

import java.util.LinkedList;
import java.util.List;

public class MonkeyGenerator {

    private final int n;

    private final int h;

    private final int t;

    private final int k;

    private final int maxV;

    private final int monkeyNumber;

    public MonkeyGenerator(int n, int h, int t, int k, int maxV, int number) {
        this.n = n;
        this.h = h;
        this.t = t;
        this.k = k;
        this.maxV = maxV;
        monkeyNumber = number;
    }

    public List<Monkey> createMonkeys() {
        MonkeyUtils utils = new MonkeyUtils(maxV);

        List<Monkey> monkeys = new LinkedList<>();      // return list

        int generateCount = k;  // how many monkeys to generate in this loop

        int monkeyLast = monkeyNumber;

        int group = 0;

        do {
            // if last monkey number is < k,
            // which means cannot create enough monkeys, so create the last
            if (monkeyLast < k) {
                generateCount = monkeyLast;
            }

            for (int i = 0; i < generateCount; i++) {
                // generate a new monkey
                Monkey monkey = utils.randomNewMonkeyWithGroup(group);

                monkeys.add(monkey);
            }

            monkeyLast -= generateCount;
            group += t;
        } while (monkeyLast > 0);

        return monkeys;
    }
}

package application;

import abs.Ladder;
import abs.Monkey;
import abs.selector.LadderSelector;
import org.apache.log4j.Logger;
import util.MonkeyGenerator;

import java.io.FileNotFoundException;
import java.util.*;

public final class MonkeyPasser {

    private final Logger log = Logger.getLogger(MonkeyPasser.class);

    private final int n;

    private final int h;

    private final List<Monkey> waitingList;

    private final List<Ladder> ladders = new ArrayList<>();

    private final List<Monkey> passedList = new ArrayList<>();

    private int count;

    private int passed = 0;

    private int secs = 0;

    private Timer countTimeUsed = new Timer();

    private MonkeyPasser(String filepath) throws FileNotFoundException {
        // new file parser
        FileParser parser = new FileParser();
        // parse file
        parser.parse(filepath);

        h = parser.getH();
        n = parser.getN();

        waitingList = parser.getMonkeys();
        count = waitingList.size();

        // use argument n to create n times' ladders,
        // each ladder has h times' bars on it.
        for (int i = 0; i < n; i++) {
            ladders.add(new Ladder(h));
        }
    }

    private MonkeyPasser() {
        throw new RuntimeException();
    }

    private MonkeyPasser(int n, int h, List<Monkey> waitingList) {
        this.n = n;
        this.h = h;
        this.waitingList = waitingList;
        count = waitingList.size();
        // use argument n to create n times' ladders,
        // each ladder has h times' bars on it.
        for (int i = 0; i < n; i++) {
            ladders.add(new Ladder(h));
        }
    }

    public static MonkeyPasser passerByMonkeyFile(String filepath) throws FileNotFoundException {
        return new MonkeyPasser(filepath);
    }

    public static MonkeyPasser randomMonkeyPasser(int n, int h, int t, int N, int k, int MV) {
        MonkeyGenerator generator =
                new MonkeyGenerator(n, h, t, k, MV, N);

        return new MonkeyPasser(n, h, generator.createMonkeys());
    }

    public static MonkeyPasser randomMonkeyPasser() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("n = ");
        int n = scanner.nextInt();      // n个梯子

        System.out.print("h = ");
        int h = scanner.nextInt();      // 梯子有多少个杠杠

        System.out.print("t = ");
        int t = scanner.nextInt();  // 每隔t秒产生一次

        System.out.print("N = ");
        int monkeyNumber = scanner.nextInt();   // 猴子总数

        System.out.print("k = ");
        final int k = scanner.nextInt();  // 一次产生2个，也就是每2个一组

        System.out.print("MV = ");
        int maxV = scanner.nextInt();

        MonkeyGenerator generator =
                new MonkeyGenerator(n, h, t, k, maxV, monkeyNumber);

        return new MonkeyPasser(n, h, generator.createMonkeys());
    }

    /**
     * start passing river.
     *
     * @param selector which ladder selecting method you want to use
     */
    public void passRiver(LadderSelector selector) {
        this.passedList.clear();

        // create a timer task to count running secs
        countTimeUsed.schedule(new TimerTask() {
            @Override
            public void run() {
                secs++;
            }
        }, 0, 1000);


        int last = 0;   // last group
        List<Monkey> monkeyGroup = new LinkedList<>();

        // a mark, created monkeys' threads or not
        // 用途：此标记是用来判断当前
        // 遍历到的猴子有没有触发创建猴子线程
        boolean created;

        for (Monkey waitingMonkey : waitingList) {
            int group = waitingMonkey.getGroup();

            // sleep secs
            int sleep = group - last;

            // if sleep secs > 0,
            // then start the former monkeys
            // in the list as new threads separately
            // then sleep for sleep secs
            if (sleep == 0) {
                // if sleep == 0,
                // then create a new monkey object and add to list

                // 标记此次循环到的猴子仍和上一个是同一组
                created = false;
            } else if (sleep > 0) {
                for (Monkey monkeyInGroup : monkeyGroup) {
                    createMonkeyThread(monkeyInGroup, selector);
                }
                // clear the list which started passing just now
                monkeyGroup.clear();

                // sleep current thread to wait for next group of monkeys
                try {
                    Thread.sleep(1000 * sleep);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // store the new group as the "last" group
                last = group;

                // 标记此次循环创建了猴子的列表
                created = true;
            } else {
                // if sleep < 0, gg
                throw new RuntimeException("猴子的组数未按顺序排列导致出错");
            }

            // 把当前这个也要添加进，不然每组都少一只猴子
            monkeyGroup.add(waitingMonkey);
        }
        // if not empty, means there's only
        // one group so not triggered creating threads
        if (!monkeyGroup.isEmpty()) {
            // if 中的条件含义是：
            // 如果待创建线程的猴子列表不为空，并且还没有创建过，此时要创建
            for (Monkey monkeyInGroup : monkeyGroup) {
                createMonkeyThread(monkeyInGroup, selector);
            }
            // clear the list which started passing just now
            monkeyGroup.clear();
        }
    }

    private void createMonkeyThread(Monkey monkeyInGroup
            , LadderSelector selector) {
//        long secs1 = System.currentTimeMillis() / 1000;
        Thread monkeyPassRiver = new Thread(new Runnable() {
            @Override
            public void run() {
                Timer timer1 = new Timer();
                timer1.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (monkeyInGroup.isOnLadder()) {
                            if (monkeyInGroup.isOK()) {
                                // 达到终点，终止线程或timer
                                passed++;
                                timer1.cancel();
                                if (isAllPassed()) {
                                    countTimeUsed.cancel();
                                    log.info("所有猴子都已到达对岸！");
//                                    long secs2 =
//                                            System.currentTimeMillis() / 1000;
//                                    long secs = secs2 - secs1;
                                    log.info("总共耗时 "
                                            + secs + " 秒");
                                    log.info("吞吐率为 " + throughput());
                                    log.info("公平率为 " + fairness());
//                                    System.exit(0);
                                }
                            } else {
                                monkeyInGroup.moveAhead();
                            }
                        } else {
                            synchronized (ladders) {
                                monkeyInGroup.selectLadder(selector,
                                        ladders);
                            }
                        }
                    }
                }, 0, 1000);
            }
        });
        monkeyPassRiver.start();
    }

    public double throughput() {
        return (double) count / secs;
    }

    private boolean isAllPassed() {
        return passed == count;
    }

    public double fairness() {
        int index = 1;
        long cnm = combination(count, 2);
        long timeSum = 0;
        for (Monkey monkey : waitingList) {

            long createTime = monkey.getGroup() * 1000;
            long endTime = monkey.getAliveTime() + createTime;

            Iterator<Monkey> iterator = waitingList.iterator();

            for (int i = 0; i < index; i++) {
                iterator.next();
            }
            while (iterator.hasNext()) {
                Monkey monkey1 = iterator.next();
                long createTime1 = monkey1.getGroup();
                long endTime1 = monkey1.getAliveTime() + createTime1;

                if ((createTime1 - createTime) * (endTime1 - endTime) >= 0) {
                    timeSum += 1;
                } else {
                    timeSum += -1;
                }

            }

            index++;
        }

        return timeSum / (double) cnm;
    }

    public boolean isLevelOfLadderOccupied(int ladderNumber, int barLevel) {
        Ladder ladder = ladders.get(ladderNumber);
        return ladder.isLevelOccupied(barLevel);
    }

    public String getLadderDirectionByNumber(int ladderNumber) {
        return ladders.get(ladderNumber).getDirection();
    }

    public int getN() {
        return n;
    }

    public int getH() {
        return h;
    }

    public int getMonkeyLast() {
        return count - passed;
    }

    public int getPassedMonkey() {
        return passed;
    }

    public int getTimeUsed() {
        return secs;
    }

    /**
     * 排列数，A(n,m) = n*(n-1)*(n-2)……*(n-m+1); .
     *
     * @param n 排列数的下面的数
     * @param m 排列数的上面的数
     */
    private int arrangement(int n, int m) {
        if (n < m) {
            System.out.println("n>m，输入错误");
        }
        int mul = 1;
        for (int i = n - m + 1; i <= n; i++) {
            mul *= i;
        }
        return mul;
    }

    /**
     * 组合数,C(n,m) = A(n,m)/m! .
     *
     * @param n 组合数的下面的数
     * @param m 组合数的上面的数
     */
    private int combination(int n, int m) {
        if (n < m) {
            System.out.println("n>m，输入错误");
        }
        int factM = 1;
        for (int i = 1; i <= m; i++) {
            factM *= i;
        }
        int a = arrangement(n, m);
        return a / factM;
    }

}

package abs.selector;

import abs.Ladder;
import abs.Monkey;

import java.util.List;
import java.util.Random;

/**
 * randomly select ladder
 */
public class RandomSelector implements LadderSelector {

    /**
     * instance.
     */
    private static final LadderSelector random = new RandomSelector();

    /**
     * 选择方向相同的梯子的选择器.
     */
    private final LadderSelector accessFirst =
            CanAccessFirstLadderSelector.getSelector();

    /**
     * 选择空梯子的选择器.
     */
    private final LadderSelector empty = EmptyLadderSelector.getSelector();

    /**
     * to generate random number.
     */
    private final Random r = new Random(System.currentTimeMillis());

    /**
     * make constructor private to implement singleton.
     */
    private RandomSelector() {
    }

    /**
     * get the instance.
     *
     * @return instance
     */
    public static LadderSelector getSelector() {
        return random;
    }

    /**
     * select ladder.
     * use random number to decide which selection method to use
     * <p>
     * notice: there are two methods:
     * 1. select the ladder which has the same direction as the monkey's
     * 2. select the ladder which is empty
     *
     * @param monkey  which monkey wants to select a ladder?
     * @param ladders ladder list waiting for selection
     * @return the selected ladder object
     */
    @Override
    public Ladder select(Monkey monkey, List<Ladder> ladders) {
        if (Math.abs(r.nextInt()) % 2 == 0) {
            return accessFirst.select(monkey, ladders);
        } else {
            return empty.select(monkey, ladders);
        }
    }
}

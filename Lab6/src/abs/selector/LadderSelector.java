package abs.selector;

import abs.Ladder;
import abs.Monkey;

import java.util.List;

/**
 * this interface specifies what a ladder selector should be like.
 * they should have method select(), then pass in a monkey and ladder list
 */
public interface LadderSelector {

    /**
     * strategy pattern, select ladder by the selector you passed in.
     *
     * @param monkey   monkey which is going to select ladder
     * @param selector selector
     * @param ladders  ladder list
     * @return selected ladder
     */
    static Ladder selectLadder(Monkey monkey, LadderSelector selector,
                               List<Ladder> ladders) {
        return selector.select(monkey, ladders);
    }

    /**
     * select ladder.
     *
     * notice: this method is not thread-safe,
     * so when you call it, you'd to use synchronized(ladders) {}
     * to wrap this method
     *
     * @param monkey  monkey which is going to select ladder
     * @param ladders ladder list
     * @return selected ladder
     */
    Ladder select(Monkey monkey, List<Ladder> ladders);

}

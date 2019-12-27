package abs.selector;

import abs.Ladder;
import abs.Monkey;

import java.util.List;

/**
 * select the ladder when the direction
 * is the same and the first bar is available.
 */
public class CanAccessFirstLadderSelector implements LadderSelector {

    private static final LadderSelector canAccess = new CanAccessFirstLadderSelector();

    private CanAccessFirstLadderSelector() {
    }

    public static LadderSelector getSelector() {
        return canAccess;
    }

    @Override
    public synchronized Ladder select(Monkey monkey, List<Ladder> ladders) {
        String direction = monkey.getDirection();
        for (Ladder ladder : ladders) {
            if (ladder.notOccupied()) {
                return ladder;
            } else if (direction.equals(ladder.getDirection())
                    && !ladder.isLevelOccupied(1)) {
                return ladder;
            }
        }
        return null;
    }
}

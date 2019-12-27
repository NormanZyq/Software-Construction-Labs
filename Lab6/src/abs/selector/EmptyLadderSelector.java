package abs.selector;

import abs.Ladder;
import abs.Monkey;

import java.util.List;

/**
 * select the empty ladder.
 * i.e. when all ladders have monkey,
 * this selector will return null continuously.
 */
public final class EmptyLadderSelector implements LadderSelector {

    private static final LadderSelector maxSpeed = new EmptyLadderSelector();

    private EmptyLadderSelector() {
    }

    public static LadderSelector getSelector() {
        return maxSpeed;
    }

    @Override
    public Ladder select(Monkey monkey, List<Ladder> ladders) {
        for (Ladder ladder : ladders) {
            if (ladder.notOccupied()) {
                return ladder;
            }
        }
        return null;
    }
//
//    /**
//     * @param direction
//     * @param ladders
//     * @return ladder which has max speed, or null when:
//     * 1. all ladders' direction are not compatible with the required direction
//     * 2. all ladders' speed are 0
//     */
//    private Ladder getMaxSpeedLadder(String direction, List<Ladder> ladders) {
//        int maxSpeed = 0;
//        Ladder maxSpeedLadder = null;
//        for (Ladder ladder : ladders) {
//            if (!direction.equals(ladder.getDirection())) {
//                continue;
//            }
//            if (ladder.getSpeed() > maxSpeed) {
////                System.out.println("got the speed" + ladder.getSpeed());
//                maxSpeed = ladder.getSpeed();
//                maxSpeedLadder = ladder;
//            }
//        }
//        return maxSpeedLadder;
//    }
}

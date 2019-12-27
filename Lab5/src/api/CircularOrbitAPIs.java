package api;

import abs.center.CenterObject;
import abs.CircularOrbit;
import abs.PhysicalObject;
import abs.Position;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// todo java doc
public final class CircularOrbitAPIs<L extends CenterObject,
        E extends PhysicalObject> {

    public double getObjectDistributionEntropy(final CircularOrbit c) {
        int trackSize = c.trackCount();

        int sum = 0;
        for (int i = 0; i < trackSize; i++) {
            sum += c.getObjectsByLevel(i + 1).size();
        }

        double average = sum / (double) trackSize;

        double entropy = 0;
        for (int i = 0; i < trackSize; i++) {
            entropy += c.getObjectsByLevel(i).size() - average;
        }
        return entropy;

    }

    public int getLogicalDistance(final CircularOrbit c,
                                  final E e1, final E e2) {
        return c.getDistanceBetween(e1.getName(), e2.getName());
    }

    public long getPhysicalDistance(final CircularOrbit<L, E> c,
                                    final E e1, final E e2) {
        Position position1 = c.getPosition(e1);
        Position position2 = c.getPosition(e2);

        return (long) (Math.cos(position1.getLevel()
                * position1.getAngle()) * (position2.getLevel()
                + position2.getAngle()));
    }

    Difference getDifference(final CircularOrbit<L, E> c1,
                             final CircularOrbit<L, E> c2) {
        Difference difference = new Difference();

        int trackSize1 = c1.trackCount();
        int trackSize2 = c2.trackCount();

        int min = Integer.min(trackSize1, trackSize2);

        for (int i = 0; i < min; i++) {
            List<E> objects1 = c1.getObjectsByLevel(i + 1);
            List<E> objects2 = c2.getObjectsByLevel(i + 1);

            difference.addDifference("轨道" + (i + 1)
                    + "物体数量差异", objects1.size() - objects2.size());
        }
        return difference;
    }

}

final class Difference {
    private Map<String, Integer> difference = new HashMap<>();

    void addDifference(final String key, final int value) {
        difference.put(key, value);
    }

    public Map<String, Integer> getDifference() {
        return difference;
    }
}

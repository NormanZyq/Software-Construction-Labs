package API;

import abs.center.CenterObject;
import abs.CircularOrbit;
import abs.PhysicalObject;
import abs.Position;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CircularOrbitAPIs<L extends CenterObject, E extends PhysicalObject> {

    public double getObjectDistributionEntropy(CircularOrbit c) {
        int trackSize = c.trackCount();

        int sum = 0;
        for (int i = 0; i < trackSize; i++) {
            sum += c.getObjectsByLevel(i + 1).size();
        }

        double average = sum / (double)trackSize;

        double entropy = 0;
        for (int i = 0; i < trackSize; i++) {
            entropy += c.getObjectsByLevel(i).size() - average;
        }
        return entropy;

    }

    public int getLogicalDistance(CircularOrbit c, E e1, E e2) {
        return c.getDistanceBetween(e1.getName(), e2.getName());
    }

    public long getPhysicalDistance(CircularOrbit<L, E> c, E e1, E e2) {
        Position position1 = c.getPosition(e1);
        Position position2 = c.getPosition(e2);

        return (long) (Math.cos(position1.getLevel() * position1.getAngle()) * (position2.getLevel() + position2.getAngle()));
    }

    Difference getDifference (CircularOrbit<L, E> c1, CircularOrbit<L, E> c2) {
        Difference difference = new Difference();

        int trackSize1 = c1.trackCount();
        int trackSize2 = c2.trackCount();

        int min = Integer.min(trackSize1, trackSize2);

        for (int i = 0; i < min; i++) {
            List<E> objects1 = c1.getObjectsByLevel(i + 1);
            List<E> objects2 = c2.getObjectsByLevel(i + 1);

            difference.addDifference("轨道" + (i + 1) + "物体数量差异", objects1.size() - objects2.size());
        }
        return difference;
    }

}

class Difference {
    private Map<String, Integer> difference = new HashMap<>();

    void addDifference(String key, int value) {
        difference.put(key, value);
    }
}
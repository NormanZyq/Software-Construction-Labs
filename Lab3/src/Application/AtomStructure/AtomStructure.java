package Application.AtomStructure;

import abs.ConcreteCircularOrbit;

import java.util.List;
import java.util.NoSuchElementException;

public class AtomStructure extends ConcreteCircularOrbit<Nuclear, Electron> {

    public static AtomStructure empty() {
        return new AtomStructure();
    }

    public boolean removeElectronFromTrack(int level) {
        if (tracks.get(level) == null) return false;        // track does not exist

        List<Electron> electrons = objectsOnTrack.get(tracks.get(level));

        removeFromTrack(electrons.get(0));

        checkRep();

        return true;
    }

    private void checkRep() {
        assert true;
    }

    /**
     *
     * @param e
     * @param toLevel
     */
    public void electronTransit(Electron e, int toLevel) {
        Electron electron = removeFromTrack(e);
        if (electron == null) {
            throw new NoSuchElementException("The electron does not exist");
        }
        addToTrack(e, toLevel, 0, 0);
    }

    public void electronTransit(int fromLevel, int toLevel, int num) {
        if (!containsLevel(fromLevel) || !containsLevel(toLevel)) return;

        List<Electron> electronOnFromLevel = objectsOnTrack.get(tracks.get(fromLevel));
        int size = electronOnFromLevel.size();
        num = Integer.min(size, num);

        for (int i = 0; i < num; i++) {
            electronTransit(electronOnFromLevel.get(i), toLevel);
        }

    }
}




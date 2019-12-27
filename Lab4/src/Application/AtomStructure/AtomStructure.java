package Application.AtomStructure;

import MyException.AtomStructureException.ElectronDoesNotExistExistException;
import MyException.CircularOrbitExceotion.ObjectDoesNotExistException;
import MyException.CircularOrbitExceotion.NoSuchLevelOfTrackException;
import abs.ConcreteCircularOrbit;
import abs.Position;

import java.util.List;

public class AtomStructure extends ConcreteCircularOrbit<Nuclear, Electron> {
	
	public static AtomStructure empty() {
		return new AtomStructure();
	}
	
	public void removeElectronFromTrack(int level) throws NoSuchLevelOfTrackException, ElectronDoesNotExistExistException {
		if (tracks.get(level) == null) throw new NoSuchLevelOfTrackException(level + "号轨道不存在。");        // track does not exist
		List<Electron> electrons = getObjectsByLevel(level);
		if (electrons.size() > 0) {
			try {
				removeObject(electrons.get(0));
			} catch (ObjectDoesNotExistException ignored) { }
		} else {
			throw new ElectronDoesNotExistExistException();
		}
		checkRep();
	}
	
	private void checkRep() {
		assert true;
	}
	
	/**
	 *
	 * @param e         @requires must be added to the orbit before
	 * @param toLevel   new level
	 * @throws ObjectDoesNotExistException
	 */
	public void electronTransit(Electron e, int toLevel) throws ObjectDoesNotExistException {
		double oldAngle = getPosition(e).getAngle();
		changePosition(e, new Position(toLevel, toLevel, oldAngle));
	}
	
	public void electronTransit(int fromLevel, int toLevel, int num) throws NoSuchLevelOfTrackException {
		if (!containsLevel(fromLevel)) {
			throw new NoSuchLevelOfTrackException(fromLevel + "层轨道不存在");
		} else if (!containsLevel(toLevel)) {
			throw new NoSuchLevelOfTrackException(toLevel + "层轨道不存在");
		} else {
			List<Electron> electronOnFromLevel = objectsOnTrack.get(tracks.get(fromLevel));
			int size = electronOnFromLevel.size();
			num = Integer.min(size, num);
			
			try {
				for (int i = 0; i < num; i++) {
					electronTransit(electronOnFromLevel.get(0), toLevel);
				}
			} catch (ObjectDoesNotExistException e) {
				e.printStackTrace();
			}
		}
	}
}




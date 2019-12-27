package abs;

import MyException.CircularOrbitExceotion.DoesNotExistException;
import MyException.CircularOrbitExceotion.NoSuchLevelOfTrackException;
import MyException.CircularOrbitExceotion.ObjectNotExistException;
import MyException.CircularOrbitExceotion.OrbitFileParseException;
import MyException.CircularOrbitExceotion.TrackDidExistException;
import abs.Parser.FileParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface CircularOrbit<L, E> extends OrbitAble<L, E> {

    /**
     * first get the file by filepath passed in
     * read and parse everything from the file.
     *
     * @param filepath target file's path
     * @param parser   parser to use
     * @return an object created by file on filepath
     * @throws FileNotFoundException   throws when the file on filepath
     *                                 is not exist
     * @throws OrbitFileParseException throws when anything wrong
     *                                 happened during parsing
     */
    static OrbitAble<?, ?> parseFile(FileParser parser, String filepath)
            throws FileNotFoundException, OrbitFileParseException {
        try {
            return parser.parse(new File(filepath));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * add an object as the center point.
     *
     * @param centerObject an object to be center point
     * @return when there's no center point, and succeeded in adding,
     * then returns true, else false
     */
    L addCenterObject(L centerObject);

    /**
     * add a new track to this orbit model.
     *
     * @param newLevel track level to add
     * @param radius   distance to center object(as a point)
     * @return true iff the track passed in isn't in the
     * model and succeeded in adding
     * @throws TrackDidExistException throws when the new
     *                                level of track is exist in the orbit model
     */
    boolean addTrack(int newLevel, double radius) throws TrackDidExistException;

    /**
     * remove a track in this orbit model.
     *
     * @param level level of track to remove
     * @return true iff the track passed in is already in the model
     * @throws NoSuchLevelOfTrackException throws when the level of
     *                                     track is not in the orbit model
     */
    boolean removeTrack(int level) throws NoSuchLevelOfTrackException;

    /**
     * add directed relation which should be from @from to @code to
     * this method is for obj to obj.
     *
     * @param from   relation from
     * @param to     relation to
     * @param weight weight of the relation
     * @return true iff succeeded in adding this relation
     * @throws ObjectNotExistException throws when from
     *                                     or to is not in the orbit
     */
    boolean addRelation(E from, E to, double weight)
            throws ObjectNotExistException;

    /**
     * add directed relation which should be from center object to @code to.
     *
     * @param to     relation to
     * @param weight weight of this relation
     * @return true iff succeeded in adding this relation
     */
    boolean addRelationWithCenter(E to, double weight);

    /**
     * remove a relation from the orbit model.
     *
     * @param p1 source
     * @param p2 target
     * @return true
     * @throws DoesNotExistException throws when p1
     *                               or p2 is not in the orbit
     */
    boolean removeRelation(E p1, E p2) throws DoesNotExistException;

    /**
     * add an object to track @targetLevel.
     *
     * @param object      object to add
     * @param targetLevel track on targetLevel to receive the object
     * @param radius      radius from center object
     * @param angle       angle from 0º of this object
     * @return true if success, else no changes
     * happen to this model and returns false
     * @throws NoSuchLevelOfTrackException throws when target level is not
     *                                     exist inthe orbit model
     */
    boolean addToTrack(E object, int targetLevel, double radius, double angle)
            throws NoSuchLevelOfTrackException;

    /**
     * remove the object from the orbit model.
     * what will be removed:
     * 1. object itself
     * 2. relations connected to this object
     *
     * @param object the object you want to remove
     * @return true iff succeeded in removing
     * @throws DoesNotExistException when the object is not
     *                               exist, it will be thrown
     */
    boolean removeObject(E object) throws DoesNotExistException;

    /**
     * get the object's level on this model.
     *
     * @param object the object you want to get level
     * @return level an int, -1 if the object is not existed
     */
    int getLevelByObject(E object);

    /**
     * get objects by level, should be sorted by angle (if has).
     *
     * @param level level to find
     * @return the objects on level
     */
    List<E> getObjectsByLevel(int level);

    /**
     * get center object.
     *
     * @return center object
     */
    L getCenterObject();

    /**
     * use object to get its position.
     *
     * @param object the object you want to get position
     * @return the position of the object, null if the object does not exist
     */
    Position getPosition(E object);

    /**
     * track number in a orbit model.
     *
     * @return track number
     */
    int trackCount();

    /***
     * get distance between two objects.
     * @param name1     label 1
     * @param name2     label2
     * @return distance an int
     */
    int getDistanceBetween(String name1, String name2);

    /**
     * get track object by level.
     * the track object is imuutable so it's safe to be exposed
     *
     * @param level level of track you want to get
     * @return the track object
     */
    Track getTrack(int level);

}

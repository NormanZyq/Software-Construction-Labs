package abs;

import MyException.CircularOrbitExceotion.*;
import abs.Parser.FileParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public interface CircularOrbit<L, E> extends OrbitAble<L, E> {

    /**
     * first get the file by filepath passed in
     * read and parse everything from the file
     * @param filepath   target file's path
     * @param parser    parser to use
     */
    static OrbitAble<?, ?> parseFile(FileParser parser, String filepath) throws FileNotFoundException, OrbitFileParseException {
        return parser.parse(new File(filepath));
    }

    /**
     * add an object as the center point
     * @param centerObject   an object to be center point
     * @return     when there's no center point, and succeeded in adding, then returns true, else false
     */
    L addCenterObject(L centerObject);

    /**
     * add a new track to this orbit model
     * @param newLevel      track level to add
     * @param radius    distance to center object(as a point)
     * @return      true iff the track passed in isn't in the model and succeeded in adding
     */
    boolean addTrack(int newLevel, double radius) throws TrackDidExistException;

    /**
     * remove a track in this orbit model
     * @param level    level of track to remove
     * @return      true iff the track passed in is already in the model
     */
    boolean removeTrack(int level) throws NoSuchLevelOfTrackException, TrackDidExistException;

    /**
     * add directed relation which should be from @from to @code to
     * this method is for obj to obj
     * @param from      relation from
     * @param to        relation to
     * @param weight    weight of the relation
     * @return          true iff succeeded in adding this relation
     */
    boolean addRelation(E from, E to, double weight) throws ObjectDoesNotExistException;

    /**
     * add directed relation which should be from center object to @code to
     * @param to        relation to
     * @param weight    weight of this relation
     * @return          true iff succeeded in adding this relation
     */
    boolean addRelationWithCenter(E to, double weight);

    /**
     * remove a relation from the orbit model
     * @param p1    source
     * @param p2    target
     * @return      true
     */
    boolean removeRelation(E p1, E p2) throws DoesNotExistException;

    /**
     * add an object to track @targetLevel
     *
     * @param object    object to add
     * @param targetLevel    track on targetLevel to receive the object
     * @param radius    radius from center object
     * @param angle     angle from 0º of this object
     * @return          true if success, else no changes happen to this model and returns false
     */
    boolean addToTrack(E object, int targetLevel, double radius, double angle) throws NoSuchLevelOfTrackException;

    /**
     *
     * @param object
     * @param position
     * @return
     */
    boolean addToTrack(E object, Position position) throws NoSuchLevelOfTrackException;

    boolean removeObject(E object) throws DoesNotExistException;
    
    /**
     * get the object's level on this model
     * @param object   the object you want to get level
     * @return  level an int, -1 if the object is not existed
     */
    int getLevelByObject(E object);

    /**
     * get objects by level, should be sorted by angle (if has)
     * @return  the objects on level
     * @param level     level to find
     */
    List<E> getObjectsByLevel(int level);

    /**
     * get center object
     * @return  center object
     */
    L getCenterObject();

    /**
     * use object to get its position
     * @param object        the object you want to get position
     * @return          the position of the object, null if the object does not exist
     */
    Position getPosition(E object);

    int trackCount();

    int getDistanceBetween(String name1, String name2);
    
    Track getTrack(int level);

}

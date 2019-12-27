package MyException.TrackGameException;

import MyException.CircularOrbitExceotion.DoesNotExistException;

public class AthleteDoesNotExistException extends DoesNotExistException {

    public AthleteDoesNotExistException(String message) {
        super(message);
    }
}

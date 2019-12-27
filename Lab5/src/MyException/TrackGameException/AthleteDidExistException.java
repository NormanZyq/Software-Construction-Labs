package MyException.TrackGameException;

import MyException.CircularOrbitExceotion.DidExistException;

public class AthleteDidExistException extends DidExistException {

    public AthleteDidExistException(String message) {
        super(message);
    }
}

package MyException.CircularOrbitExceotion;

import MyException.CircularOrbitExceotion.DidExistException;

public class TrackDidExistException extends DidExistException {
    public TrackDidExistException(String message) {
        super(message);
    }
}

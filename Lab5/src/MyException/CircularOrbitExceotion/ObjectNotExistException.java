package MyException.CircularOrbitExceotion;

public class ObjectNotExistException extends DoesNotExistException {
    public ObjectNotExistException() {
    }

    public ObjectNotExistException(String message) {
        super(message);
    }
}

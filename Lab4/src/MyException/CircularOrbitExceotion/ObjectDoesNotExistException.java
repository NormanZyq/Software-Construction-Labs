package MyException.CircularOrbitExceotion;

public class ObjectDoesNotExistException extends DoesNotExistException {
    public ObjectDoesNotExistException() {
    }

    public ObjectDoesNotExistException(String message) {
        super(message);
    }
}

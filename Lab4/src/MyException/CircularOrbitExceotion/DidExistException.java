package MyException.CircularOrbitExceotion;

public class DidExistException extends Exception {
    protected DidExistException() {
    }

    public DidExistException(String message) {
        super(message);
    }

}

package MyException.CircularOrbitExceotion;

public class DoesNotExistException extends Exception {
    protected DoesNotExistException() {
    }

    public DoesNotExistException(String message) {
        super(message);
    }
    
}

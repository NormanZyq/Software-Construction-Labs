package MyException.SocialNetworkCircleException;

import MyException.CircularOrbitExceotion.ObjectNotExistException;

public class PersonNotExistException extends ObjectNotExistException {
    public PersonNotExistException() {
    }

    public PersonNotExistException(String message) {
        super(message);
    }
}

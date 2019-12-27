package MyException.CircularOrbitExceotion;

import org.apache.log4j.Logger;

public class MisArgumentException extends RuntimeException {
    private Logger log = Logger.getLogger(MisArgumentException.class);
    
    public MisArgumentException(String message) {
        super(message);
    }
}

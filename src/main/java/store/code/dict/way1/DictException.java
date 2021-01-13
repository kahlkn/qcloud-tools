package store.code.dict.way1;

import artoria.exception.UncheckedException;

/**
 * Dict exception.
 * @author Kahle
 */
public class DictException extends UncheckedException {

    public DictException() {

        super();
    }

    public DictException(String message) {

        super(message);
    }

    public DictException(Throwable cause) {

        super(cause);
    }

    public DictException(String message, Throwable cause) {

        super(message, cause);
    }

}

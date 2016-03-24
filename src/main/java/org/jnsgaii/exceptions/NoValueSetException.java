package org.jnsgaii.exceptions;

/**
 * Created by Mitchell on 11/25/2015.
 */
public class NoValueSetException extends RuntimeException {

    public NoValueSetException() {
    }

    public NoValueSetException(String message) {
        super(message);
    }

    public NoValueSetException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoValueSetException(Throwable cause) {
        super(cause);
    }

    public NoValueSetException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

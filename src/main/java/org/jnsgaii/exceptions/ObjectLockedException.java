package org.jnsgaii.exceptions;

/**
 * Created by Mitchell on 11/25/2015.
 */
public class ObjectLockedException extends RuntimeException {

    public ObjectLockedException() {
    }

    public ObjectLockedException(String message) {
        super(message);
    }

    public ObjectLockedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ObjectLockedException(Throwable cause) {
        super(cause);
    }

    public ObjectLockedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

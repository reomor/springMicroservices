package com.github.reomor.photoapp.api.users.exception;

/**
 * Base microservice exception
 */
public class CloudBaseException extends RuntimeException {

    public CloudBaseException() {
        super();
    }

    public CloudBaseException(String message) {
        super(message);
    }

    public CloudBaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public CloudBaseException(Throwable cause) {
        super(cause);
    }
}

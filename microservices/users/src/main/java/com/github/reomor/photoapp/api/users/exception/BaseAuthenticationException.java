package com.github.reomor.photoapp.api.users.exception;

/**
 * General authentication exception
 */
public class BaseAuthenticationException extends CloudBaseException {

    public BaseAuthenticationException() {
    }

    public BaseAuthenticationException(String message) {
        super(message);
    }

    public BaseAuthenticationException(Throwable cause) {
        super(cause);
    }
}

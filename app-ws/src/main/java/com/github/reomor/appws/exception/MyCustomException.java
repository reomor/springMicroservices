package com.github.reomor.appws.exception;

public class MyCustomException extends RuntimeException {
    private static final long serialVersionUID = -4393897901830525357L;

    public MyCustomException(String message) {
        super(message);
    }
}

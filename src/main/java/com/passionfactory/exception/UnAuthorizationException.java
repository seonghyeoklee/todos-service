package com.passionfactory.exception;

public class UnAuthorizationException extends RuntimeException {

    public UnAuthorizationException() {
        super();
    }

    public UnAuthorizationException(String msg) {
        super(msg);
    }

}

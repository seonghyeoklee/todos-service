package com.passionfactory.exception;

public class DuplicateUserNameException extends RuntimeException {

    public DuplicateUserNameException() {
        super();
    }

    public DuplicateUserNameException(String msg) {
        super(msg);
    }

}

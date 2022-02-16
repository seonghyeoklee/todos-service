package com.passionfactory.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e) {
        ErrorResponse response = ErrorResponse.builder()
                .status(INTERNAL_SERVER_ERROR.getReasonPhrase())
                .error(e.getMessage())
                .build();

        return new ResponseEntity<>(response, INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DuplicateUserNameException.class)
    protected ResponseEntity<ErrorResponse> handleDuplicateUserNameException(DuplicateUserNameException e) {
        ErrorResponse response = ErrorResponse.builder()
                .status(BAD_REQUEST.getReasonPhrase())
                .error(e.getMessage())
                .build();

        return new ResponseEntity<>(response, BAD_REQUEST);
    }
}

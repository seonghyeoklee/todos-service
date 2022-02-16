package com.passionfactory.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<ErrorResponse> handleRuntimeException() {
        HttpStatus httpStatus = INTERNAL_SERVER_ERROR;
        return ResponseEntity
                .status(httpStatus)
                .body(getErrorResponse(httpStatus));
    }

    @ExceptionHandler(DuplicateUserNameException.class)
    protected ResponseEntity<ErrorResponse> handleDuplicateUserNameException() {
        HttpStatus httpStatus = BAD_REQUEST;
        return ResponseEntity
                .status(httpStatus)
                .body(getErrorResponse(httpStatus));
    }

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleNotFoundException() {
        HttpStatus httpStatus = NOT_FOUND;
        return ResponseEntity
                .status(httpStatus)
                .body(getErrorResponse(httpStatus));
    }

    private ErrorResponse getErrorResponse(HttpStatus httpStatus) {
        return ErrorResponse.builder()
                .status(String.valueOf(httpStatus.value()))
                .error(httpStatus.getReasonPhrase())
                .build();
    }
}

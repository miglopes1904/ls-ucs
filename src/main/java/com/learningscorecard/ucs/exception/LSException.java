package com.learningscorecard.ucs.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class LSException extends RuntimeException {

    private String message;
    private String detail;
    private HttpStatus httpStatus;

    public LSException() {
    }

    public LSException(String message) {
        super(message);
        this.message = message;
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public LSException(String message, String detail, HttpStatus status) {
        super(message);
        this.message = message;
        this.httpStatus = status;
    }

    public LSException(String message, String detail, HttpStatus httpStatus, Throwable cause) {
        super(message, cause);
        this.message = message;
        this.detail = detail;
        this.httpStatus = httpStatus;
    }

    public LSException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public LSException(String message, HttpStatus status, Throwable cause) {
        super(message, cause);
        this.message = message;
        this.httpStatus = status;
    }

    public LSException(String message, String detail, Throwable cause) {
        super(message, cause);
        this.message = message;
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        this.detail = detail;
    }

    public LSException(String message, HttpStatus status) {
        super(message);
        this.message = message;
        this.httpStatus = status;
    }

}

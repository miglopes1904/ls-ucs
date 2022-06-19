package com.learningscorecard.ucs.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.Date;

@ControllerAdvice
@Slf4j
public class LSExceptionHandler {

    public static final String EXCEPTION_THROWN_MESSAGE_CAUSE =
            "\n**** Exception thrown -> {} \n**** Message: {} \n**** Cause: {} ";

    @ExceptionHandler(LSException.class)
    protected ResponseEntity<ExceptionResponseBody> globalExceptionHandler(LSException e) {
        log.error(EXCEPTION_THROWN_MESSAGE_CAUSE,
                e.getClass().getSimpleName(), e.getMessage(), e.getStackTrace()[0]);
        return new ResponseEntity<>(
                ExceptionResponseBody
                        .builder()
                        .message(e.getMessage())
                        .detail(e.getDetail())
                        .timestamp(LocalDateTime.now())
                        .build(),
                e.getHttpStatus()
        );
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ExceptionResponseBody> genericExceptionHandler(Exception e) {
        log.error(EXCEPTION_THROWN_MESSAGE_CAUSE,
                e.getClass().getSimpleName(), e.getMessage(), e.getStackTrace()[0]);
        return new ResponseEntity<>(
                ExceptionResponseBody
                        .builder()
                        .message("Internal error has occurred")
                        .timestamp(LocalDateTime.now())
                        .build(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ExceptionResponseBody> handleAccessDeniedException(AccessDeniedException e, WebRequest request) {
        log.error(EXCEPTION_THROWN_MESSAGE_CAUSE,
                e.getClass().getSimpleName(), e.getMessage(), e.getStackTrace()[0]);
        return new ResponseEntity<>(
                ExceptionResponseBody
                        .builder()
                        .message("Access denied")
                        .timestamp(LocalDateTime.now())
                        .build(),
                HttpStatus.FORBIDDEN
        );
    }
}

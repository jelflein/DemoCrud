package com.example.democrud.config;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ConstrainViolationEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ConstraintViolationException.class, ConstraintViolationException.class})
    protected ResponseEntity<Object> handleConflict(
            RuntimeException ex,
            WebRequest request
    ) {
        return handleExceptionInternal(
                ex,
                ex.getLocalizedMessage(),
                new HttpHeaders(),
                HttpStatus.CONFLICT,
                request
        );
    }
}
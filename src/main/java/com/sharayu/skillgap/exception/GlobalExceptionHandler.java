package com.sharayu.skillgap.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex){


        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(
                error -> errors.put(error.getCode(), error.getDefaultMessage()));

        log.error("Validation failed. Errors: {}", errors);

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);

    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String,String>> handleConstraintViolation(ConstraintViolationException ex) {

        Map<String,String> error = new HashMap<>();

        ex.getConstraintViolations().forEach(v -> {
            error.put(v.getPropertyPath().toString(), v.getMessage());
        });

        log.error("Constraint violation failed: {}", error);

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    //ALL duplicate cases here
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<String> handleDuplicate(DuplicateResourceException ex) {

        log.error("Duplicate resource: {}", ex.getMessage());

        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    // ALL not found cases here
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleNotFound(ResourceNotFoundException ex) {

        log.error("Resource not found: {}", ex.getMessage());

        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    //debug + fallback
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneral(Exception ex) {

        log.error("Unexpected error occurred", ex);

        return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
package com.flatlogic.app.generator.controller;

import com.flatlogic.app.generator.exception.NoSuchEntityException;
import com.flatlogic.app.generator.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * GlobalControllerExceptionHandler controllerAdvice.
 */
@RestControllerAdvice
@Slf4j
public class GlobalControllerExceptionHandler {

    /**
     * NoSuchEntityException handler.
     *
     * @param e NoSuchEntityException
     * @return Error message
     */
    @ExceptionHandler(NoSuchEntityException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleNoSuchEntityException(NoSuchEntityException e) {
        log.error("NoSuchEntityException handler.", e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * ValidationException handler.
     *
     * @param e ValidationException
     * @return Error message
     */
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleValidationException(ValidationException e) {
        log.error("ValidationException handler.", e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Exception handler.
     *
     * @param e Exception
     * @return Error message
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<String> handleException(Exception e) {
        log.error("Exception handler.", e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    /**
     * Throwable handler.
     *
     * @param e Throwable
     */
    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public void handleThrowable(Throwable e) {
        log.error("Throwable handler.", e);
    }

}

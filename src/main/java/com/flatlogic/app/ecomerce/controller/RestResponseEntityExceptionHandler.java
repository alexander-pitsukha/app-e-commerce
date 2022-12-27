package com.flatlogic.app.ecomerce.controller;

import com.flatlogic.app.ecomerce.exception.NoSuchEntityException;
import com.flatlogic.app.ecomerce.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * GlobalControllerExceptionHandler controllerAdvice.
 */
@RestControllerAdvice
@Slf4j
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * NoSuchEntityException handler.
     *
     * @param e NoSuchEntityException
     * @return Error message
     */
    @ExceptionHandler(NoSuchEntityException.class)
    public ResponseEntity<Object> handleNoSuchEntityException(NoSuchEntityException e, WebRequest request) {
        log.error("NoSuchEntityException handler.", e);
        return handleExceptionInternal(e, e.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    /**
     * ValidationException handler.
     *
     * @param e ValidationException
     * @return Error message
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handleValidationException(ValidationException e, WebRequest request) {
        log.error("ValidationException handler.", e);
        return handleExceptionInternal(e, e.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

}

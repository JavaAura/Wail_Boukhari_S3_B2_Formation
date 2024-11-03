package com.formation.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import javax.validation.ConstraintViolationException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(BaseException ex, WebRequest request) {
        HttpStatus status = determineHttpStatus(ex);
        ErrorResponse errorResponse = new ErrorResponse(
            status.value(),
            ex.getCode().getCode(),
            ex.getMessage(),
            request.getDescription(false)
        );
        
        log.error("Exception occurred: {}", errorResponse);
        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            ConstraintViolationException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "VAL-400",
            ex.getMessage(),
            request.getDescription(false)
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    private HttpStatus determineHttpStatus(BaseException ex) {
        if (ex instanceof ResourceNotFoundException) return HttpStatus.NOT_FOUND;
        if (ex instanceof DuplicateResourceException) return HttpStatus.CONFLICT;
        if (ex instanceof ResourceInUseException) return HttpStatus.CONFLICT;
        if (ex instanceof ValidationException) return HttpStatus.BAD_REQUEST;
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
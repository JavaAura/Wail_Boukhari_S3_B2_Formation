package com.formation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateResourceException extends BaseException {
    public DuplicateResourceException(ExceptionCode code, Object... args) {
        super(code, args);
    }
}
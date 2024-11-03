package com.formation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ResourceInUseException extends BaseException {
    public ResourceInUseException(ExceptionCode code, Object... args) {
        super(code, args);
    }
}
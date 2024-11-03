package com.formation.exception;

import lombok.Getter;

@Getter
public abstract class BaseException extends RuntimeException {
    private final ExceptionCode code;

    protected BaseException(ExceptionCode code, Object... args) {
        super(String.format(code.getMessageTemplate(), args));
        this.code = code;
    }
}
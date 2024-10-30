package com.formation.exception;

import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public abstract class BaseException extends RuntimeException {
    private final LocalDateTime timestamp;
    private final String message;

    protected BaseException(String message) {
        super(message);
        this.timestamp = LocalDateTime.now();
        this.message = message;
    }
}

package com.virtualmarathon.stravamicroservice.exception;

import org.springframework.http.HttpStatus;

public class StravaInfoException extends RuntimeException{
    private final HttpStatus status;

    public StravaInfoException(String message, HttpStatus status) {
        super(message);
        this.status=status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}

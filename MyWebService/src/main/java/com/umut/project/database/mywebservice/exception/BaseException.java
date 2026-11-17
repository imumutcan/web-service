package com.umut.project.database.mywebservice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


public class BaseException extends RuntimeException{
    @Getter
    private final HttpStatus status;

    public BaseException(String message, HttpStatus status)
    {
        super(message);
        this.status = status;
    }
}

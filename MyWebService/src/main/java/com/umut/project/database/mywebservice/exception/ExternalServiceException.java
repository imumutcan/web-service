package com.umut.project.database.mywebservice.exception;

import org.springframework.http.HttpStatus;

public class ExternalServiceException extends BaseException{
    public ExternalServiceException(String message) {
        super(message, HttpStatus.BAD_GATEWAY);
    }
}

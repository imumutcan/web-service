package com.umut.project.database.mywebservice.exception;

import org.springframework.http.HttpStatus;

public class InvalidRefreshTokenException extends BaseException{
    public InvalidRefreshTokenException() {
        super("auth.refresh.token.invalid", HttpStatus.UNAUTHORIZED);
    }
}

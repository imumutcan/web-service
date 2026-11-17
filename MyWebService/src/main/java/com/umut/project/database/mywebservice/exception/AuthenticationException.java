package com.umut.project.database.mywebservice.exception;

import org.springframework.http.HttpStatus;

public class AuthenticationException extends BaseException{
    public AuthenticationException() {
        super("user.auth.invalid.credential", HttpStatus.UNAUTHORIZED);
    }
}

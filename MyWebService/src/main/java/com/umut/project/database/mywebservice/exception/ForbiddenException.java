package com.umut.project.database.mywebservice.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends BaseException{
    public ForbiddenException() {
        super("user.forbidden", HttpStatus.FORBIDDEN);
    }
}

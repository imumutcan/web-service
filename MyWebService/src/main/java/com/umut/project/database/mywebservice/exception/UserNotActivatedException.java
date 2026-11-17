package com.umut.project.database.mywebservice.exception;

import org.springframework.http.HttpStatus;

public class UserNotActivatedException extends BaseException{
    public UserNotActivatedException() {
        super("user.not.activated", HttpStatus.FORBIDDEN);
    }
}

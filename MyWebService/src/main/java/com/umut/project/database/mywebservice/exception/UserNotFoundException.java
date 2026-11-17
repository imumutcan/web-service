package com.umut.project.database.mywebservice.exception;

public class UserNotFoundException extends ResourceNotFoundException{
    public UserNotFoundException() {
        super("user.not.found");
    }
}

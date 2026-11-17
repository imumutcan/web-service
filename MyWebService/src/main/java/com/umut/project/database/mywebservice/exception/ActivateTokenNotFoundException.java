package com.umut.project.database.mywebservice.exception;

public class ActivateTokenNotFoundException extends ResourceNotFoundException{
    public ActivateTokenNotFoundException() {
        super("user.activatetoken.not.found");
    }
}

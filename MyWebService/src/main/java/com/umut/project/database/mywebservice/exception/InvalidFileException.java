package com.umut.project.database.mywebservice.exception;

public class InvalidFileException extends BusinessException{
    public InvalidFileException() {
        super("file.invalid.type");
    }
}

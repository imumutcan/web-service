package com.umut.project.database.mywebservice.exception;

import org.springframework.http.HttpStatus;

public class FileStorageException extends BaseException{
    public FileStorageException() {
        super("file.storage.error", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

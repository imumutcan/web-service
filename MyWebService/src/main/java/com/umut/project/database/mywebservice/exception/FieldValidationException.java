package com.umut.project.database.mywebservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class FieldValidationException extends BaseException{
    private final String field;

    public FieldValidationException(String field, String messageKey) {
        super(messageKey, HttpStatus.BAD_REQUEST);
        this.field = field;
    }
}

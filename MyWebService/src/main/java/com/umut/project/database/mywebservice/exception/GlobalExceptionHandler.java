package com.umut.project.database.mywebservice.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidException(MethodArgumentNotValidException exception, WebRequest request)
    {
        Map<String, String> fieldErrors = new HashMap<>();
        for(FieldError error:exception.getBindingResult().getFieldErrors())
        {
            fieldErrors.putIfAbsent(error.getField(), error.getDefaultMessage());
        }
        return validationResponse(fieldErrors, request);
    }

    @ExceptionHandler(FieldValidationException.class)
    public ResponseEntity<Map<String, Object>> handleFieldValidationException(FieldValidationException exception, WebRequest request)
    {
        return validationResponse(Map.of(exception.getField(), resolve(exception.getMessage())), request);
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<Map<String, Object>> handleBaseExceptions(BaseException exception, WebRequest request)
    {
        return errorResponse(exception.getStatus(), resolve(exception.getMessage()), request);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Map<String, Object>> handleMaxUploadSize(MaxUploadSizeExceededException exception, WebRequest request)
    {
        return errorResponse(HttpStatus.PAYLOAD_TOO_LARGE, resolve("file.too.large"), request);
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<Map<String, Object>> handleMissingPart(MissingServletRequestPartException exception, WebRequest request)
    {
        return errorResponse(HttpStatus.BAD_REQUEST, resolve("file.required"), request);
    }

    private ResponseEntity<Map<String, Object>> validationResponse(Map<String, String> fieldErrors, WebRequest request)
    {
        Map<String, Object> response = Map.of(
                "timestamp",new Date(),
                "status", HttpStatus.BAD_REQUEST.value(),
                "message", resolve("validation.failure"),
                "path", request.getDescription(false),
                "errors", fieldErrors
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    private ResponseEntity<Map<String, Object>> errorResponse(HttpStatus status, String message, WebRequest request)
    {
        Map<String, Object> response = Map.of(
                "timestamp", new Date(),
                "status", status.value(),
                "message", message,
                "path", request.getDescription(false)
        );
        return ResponseEntity.status(status).body(response);
    }

    private String resolve(String key)
    {
        return messageSource.getMessage(key, null, key, LocaleContextHolder.getLocale());
    }
}

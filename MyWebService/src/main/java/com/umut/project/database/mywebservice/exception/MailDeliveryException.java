package com.umut.project.database.mywebservice.exception;

public class MailDeliveryException extends ExternalServiceException{
    public MailDeliveryException() {
        super("user.mail.not.send");
    }
}

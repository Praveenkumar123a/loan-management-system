package com.nexturn.lms.exception;

public class AuditLogNotFoundException extends RuntimeException {
    public AuditLogNotFoundException(String message){
        super(message);
    }
    
}

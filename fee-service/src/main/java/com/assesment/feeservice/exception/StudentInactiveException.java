package com.assesment.feeservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class StudentInactiveException extends RuntimeException {
    
    public StudentInactiveException(String message) {
        super(message);
    }
}

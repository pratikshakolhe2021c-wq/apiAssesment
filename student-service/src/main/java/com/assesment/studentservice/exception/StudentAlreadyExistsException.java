package com.assesment.studentservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class StudentAlreadyExistsException extends RuntimeException {
    
    public StudentAlreadyExistsException(String message) {
        super(message);
    }
}

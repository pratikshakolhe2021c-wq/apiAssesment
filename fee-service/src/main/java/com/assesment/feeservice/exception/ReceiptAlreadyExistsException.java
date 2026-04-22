package com.assesment.feeservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ReceiptAlreadyExistsException extends RuntimeException {
    
    public ReceiptAlreadyExistsException(String message) {
        super(message);
    }
}

package com.assesment.feeservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ReceiptNotFoundException extends RuntimeException {
    
    public ReceiptNotFoundException(String message) {
        super(message);
    }
    
    public ReceiptNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

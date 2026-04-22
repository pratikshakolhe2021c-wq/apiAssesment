package com.assesment.feeservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidReceiptDataException extends RuntimeException {
    
    public InvalidReceiptDataException(String message) {
        super(message);
    }
}

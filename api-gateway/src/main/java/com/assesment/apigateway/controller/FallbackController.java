package com.assesment.apigateway.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class FallbackController {
    
    @RequestMapping("/fallback/students")
    public ResponseEntity<Map<String, Object>> studentServiceFallback() {
        log.warn("Student service is currently unavailable. Fallback activated.");
        
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());
        response.put("error", "Service Unavailable");
        response.put("message", "Student service is temporarily unavailable. Please try again later.");
        response.put("path", "/api/v1/students");
        
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }
    
    @RequestMapping("/fallback/fee")
    public ResponseEntity<Map<String, Object>> feeServiceFallback() {
        log.warn("Fee service is currently unavailable. Fallback activated.");
        
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());
        response.put("error", "Service Unavailable");
        response.put("message", "Fee service is temporarily unavailable. Please try again later.");
        response.put("path", "/api/v1/fee");
        
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }
}

package com.assesment.feeservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptDTO {
    
    private Long id;
    
    @NotBlank(message = "Receipt number is required")
    @Size(max = 50, message = "Receipt number must not exceed 50 characters")
    private String receiptNumber;
    
    @NotNull(message = "Student ID is required")
    private Long studentId;
    
    private String studentName;
    
    @NotBlank(message = "Academic year is required")
    @Size(max = 20, message = "Academic year must not exceed 20 characters")
    private String academicYear;
    
    @NotBlank(message = "Fee type is required")
    @Size(max = 50, message = "Fee type must not exceed 50 characters")
    private String feeType;
    
    @NotNull(message = "Amount is required")
    private BigDecimal amount;
    
    @NotBlank(message = "Payment method is required")
    @Size(max = 50, message = "Payment method must not exceed 50 characters")
    private String paymentMethod;
    
    @Size(max = 100, message = "Transaction ID must not exceed 100 characters")
    private String transactionId;
    
    private LocalDateTime paymentDate;
    
    @Size(max = 500, message = "Notes must not exceed 500 characters")
    private String notes;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}

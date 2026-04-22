package com.assesment.apiAssesment.Fee.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    private String receiptNumber;
    
    @NotNull(message = "Student ID is required")
    private Long studentId;
    
    @NotBlank(message = "Student name is required")
    private String studentName;
    
    @NotBlank(message = "Fee type is required")
    private String feeType;
    
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;
    
    private LocalDateTime paymentDate;
    
    @NotBlank(message = "Payment method is required")
    private String paymentMethod;
    
    private String transactionId;
    
    private String referenceNumber;
    
    private String cardNumber;
    
    private String cardType;
    
    private String schoolName;
    
    private String gradeLevel;
    
    private Integer quantity;
    
    private BigDecimal unitPrice;
    
    private BigDecimal customAmount;
    
    @NotBlank(message = "Academic year is required")
    private String academicYear;
    
    private String month;
    
    private String quarter;
    
    private String remarks;
    
    private LocalDateTime createdAt;
    
    private String createdBy;
}

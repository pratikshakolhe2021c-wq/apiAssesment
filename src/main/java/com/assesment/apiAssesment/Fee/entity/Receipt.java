package com.assesment.apiAssesment.Fee.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "receipts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Receipt {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Receipt number is required")
    @Column(name = "receipt_number", nullable = false, unique = true)
    private String receiptNumber;
    
    @NotNull(message = "Student ID is required")
    @Column(name = "student_id", nullable = false)
    private Long studentId;
    
    @NotBlank(message = "Student name is required")
    @Column(name = "student_name", nullable = false)
    private String studentName;
    
    @NotBlank(message = "Fee type is required")
    @Column(name = "fee_type", nullable = false)
    private String feeType;
    
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;
    
    @NotNull(message = "Payment date is required")
    @Column(name = "payment_date", nullable = false)
    private LocalDateTime paymentDate;
    
    @NotBlank(message = "Payment method is required")
    @Column(name = "payment_method", nullable = false)
    private String paymentMethod;
    
    @Column(name = "transaction_id")
    private String transactionId;
    
    @Column(name = "reference_number")
    private String referenceNumber;
    
    @Column(name = "card_number")
    private String cardNumber;
    
    @Column(name = "card_type")
    private String cardType;
    
    @Column(name = "school_name")
    private String schoolName;
    
    @Column(name = "grade_level")
    private String gradeLevel;
    
    @Column(name = "quantity")
    private Integer quantity;
    
    @Column(name = "unit_price")
    private BigDecimal unitPrice;
    
    @Column(name = "custom_amount")
    private BigDecimal customAmount;
    
    @NotBlank(message = "Academic year is required")
    @Column(name = "academic_year", nullable = false)
    private String academicYear;
    
    @Column(name = "fee_month")
    private String month;
    
    @Column(name = "quarter")
    private String quarter;
    
    @Column(name = "remarks")
    private String remarks;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "created_by", nullable = false)
    private String createdBy;
    
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (paymentDate == null) {
            paymentDate = LocalDateTime.now();
        }
    }
}

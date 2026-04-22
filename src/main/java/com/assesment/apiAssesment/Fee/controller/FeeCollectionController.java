package com.assesment.apiAssesment.Fee.controller;

import com.assesment.apiAssesment.Fee.dto.ReceiptDTO;
import com.assesment.apiAssesment.Fee.service.FeeCollectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/fee")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Fee Collection", description = "APIs for managing fee collection and receipts")
public class FeeCollectionController {
    
    private final FeeCollectionService feeCollectionService;
    
    @PostMapping("/collect")
    @Operation(summary = "Collect fee from student", description = "Collect fee and generate receipt")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Fee collected successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Student not found"),
        @ApiResponse(responseCode = "409", description = "Receipt number already exists")
    })
    public ResponseEntity<ReceiptDTO> collectFee(@Valid @RequestBody ReceiptDTO receiptDTO) {
        log.info("REST request to collect fee: {}", receiptDTO);
        try {
            ReceiptDTO receipt = feeCollectionService.collectFee(receiptDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(receipt);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("Student not found")) {
                return ResponseEntity.notFound().build();
            }
            if (e.getMessage().contains("already exists")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/receipts")
    @Operation(summary = "Get all receipts", description = "Retrieve a list of all fee receipts")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Receipts retrieved successfully")
    })
    public ResponseEntity<List<ReceiptDTO>> getAllReceipts() {
        log.info("REST request to get all receipts");
        List<ReceiptDTO> receipts = feeCollectionService.getAllReceipts();
        return ResponseEntity.ok(receipts);
    }
    
    @GetMapping("/receipts/{id}")
    @Operation(summary = "Get receipt by ID", description = "Retrieve a specific receipt by its database ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Receipt found"),
        @ApiResponse(responseCode = "404", description = "Receipt not found")
    })
    public ResponseEntity<ReceiptDTO> getReceiptById(
            @Parameter(description = "Receipt database ID") @PathVariable Long id) {
        log.info("REST request to get receipt by ID: {}", id);
        Optional<ReceiptDTO> receipt = feeCollectionService.getReceiptById(id);
        return receipt.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/receipts/by-number/{receiptNumber}")
    @Operation(summary = "Get receipt by receipt number", description = "Retrieve a specific receipt by its receipt number")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Receipt found"),
        @ApiResponse(responseCode = "404", description = "Receipt not found")
    })
    public ResponseEntity<ReceiptDTO> getReceiptByReceiptNumber(
            @Parameter(description = "Receipt number") @PathVariable String receiptNumber) {
        log.info("REST request to get receipt by receipt number: {}", receiptNumber);
        Optional<ReceiptDTO> receipt = feeCollectionService.getReceiptByReceiptNumber(receiptNumber);
        return receipt.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/receipts/student/{studentId}")
    @Operation(summary = "Get receipts by student ID", description = "Retrieve all receipts for a specific student")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Receipts retrieved successfully")
    })
    public ResponseEntity<List<ReceiptDTO>> getReceiptsByStudentId(
            @Parameter(description = "Student ID") @PathVariable Long studentId) {
        log.info("REST request to get receipts for student ID: {}", studentId);
        List<ReceiptDTO> receipts = feeCollectionService.getReceiptsByStudentId(studentId);
        return ResponseEntity.ok(receipts);
    }
    
    @GetMapping("/receipts/academic-year/{academicYear}")
    @Operation(summary = "Get receipts by academic year", description = "Retrieve all receipts for a specific academic year")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Receipts retrieved successfully")
    })
    public ResponseEntity<List<ReceiptDTO>> getReceiptsByAcademicYear(
            @Parameter(description = "Academic year") @PathVariable String academicYear) {
        log.info("REST request to get receipts for academic year: {}", academicYear);
        List<ReceiptDTO> receipts = feeCollectionService.getReceiptsByAcademicYear(academicYear);
        return ResponseEntity.ok(receipts);
    }
    
    @GetMapping("/total/student/{studentId}/academic-year/{academicYear}")
    @Operation(summary = "Get total fees paid by student", description = "Calculate total fees paid by a student for a specific academic year")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Total calculated successfully")
    })
    public ResponseEntity<BigDecimal> getTotalFeesPaidByStudent(
            @Parameter(description = "Student ID") @PathVariable Long studentId,
            @Parameter(description = "Academic year") @PathVariable String academicYear) {
        log.info("REST request to get total fees for student {} in academic year: {}", studentId, academicYear);
        BigDecimal total = feeCollectionService.getTotalFeesPaidByStudent(studentId, academicYear);
        return ResponseEntity.ok(total);
    }
    
    @GetMapping("/total/academic-year/{academicYear}")
    @Operation(summary = "Get total fees collected", description = "Calculate total fees collected for a specific academic year")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Total calculated successfully")
    })
    public ResponseEntity<BigDecimal> getTotalFeesCollected(
            @Parameter(description = "Academic year") @PathVariable String academicYear) {
        log.info("REST request to get total fees collected for academic year: {}", academicYear);
        BigDecimal total = feeCollectionService.getTotalFeesCollected(academicYear);
        return ResponseEntity.ok(total);
    }
    
    @GetMapping("/receipts/search")
    @Operation(summary = "Search receipts", description = "Search receipts by student name, receipt number, or transaction ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Search completed successfully")
    })
    public ResponseEntity<List<ReceiptDTO>> searchReceipts(
            @Parameter(description = "Search term") @RequestParam String search) {
        log.info("REST request to search receipts with term: {}", search);
        List<ReceiptDTO> receipts = feeCollectionService.searchReceipts(search);
        return ResponseEntity.ok(receipts);
    }
    
    @GetMapping("/receipts/date-range")
    @Operation(summary = "Get receipts by date range", description = "Retrieve receipts within a specific date range")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Receipts retrieved successfully")
    })
    public ResponseEntity<List<ReceiptDTO>> getReceiptsByDateRange(
            @Parameter(description = "Start date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        log.info("REST request to get receipts between {} and {}", startDate, endDate);
        List<ReceiptDTO> receipts = feeCollectionService.getReceiptsByDateRange(startDate, endDate);
        return ResponseEntity.ok(receipts);
    }
    
    @GetMapping("/receipts/fee-type/{feeType}")
    @Operation(summary = "Get receipts by fee type", description = "Retrieve all receipts for a specific fee type")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Receipts retrieved successfully")
    })
    public ResponseEntity<List<ReceiptDTO>> getReceiptsByFeeType(
            @Parameter(description = "Fee type") @PathVariable String feeType) {
        log.info("REST request to get receipts by fee type: {}", feeType);
        List<ReceiptDTO> receipts = feeCollectionService.getReceiptsByFeeType(feeType);
        return ResponseEntity.ok(receipts);
    }
    
    @GetMapping("/receipts/payment-method/{paymentMethod}")
    @Operation(summary = "Get receipts by payment method", description = "Retrieve all receipts for a specific payment method")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Receipts retrieved successfully")
    })
    public ResponseEntity<List<ReceiptDTO>> getReceiptsByPaymentMethod(
            @Parameter(description = "Payment method") @PathVariable String paymentMethod) {
        log.info("REST request to get receipts by payment method: {}", paymentMethod);
        List<ReceiptDTO> receipts = feeCollectionService.getReceiptsByPaymentMethod(paymentMethod);
        return ResponseEntity.ok(receipts);
    }
    
    @GetMapping("/receipt-number/generate")
    @Operation(summary = "Generate receipt number", description = "Generate a unique receipt number")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Receipt number generated successfully")
    })
    public ResponseEntity<String> generateReceiptNumber() {
        log.info("REST request to generate receipt number");
        String receiptNumber = feeCollectionService.generateReceiptNumber();
        return ResponseEntity.ok(receiptNumber);
    }
}

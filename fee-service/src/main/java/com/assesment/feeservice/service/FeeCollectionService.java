package com.assesment.feeservice.service;

import com.assesment.feeservice.client.StudentServiceClient;
import com.assesment.feeservice.dto.ReceiptDTO;
import com.assesment.feeservice.dto.StudentDTO;
import com.assesment.feeservice.entity.Receipt;
import com.assesment.feeservice.exception.*;
import com.assesment.feeservice.mapper.ReceiptMapper;
import com.assesment.feeservice.repository.ReceiptRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeeCollectionService {
    
    private final ReceiptRepository receiptRepository;
    private final ReceiptMapper receiptMapper;
    private final StudentServiceClient studentServiceClient;
    private final CircuitBreakerFactory circuitBreakerFactory;
    
    @Transactional
    public ReceiptDTO collectFee(ReceiptDTO receiptDTO) {
        log.info("Collecting fee for student ID: {}", receiptDTO.getStudentId());
        
        if (receiptRepository.existsByReceiptNumber(receiptDTO.getReceiptNumber())) {
            throw new ReceiptAlreadyExistsException(
                "Receipt with number " + receiptDTO.getReceiptNumber() + " already exists");
        }
        
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("studentService");
        StudentDTO student = circuitBreaker.run(
            () -> studentServiceClient.getStudentById(receiptDTO.getStudentId())
                .orElseThrow(() -> new StudentNotFoundException("Student not found with ID: " + receiptDTO.getStudentId())),
            throwable -> {
                log.error("Error calling student service: {}", throwable.getMessage());
                throw new StudentNotFoundException("Student service unavailable. Cannot validate student.");
            }
        );
        
        receiptDTO.setStudentName(student.getStudentName());
        
        if (receiptDTO.getPaymentDate() == null) {
            receiptDTO.setPaymentDate(LocalDateTime.now());
        }
        
        Receipt receipt = receiptMapper.toEntity(receiptDTO);
        Receipt savedReceipt = receiptRepository.save(receipt);
        
        log.info("Successfully collected fee with receipt ID: {}", savedReceipt.getId());
        return receiptMapper.toDTO(savedReceipt);
    }
    
    public List<ReceiptDTO> getAllReceipts() {
        log.info("Fetching all receipts");
        List<Receipt> receipts = receiptRepository.findAll();
        return receipts.stream()
                .map(receiptMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    public Optional<ReceiptDTO> getReceiptById(Long id) {
        log.info("Fetching receipt by ID: {}", id);
        return receiptRepository.findById(id)
                .map(receiptMapper::toDTO);
    }
    
    public Optional<ReceiptDTO> getReceiptByReceiptNumber(String receiptNumber) {
        log.info("Fetching receipt by receipt number: {}", receiptNumber);
        return receiptRepository.findByReceiptNumber(receiptNumber)
                .map(receiptMapper::toDTO);
    }
    
    public List<ReceiptDTO> getReceiptsByStudentId(Long studentId) {
        log.info("Fetching receipts for student ID: {}", studentId);
        List<Receipt> receipts = receiptRepository.findByStudentId(studentId);
        return receipts.stream()
                .map(receiptMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<ReceiptDTO> getReceiptsByAcademicYear(String academicYear) {
        log.info("Fetching receipts for academic year: {}", academicYear);
        List<Receipt> receipts = receiptRepository.findByAcademicYear(academicYear);
        return receipts.stream()
                .map(receiptMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    public BigDecimal getTotalFeesPaidByStudent(Long studentId, String academicYear) {
        log.info("Calculating total fees for student {} in academic year: {}", studentId, academicYear);
        return receiptRepository.getTotalFeesPaidByStudent(studentId, academicYear);
    }
    
    public BigDecimal getTotalFeesCollected(String academicYear) {
        log.info("Calculating total fees collected for academic year: {}", academicYear);
        return receiptRepository.getTotalFeesCollected(academicYear);
    }
    
    public List<ReceiptDTO> searchReceipts(String search) {
        log.info("Searching receipts with term: {}", search);
        List<Receipt> receipts = receiptRepository.searchReceipts(search);
        return receipts.stream()
                .map(receiptMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<ReceiptDTO> getReceiptsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Fetching receipts between {} and {}", startDate, endDate);
        List<Receipt> receipts = receiptRepository.findByPaymentDateBetween(startDate, endDate);
        return receipts.stream()
                .map(receiptMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<ReceiptDTO> getReceiptsByFeeType(String feeType) {
        log.info("Fetching receipts by fee type: {}", feeType);
        List<Receipt> receipts = receiptRepository.findByFeeType(feeType);
        return receipts.stream()
                .map(receiptMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<ReceiptDTO> getReceiptsByPaymentMethod(String paymentMethod) {
        log.info("Fetching receipts by payment method: {}", paymentMethod);
        List<Receipt> receipts = receiptRepository.findByPaymentMethod(paymentMethod);
        return receipts.stream()
                .map(receiptMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    public String generateReceiptNumber() {
        log.info("Generating unique receipt number");
        String receiptNumber;
        do {
            receiptNumber = "RCP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        } while (receiptRepository.existsByReceiptNumber(receiptNumber));
        
        return receiptNumber;
    }
}

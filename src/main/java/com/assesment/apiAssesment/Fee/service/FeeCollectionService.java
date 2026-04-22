package com.assesment.apiAssesment.Fee.service;

import com.assesment.apiAssesment.Fee.dto.ReceiptDTO;
import com.assesment.apiAssesment.Fee.entity.Receipt;
import com.assesment.apiAssesment.Fee.repository.ReceiptRepository;
import com.assesment.apiAssesment.Student.entity.Student;
import com.assesment.apiAssesment.Student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FeeCollectionService {
    
    private final ReceiptRepository receiptRepository;
    private final StudentRepository studentRepository;
    
    public ReceiptDTO collectFee(ReceiptDTO receiptDTO) {
        log.info("Collecting fee for student ID: {}", receiptDTO.getStudentId());
        
        Student student = studentRepository.findById(receiptDTO.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + receiptDTO.getStudentId()));
        
        if (receiptRepository.existsByReceiptNumber(receiptDTO.getReceiptNumber())) {
            throw new RuntimeException("Receipt with number " + receiptDTO.getReceiptNumber() + " already exists");
        }
        
        receiptDTO.setStudentName(student.getStudentName());
        
        Receipt receipt = convertToEntity(receiptDTO);
        Receipt savedReceipt = receiptRepository.save(receipt);
        
        log.info("Fee collected successfully with receipt ID: {}", savedReceipt.getId());
        return convertToDTO(savedReceipt);
    }
    
    @Transactional(readOnly = true)
    public List<ReceiptDTO> getAllReceipts() {
        log.info("Retrieving all receipts");
        return receiptRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public Optional<ReceiptDTO> getReceiptById(Long id) {
        log.info("Retrieving receipt with ID: {}", id);
        return receiptRepository.findById(id)
                .map(this::convertToDTO);
    }
    
    @Transactional(readOnly = true)
    public Optional<ReceiptDTO> getReceiptByReceiptNumber(String receiptNumber) {
        log.info("Retrieving receipt with receipt number: {}", receiptNumber);
        return receiptRepository.findByReceiptNumber(receiptNumber)
                .map(this::convertToDTO);
    }
    
    @Transactional(readOnly = true)
    public List<ReceiptDTO> getReceiptsByStudentId(Long studentId) {
        log.info("Retrieving receipts for student ID: {}", studentId);
        return receiptRepository.findByStudentIdOrderByPaymentDateDesc(studentId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<ReceiptDTO> getReceiptsByAcademicYear(String academicYear) {
        log.info("Retrieving receipts for academic year: {}", academicYear);
        return receiptRepository.findByAcademicYear(academicYear).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public BigDecimal getTotalFeesPaidByStudent(Long studentId, String academicYear) {
        log.info("Calculating total fees paid by student {} for academic year: {}", studentId, academicYear);
        BigDecimal total = receiptRepository.getTotalFeesPaidByStudentForYear(studentId, academicYear);
        return total != null ? total : BigDecimal.ZERO;
    }
    
    @Transactional(readOnly = true)
    public BigDecimal getTotalFeesCollected(String academicYear) {
        log.info("Calculating total fees collected for academic year: {}", academicYear);
        BigDecimal total = receiptRepository.getTotalFeesCollectedForYear(academicYear);
        return total != null ? total : BigDecimal.ZERO;
    }
    
    @Transactional(readOnly = true)
    public List<ReceiptDTO> searchReceipts(String search) {
        log.info("Searching receipts with term: {}", search);
        return receiptRepository.searchReceipts(search).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<ReceiptDTO> getReceiptsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Retrieving receipts between {} and {}", startDate, endDate);
        return receiptRepository.findByPaymentDateBetween(startDate, endDate).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<ReceiptDTO> getReceiptsByFeeType(String feeType) {
        log.info("Retrieving receipts by fee type: {}", feeType);
        return receiptRepository.findByFeeType(feeType).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<ReceiptDTO> getReceiptsByPaymentMethod(String paymentMethod) {
        log.info("Retrieving receipts by payment method: {}", paymentMethod);
        return receiptRepository.findByPaymentMethod(paymentMethod).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public String generateReceiptNumber() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timestamp = LocalDateTime.now().format(formatter);
        return "REC" + timestamp;
    }
    
    private Receipt convertToEntity(ReceiptDTO dto) {
        Receipt receipt = new Receipt();
        receipt.setReceiptNumber(dto.getReceiptNumber());
        receipt.setStudentId(dto.getStudentId());
        receipt.setStudentName(dto.getStudentName());
        receipt.setFeeType(dto.getFeeType());
        receipt.setAmount(dto.getAmount());
        receipt.setPaymentDate(dto.getPaymentDate() != null ? dto.getPaymentDate() : LocalDateTime.now());
        receipt.setPaymentMethod(dto.getPaymentMethod());
        receipt.setTransactionId(dto.getTransactionId());
        receipt.setAcademicYear(dto.getAcademicYear());
        receipt.setMonth(dto.getMonth());
        receipt.setQuarter(dto.getQuarter());
        receipt.setRemarks(dto.getRemarks());
        receipt.setCreatedAt(dto.getCreatedAt() != null ? dto.getCreatedAt() : LocalDateTime.now());
        receipt.setCreatedBy(dto.getCreatedBy() != null ? dto.getCreatedBy() : "System");
        return receipt;
    }
    
    private ReceiptDTO convertToDTO(Receipt entity) {
        ReceiptDTO dto = new ReceiptDTO();
        dto.setId(entity.getId());
        dto.setReceiptNumber(entity.getReceiptNumber());
        dto.setStudentId(entity.getStudentId());
        dto.setStudentName(entity.getStudentName());
        dto.setFeeType(entity.getFeeType());
        dto.setAmount(entity.getAmount());
        dto.setPaymentDate(entity.getPaymentDate());
        dto.setPaymentMethod(entity.getPaymentMethod());
        dto.setTransactionId(entity.getTransactionId());
        dto.setAcademicYear(entity.getAcademicYear());
        dto.setMonth(entity.getMonth());
        dto.setQuarter(entity.getQuarter());
        dto.setRemarks(entity.getRemarks());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setCreatedBy(entity.getCreatedBy());
        return dto;
    }
}

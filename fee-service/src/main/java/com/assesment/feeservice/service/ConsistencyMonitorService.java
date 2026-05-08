package com.assesment.feeservice.service;

import com.assesment.feeservice.client.StudentServiceClient;
import com.assesment.feeservice.dto.StudentDTO;
import com.assesment.feeservice.entity.Receipt;
import com.assesment.feeservice.repository.ReceiptRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConsistencyMonitorService {
    
    private final ReceiptRepository receiptRepository;
    private final StudentServiceClient studentServiceClient;
    
    @Scheduled(fixedRate = 86400000) // Daily check at midnight
    public void validateDataConsistency() {
        log.info("Starting daily data consistency validation");
        
        AtomicInteger inconsistencyCount = new AtomicInteger(0);
        
        try {
            // Check for orphaned receipts (student not found)
            validateOrphanedReceipts(inconsistencyCount);
            
            // Check for student name mismatches
            validateStudentNameConsistency(inconsistencyCount);
            
            // Check for inactive students with recent receipts
            validateInactiveStudentReceipts(inconsistencyCount);
            
            int totalInconsistencies = inconsistencyCount.get();
            if (totalInconsistencies > 0) {
                log.warn("Data consistency validation completed with {} inconsistencies found", 
                    totalInconsistencies);
            } else {
                log.info("Data consistency validation completed successfully - no inconsistencies found");
            }
            
        } catch (Exception e) {
            log.error("Error during data consistency validation", e);
        }
    }
    
    private void validateOrphanedReceipts(AtomicInteger inconsistencyCount) {
        log.info("Validating orphaned receipts");
        
        List<Receipt> allReceipts = receiptRepository.findAll();
        int orphanedCount = 0;
        
        for (Receipt receipt : allReceipts) {
            try {
                StudentDTO student = studentServiceClient.getStudentById(receipt.getStudentId())
                    .orElse(null);
                
                if (student == null) {
                    log.warn("Found orphaned receipt {} for non-existent student {}", 
                        receipt.getId(), receipt.getStudentId());
                    orphanedCount++;
                    inconsistencyCount.incrementAndGet();
                }
            } catch (Exception e) {
                log.error("Error validating receipt {} for student {}", 
                    receipt.getId(), receipt.getStudentId(), e);
            }
        }
        
        if (orphanedCount > 0) {
            log.warn("Found {} orphaned receipts", orphanedCount);
        }
    }
    
    private void validateStudentNameConsistency(AtomicInteger inconsistencyCount) {
        log.info("Validating student name consistency");
        
        List<Receipt> allReceipts = receiptRepository.findAll();
        int nameMismatchCount = 0;
        
        for (Receipt receipt : allReceipts) {
            try {
                StudentDTO student = studentServiceClient.getStudentById(receipt.getStudentId())
                    .orElse(null);
                
                if (student != null && !student.getStudentName().equals(receipt.getStudentName())) {
                    log.warn("Student name mismatch for student {}: receipt has '{}' but current name is '{}'", 
                        receipt.getStudentId(), receipt.getStudentName(), student.getStudentName());
                    nameMismatchCount++;
                    inconsistencyCount.incrementAndGet();
                }
            } catch (Exception e) {
                log.error("Error validating student name for receipt {} student {}", 
                    receipt.getId(), receipt.getStudentId(), e);
            }
        }
        
        if (nameMismatchCount > 0) {
            log.warn("Found {} receipts with student name mismatches", nameMismatchCount);
        }
    }
    
    private void validateInactiveStudentReceipts(AtomicInteger inconsistencyCount) {
        log.info("Validating inactive student receipts");
        
        List<Receipt> recentReceipts = receiptRepository.findRecentReceipts(30); // Last 30 days
        int inactiveStudentReceiptCount = 0;
        
        for (Receipt receipt : recentReceipts) {
            try {
                StudentDTO student = studentServiceClient.getStudentById(receipt.getStudentId())
                    .orElse(null);
                
                if (student != null && !student.getActive()) {
                    log.warn("Found recent receipt {} for inactive student {}", 
                        receipt.getId(), receipt.getStudentId());
                    inactiveStudentReceiptCount++;
                    inconsistencyCount.incrementAndGet();
                }
            } catch (Exception e) {
                log.error("Error validating student status for receipt {} student {}", 
                    receipt.getId(), receipt.getStudentId(), e);
            }
        }
        
        if (inactiveStudentReceiptCount > 0) {
            log.warn("Found {} recent receipts for inactive students", inactiveStudentReceiptCount);
        }
    }
    
    @Scheduled(fixedRate = 3600000) // Hourly sync for critical inconsistencies
    public void autoCorrectCriticalInconsistencies() {
        log.info("Starting automatic correction of critical inconsistencies");
        
        try {
            // Auto-correct student name mismatches
            int correctedCount = correctStudentNameMismatches();
            
            if (correctedCount > 0) {
                log.info("Auto-corrected {} student name mismatches", correctedCount);
            }
            
        } catch (Exception e) {
            log.error("Error during automatic inconsistency correction", e);
        }
    }
    
    private int correctStudentNameMismatches() {
        int correctedCount = 0;
        List<Receipt> allReceipts = receiptRepository.findAll();
        
        for (Receipt receipt : allReceipts) {
            try {
                StudentDTO student = studentServiceClient.getStudentById(receipt.getStudentId())
                    .orElse(null);
                
                if (student != null && !student.getStudentName().equals(receipt.getStudentName())) {
                    // Auto-correct the student name
                    receiptRepository.updateStudentName(receipt.getStudentId(), student.getStudentName());
                    correctedCount++;
                    log.info("Auto-corrected student name for receipt {} student {}: '{}' -> '{}'", 
                        receipt.getId(), receipt.getStudentId(), 
                        receipt.getStudentName(), student.getStudentName());
                }
            } catch (Exception e) {
                log.error("Error auto-correcting receipt {} student {}", 
                    receipt.getId(), receipt.getStudentId(), e);
            }
        }
        
        return correctedCount;
    }
}

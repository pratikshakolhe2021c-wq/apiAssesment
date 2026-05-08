package com.assesment.feeservice.event;

import com.assesment.feeservice.repository.ReceiptRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class FeeServiceEventHandler {
    
    private final ReceiptRepository receiptRepository;
    
    @EventListener
    @Async
    public void handleStudentNameChangeEvent(com.assesment.studentservice.event.StudentUpdatedEvent event) {
        if ("NAME_CHANGE".equals(event.getEventType())) {
            log.info("Handling student name change event for student ID: {}", event.getStudentId());
            
            try {
                int updatedCount = receiptRepository.updateStudentName(
                    event.getStudentId(), event.getNewName());
                log.info("Updated student name in {} receipts", updatedCount);
            } catch (Exception e) {
                log.error("Error updating student name in receipts for student ID: {}", 
                    event.getStudentId(), e);
            }
        }
    }
    
    @EventListener
    @Async
    public void handleStudentStatusChangeEvent(com.assesment.studentservice.event.StudentUpdatedEvent event) {
        if ("STATUS_CHANGE".equals(event.getEventType())) {
            log.info("Handling student status change event for student ID: {}", event.getStudentId());
            
            if (Boolean.FALSE.equals(event.getNewActiveStatus())) {
                // Student was deactivated - log warning for existing receipts
                try {
                    long receiptCount = receiptRepository.countByStudentId(event.getStudentId());
                    if (receiptCount > 0) {
                        log.warn("Student {} was deactivated. {} existing receipts may need review", 
                            event.getStudentId(), receiptCount);
                    }
                } catch (Exception e) {
                    log.error("Error checking receipts for deactivated student ID: {}", 
                        event.getStudentId(), e);
                }
            }
        }
    }
}

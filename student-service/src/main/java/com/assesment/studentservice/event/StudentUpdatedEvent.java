package com.assesment.studentservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentUpdatedEvent {
    private Long studentId;
    private String studentIdNumber;
    private String oldName;
    private String newName;
    private Boolean oldActiveStatus;
    private Boolean newActiveStatus;
    private LocalDateTime timestamp;
    private String eventType;
    
    public static StudentUpdatedEvent forNameChange(Long studentId, String studentIdNumber, 
                                                   String oldName, String newName) {
        return new StudentUpdatedEvent(
            studentId, studentIdNumber, oldName, newName,
            null, null, LocalDateTime.now(), "NAME_CHANGE"
        );
    }
    
    public static StudentUpdatedEvent forStatusChange(Long studentId, String studentIdNumber,
                                                      Boolean oldStatus, Boolean newStatus) {
        return new StudentUpdatedEvent(
            studentId, studentIdNumber, null, null,
            oldStatus, newStatus, LocalDateTime.now(), "STATUS_CHANGE"
        );
    }
}

package com.assesment.studentservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {
    
    private Long id;
    
    @NotBlank(message = "Student name is required")
    @Size(max = 100, message = "Student name must not exceed 100 characters")
    private String studentName;
    
    @NotBlank(message = "Student ID is required")
    @Size(max = 50, message = "Student ID must not exceed 50 characters")
    private String studentId;
    
    @NotBlank(message = "Grade is required")
    @Size(max = 20, message = "Grade must not exceed 20 characters")
    private String grade;
    
    @NotBlank(message = "Mobile number is required")
    @Size(max = 20, message = "Mobile number must not exceed 20 characters")
    private String mobileNumber;
    
    @NotBlank(message = "School name is required")
    @Size(max = 200, message = "School name must not exceed 200 characters")
    private String schoolName;
    
    private Boolean active;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}

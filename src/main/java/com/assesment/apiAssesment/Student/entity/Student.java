package com.assesment.apiAssesment.Student.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "students")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Student name is required")
    @Size(max = 100, message = "Student name must not exceed 100 characters")
    @Column(name = "student_name", nullable = false)
    private String studentName;
    
    @NotBlank(message = "Student ID is required")
    @Size(max = 50, message = "Student ID must not exceed 50 characters")
    @Column(name = "student_id", nullable = false, unique = true)
    private String studentId;
    
    @NotBlank(message = "Grade is required")
    @Size(max = 20, message = "Grade must not exceed 20 characters")
    @Column(name = "grade", nullable = false)
    private String grade;
    
    @NotBlank(message = "Mobile number is required")
    @Size(max = 20, message = "Mobile number must not exceed 20 characters")
    @Column(name = "mobile_number", nullable = false)
    private String mobileNumber;
    
    @NotBlank(message = "School name is required")
    @Size(max = 200, message = "School name must not exceed 200 characters")
    @Column(name = "school_name", nullable = false)
    private String schoolName;
    
    @Column(name = "active", nullable = false)
    private Boolean active = true;
}

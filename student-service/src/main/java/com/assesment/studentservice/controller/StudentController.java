package com.assesment.studentservice.controller;

import com.assesment.studentservice.dto.StudentDTO;
import com.assesment.studentservice.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Student Management", description = "APIs for managing student information")
public class StudentController {
    
    private final StudentService studentService;
    
    @PostMapping
    @Operation(summary = "Create a new student", description = "Add a new student to the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Student created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "409", description = "Student with given ID already exists")
    })
    public ResponseEntity<StudentDTO> createStudent(@Valid @RequestBody StudentDTO studentDTO) {
        log.info("REST request to create student: {}", studentDTO);
        StudentDTO createdStudent = studentService.createStudent(studentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStudent);
    }
    
    @GetMapping
    @Operation(summary = "Get all students", description = "Retrieve a list of all students")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Students retrieved successfully")
    })
    public ResponseEntity<List<StudentDTO>> getAllStudents() {
        log.info("REST request to get all students");
        List<StudentDTO> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get student by ID", description = "Retrieve a specific student by their database ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Student found"),
        @ApiResponse(responseCode = "404", description = "Student not found")
    })
    public ResponseEntity<StudentDTO> getStudentById(
            @Parameter(description = "Student database ID") @PathVariable Long id) {
        log.info("REST request to get student by ID: {}", id);
        Optional<StudentDTO> student = studentService.getStudentById(id);
        return student.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/by-student-id/{studentId}")
    @Operation(summary = "Get student by student ID", description = "Retrieve a specific student by their student ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Student found"),
        @ApiResponse(responseCode = "404", description = "Student not found")
    })
    public ResponseEntity<StudentDTO> getStudentByStudentId(
            @Parameter(description = "Student ID") @PathVariable String studentId) {
        log.info("REST request to get student by student ID: {}", studentId);
        Optional<StudentDTO> student = studentService.getStudentByStudentId(studentId);
        return student.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update student", description = "Update an existing student's information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Student updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Student not found"),
        @ApiResponse(responseCode = "409", description = "Student with given ID already exists")
    })
    public ResponseEntity<StudentDTO> updateStudent(
            @Parameter(description = "Student database ID") @PathVariable Long id,
            @Valid @RequestBody StudentDTO studentDTO) {
        log.info("REST request to update student with ID: {}", id);
        StudentDTO updatedStudent = studentService.updateStudent(id, studentDTO);
        return ResponseEntity.ok(updatedStudent);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete student", description = "Delete a student from the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Student deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Student not found")
    })
    public ResponseEntity<Void> deleteStudent(
            @Parameter(description = "Student database ID") @PathVariable Long id) {
        log.info("REST request to delete student with ID: {}", id);
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate student", description = "Deactivate a student (soft delete)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Student deactivated successfully"),
        @ApiResponse(responseCode = "404", description = "Student not found")
    })
    public ResponseEntity<StudentDTO> deactivateStudent(
            @Parameter(description = "Student database ID") @PathVariable Long id) {
        log.info("REST request to deactivate student with ID: {}", id);
        StudentDTO deactivatedStudent = studentService.deactivateStudent(id);
        return ResponseEntity.ok(deactivatedStudent);
    }
    
    @GetMapping("/search")
    @Operation(summary = "Search students", description = "Search students by name, ID, or mobile number")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Search completed successfully")
    })
    public ResponseEntity<List<StudentDTO>> searchStudents(
            @Parameter(description = "Search term") @RequestParam String search) {
        log.info("REST request to search students with term: {}", search);
        List<StudentDTO> students = studentService.searchStudents(search);
        return ResponseEntity.ok(students);
    }
    
    @GetMapping("/by-grade/{grade}")
    @Operation(summary = "Get students by grade", description = "Retrieve all students in a specific grade")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Students retrieved successfully")
    })
    public ResponseEntity<List<StudentDTO>> getStudentsByGrade(
            @Parameter(description = "Grade") @PathVariable String grade) {
        log.info("REST request to get students by grade: {}", grade);
        List<StudentDTO> students = studentService.getStudentsByGrade(grade);
        return ResponseEntity.ok(students);
    }
    
    @GetMapping("/active")
    @Operation(summary = "Get active students", description = "Retrieve all active students")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Active students retrieved successfully")
    })
    public ResponseEntity<List<StudentDTO>> getActiveStudents() {
        log.info("REST request to get active students");
        List<StudentDTO> students = studentService.getActiveStudents();
        return ResponseEntity.ok(students);
    }
}

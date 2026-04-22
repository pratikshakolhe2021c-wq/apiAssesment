package com.assesment.feeservice.client;

import com.assesment.feeservice.dto.StudentDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "student-service", url = "${student-service.url}")
public interface StudentServiceClient {
    
    @GetMapping("/api/v1/students/{id}")
    @CircuitBreaker(name = "studentService", fallbackMethod = "fallbackStudent")
    Optional<StudentDTO> getStudentById(@PathVariable("id") Long id);
    
    @GetMapping("/api/v1/students/by-student-id/{studentId}")
    @CircuitBreaker(name = "studentService", fallbackMethod = "fallbackStudentByStudentId")
    Optional<StudentDTO> getStudentByStudentId(@PathVariable("studentId") String studentId);
    
    default Optional<StudentDTO> fallbackStudent(Long id, Exception exception) {
        return Optional.empty();
    }
    
    default Optional<StudentDTO> fallbackStudentByStudentId(String studentId, Exception exception) {
        return Optional.empty();
    }
}

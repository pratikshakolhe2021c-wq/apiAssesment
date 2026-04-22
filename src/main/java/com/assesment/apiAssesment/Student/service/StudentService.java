package com.assesment.apiAssesment.Student.service;

import com.assesment.apiAssesment.Student.dto.StudentDTO;
import com.assesment.apiAssesment.Student.entity.Student;
import com.assesment.apiAssesment.Student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class StudentService {
    
    private final StudentRepository studentRepository;
    
    public StudentDTO createStudent(StudentDTO studentDTO) {
        log.info("Creating new student with ID: {}", studentDTO.getStudentId());
        
        if (studentRepository.existsByStudentId(studentDTO.getStudentId())) {
            throw new RuntimeException("Student with ID " + studentDTO.getStudentId() + " already exists");
        }
        
        Student student = convertToEntity(studentDTO);
        Student savedStudent = studentRepository.save(student);
        log.info("Student created successfully with ID: {}", savedStudent.getId());
        return convertToDTO(savedStudent);
    }
    
    @Transactional(readOnly = true)
    public List<StudentDTO> getAllStudents() {
        log.info("Retrieving all students");
        return studentRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public Optional<StudentDTO> getStudentById(Long id) {
        log.info("Retrieving student with ID: {}", id);
        return studentRepository.findById(id)
                .map(this::convertToDTO);
    }
    
    @Transactional(readOnly = true)
    public Optional<StudentDTO> getStudentByStudentId(String studentId) {
        log.info("Retrieving student with student ID: {}", studentId);
        return studentRepository.findByStudentId(studentId)
                .map(this::convertToDTO);
    }
    
    public StudentDTO updateStudent(Long id, StudentDTO studentDTO) {
        log.info("Updating student with ID: {}", id);
        
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + id));
        
        if (!existingStudent.getStudentId().equals(studentDTO.getStudentId()) &&
            studentRepository.existsByStudentId(studentDTO.getStudentId())) {
            throw new RuntimeException("Student with ID " + studentDTO.getStudentId() + " already exists");
        }
        
        updateEntityFromDTO(existingStudent, studentDTO);
        Student updatedStudent = studentRepository.save(existingStudent);
        log.info("Student updated successfully with ID: {}", updatedStudent.getId());
        return convertToDTO(updatedStudent);
    }
    
    public void deleteStudent(Long id) {
        log.info("Deleting student with ID: {}", id);
        
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + id));
        
        studentRepository.delete(student);
        log.info("Student deleted successfully with ID: {}", id);
    }
    
    public StudentDTO deactivateStudent(Long id) {
        log.info("Deactivating student with ID: {}", id);
        
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + id));
        
        student.setActive(false);
        Student updatedStudent = studentRepository.save(student);
        log.info("Student deactivated successfully with ID: {}", updatedStudent.getId());
        return convertToDTO(updatedStudent);
    }
    
    @Transactional(readOnly = true)
    public List<StudentDTO> searchStudents(String search) {
        log.info("Searching students with term: {}", search);
        return studentRepository.searchStudents(search).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<StudentDTO> getStudentsByGrade(String grade) {
        log.info("Retrieving students by grade: {}", grade);
        return studentRepository.findByGrade(grade).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<StudentDTO> getActiveStudents() {
        log.info("Retrieving active students");
        return studentRepository.findByActiveTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    private Student convertToEntity(StudentDTO dto) {
        Student student = new Student();
        student.setStudentName(dto.getStudentName());
        student.setStudentId(dto.getStudentId());
        student.setGrade(dto.getGrade());
        student.setMobileNumber(dto.getMobileNumber());
        student.setSchoolName(dto.getSchoolName());
        student.setActive(dto.getActive() != null ? dto.getActive() : true);
        return student;
    }
    
    private StudentDTO convertToDTO(Student entity) {
        StudentDTO dto = new StudentDTO();
        dto.setId(entity.getId());
        dto.setStudentName(entity.getStudentName());
        dto.setStudentId(entity.getStudentId());
        dto.setGrade(entity.getGrade());
        dto.setMobileNumber(entity.getMobileNumber());
        dto.setSchoolName(entity.getSchoolName());
        dto.setActive(entity.getActive());
        return dto;
    }
    
    private void updateEntityFromDTO(Student entity, StudentDTO dto) {
        entity.setStudentName(dto.getStudentName());
        entity.setStudentId(dto.getStudentId());
        entity.setGrade(dto.getGrade());
        entity.setMobileNumber(dto.getMobileNumber());
        entity.setSchoolName(dto.getSchoolName());
        if (dto.getActive() != null) {
            entity.setActive(dto.getActive());
        }
    }
}

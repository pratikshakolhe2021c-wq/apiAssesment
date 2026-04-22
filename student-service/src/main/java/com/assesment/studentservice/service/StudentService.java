package com.assesment.studentservice.service;

import com.assesment.studentservice.dto.StudentDTO;
import com.assesment.studentservice.entity.Student;
import com.assesment.studentservice.exception.StudentAlreadyExistsException;
import com.assesment.studentservice.exception.StudentNotFoundException;
import com.assesment.studentservice.mapper.StudentMapper;
import com.assesment.studentservice.repository.StudentRepository;
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
public class StudentService {
    
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    
    @Transactional
    public StudentDTO createStudent(StudentDTO studentDTO) {
        log.info("Creating new student with studentId: {}", studentDTO.getStudentId());
        
        if (studentRepository.existsByStudentId(studentDTO.getStudentId())) {
            throw new StudentAlreadyExistsException(
                "Student with ID " + studentDTO.getStudentId() + " already exists");
        }
        
        Student student = studentMapper.toEntity(studentDTO);
        Student savedStudent = studentRepository.save(student);
        
        log.info("Successfully created student with ID: {}", savedStudent.getId());
        return studentMapper.toDTO(savedStudent);
    }
    
    public List<StudentDTO> getAllStudents() {
        log.info("Fetching all students");
        List<Student> students = studentRepository.findAll();
        return students.stream()
                .map(studentMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    public Optional<StudentDTO> getStudentById(Long id) {
        log.info("Fetching student by ID: {}", id);
        return studentRepository.findById(id)
                .map(studentMapper::toDTO);
    }
    
    public Optional<StudentDTO> getStudentByStudentId(String studentId) {
        log.info("Fetching student by studentId: {}", studentId);
        return studentRepository.findByStudentId(studentId)
                .map(studentMapper::toDTO);
    }
    
    @Transactional
    public StudentDTO updateStudent(Long id, StudentDTO studentDTO) {
        log.info("Updating student with ID: {}", id);
        
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with ID: " + id));
        
        if (!existingStudent.getStudentId().equals(studentDTO.getStudentId()) &&
            studentRepository.existsByStudentId(studentDTO.getStudentId())) {
            throw new StudentAlreadyExistsException(
                "Student with ID " + studentDTO.getStudentId() + " already exists");
        }
        
        studentMapper.updateEntityFromDTO(studentDTO, existingStudent);
        Student updatedStudent = studentRepository.save(existingStudent);
        
        log.info("Successfully updated student with ID: {}", updatedStudent.getId());
        return studentMapper.toDTO(updatedStudent);
    }
    
    @Transactional
    public void deleteStudent(Long id) {
        log.info("Deleting student with ID: {}", id);
        
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with ID: " + id));
        
        studentRepository.delete(student);
        log.info("Successfully deleted student with ID: {}", id);
    }
    
    @Transactional
    public StudentDTO deactivateStudent(Long id) {
        log.info("Deactivating student with ID: {}", id);
        
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with ID: " + id));
        
        student.setActive(false);
        Student deactivatedStudent = studentRepository.save(student);
        
        log.info("Successfully deactivated student with ID: {}", deactivatedStudent.getId());
        return studentMapper.toDTO(deactivatedStudent);
    }
    
    public List<StudentDTO> searchStudents(String search) {
        log.info("Searching students with term: {}", search);
        List<Student> students = studentRepository.searchStudents(search);
        return students.stream()
                .map(studentMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<StudentDTO> getStudentsByGrade(String grade) {
        log.info("Fetching students by grade: {}", grade);
        List<Student> students = studentRepository.findByGradeAndActive(grade);
        return students.stream()
                .map(studentMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<StudentDTO> getActiveStudents() {
        log.info("Fetching all active students");
        List<Student> students = studentRepository.findActiveStudents();
        return students.stream()
                .map(studentMapper::toDTO)
                .collect(Collectors.toList());
    }
}

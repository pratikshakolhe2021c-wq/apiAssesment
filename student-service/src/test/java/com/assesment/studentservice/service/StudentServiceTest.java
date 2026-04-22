package com.assesment.studentservice.service;

import com.assesment.studentservice.dto.StudentDTO;
import com.assesment.studentservice.entity.Student;
import com.assesment.studentservice.exception.StudentAlreadyExistsException;
import com.assesment.studentservice.exception.StudentNotFoundException;
import com.assesment.studentservice.mapper.StudentMapper;
import com.assesment.studentservice.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {
    
    @Mock
    private StudentRepository studentRepository;
    
    @Mock
    private StudentMapper studentMapper;
    
    @InjectMocks
    private StudentService studentService;
    
    private Student student;
    private StudentDTO studentDTO;
    
    @BeforeEach
    void setUp() {
        student = new Student();
        student.setId(1L);
        student.setStudentName("John Doe");
        student.setStudentId("STU001");
        student.setGrade("10A");
        student.setMobileNumber("1234567890");
        student.setSchoolName("ABC School");
        student.setActive(true);
        student.setCreatedAt(LocalDateTime.now());
        student.setUpdatedAt(LocalDateTime.now());
        
        studentDTO = new StudentDTO();
        studentDTO.setId(1L);
        studentDTO.setStudentName("John Doe");
        studentDTO.setStudentId("STU001");
        studentDTO.setGrade("10A");
        studentDTO.setMobileNumber("1234567890");
        studentDTO.setSchoolName("ABC School");
        studentDTO.setActive(true);
        studentDTO.setCreatedAt(LocalDateTime.now());
        studentDTO.setUpdatedAt(LocalDateTime.now());
    }
    
    @Test
    void createStudent_ShouldReturnStudentDTO_WhenValidInput() {
        when(studentRepository.existsByStudentId("STU001")).thenReturn(false);
        when(studentMapper.toEntity(studentDTO)).thenReturn(student);
        when(studentRepository.save(student)).thenReturn(student);
        when(studentMapper.toDTO(student)).thenReturn(studentDTO);
        
        StudentDTO result = studentService.createStudent(studentDTO);
        
        assertNotNull(result);
        assertEquals("STU001", result.getStudentId());
        verify(studentRepository).save(student);
    }
    
    @Test
    void createStudent_ShouldThrowException_WhenStudentIdExists() {
        when(studentRepository.existsByStudentId("STU001")).thenReturn(true);
        
        assertThrows(StudentAlreadyExistsException.class, () -> studentService.createStudent(studentDTO));
        verify(studentRepository, never()).save(any());
    }
    
    @Test
    void getAllStudents_ShouldReturnListOfStudentDTOs() {
        List<Student> students = Arrays.asList(student);
        when(studentRepository.findAll()).thenReturn(students);
        when(studentMapper.toDTO(student)).thenReturn(studentDTO);
        
        List<StudentDTO> result = studentService.getAllStudents();
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("STU001", result.get(0).getStudentId());
    }
    
    @Test
    void getStudentById_ShouldReturnStudentDTO_WhenStudentExists() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentMapper.toDTO(student)).thenReturn(studentDTO);
        
        Optional<StudentDTO> result = studentService.getStudentById(1L);
        
        assertTrue(result.isPresent());
        assertEquals("STU001", result.get().getStudentId());
    }
    
    @Test
    void getStudentById_ShouldReturnEmpty_WhenStudentNotExists() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());
        
        Optional<StudentDTO> result = studentService.getStudentById(1L);
        
        assertFalse(result.isPresent());
    }
    
    @Test
    void getStudentByStudentId_ShouldReturnStudentDTO_WhenStudentExists() {
        when(studentRepository.findByStudentId("STU001")).thenReturn(Optional.of(student));
        when(studentMapper.toDTO(student)).thenReturn(studentDTO);
        
        Optional<StudentDTO> result = studentService.getStudentByStudentId("STU001");
        
        assertTrue(result.isPresent());
        assertEquals("STU001", result.get().getStudentId());
    }
    
    @Test
    void updateStudent_ShouldReturnUpdatedStudentDTO_WhenStudentExists() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentRepository.existsByStudentId("STU001")).thenReturn(false);
        when(studentRepository.save(student)).thenReturn(student);
        when(studentMapper.toDTO(student)).thenReturn(studentDTO);
        
        StudentDTO result = studentService.updateStudent(1L, studentDTO);
        
        assertNotNull(result);
        assertEquals("STU001", result.getStudentId());
        verify(studentRepository).save(student);
    }
    
    @Test
    void updateStudent_ShouldThrowException_WhenStudentNotExists() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());
        
        assertThrows(StudentNotFoundException.class, () -> studentService.updateStudent(1L, studentDTO));
        verify(studentRepository, never()).save(any());
    }
    
    @Test
    void deleteStudent_ShouldDeleteStudent_WhenStudentExists() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        
        studentService.deleteStudent(1L);
        
        verify(studentRepository).delete(student);
    }
    
    @Test
    void deleteStudent_ShouldThrowException_WhenStudentNotExists() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());
        
        assertThrows(StudentNotFoundException.class, () -> studentService.deleteStudent(1L));
        verify(studentRepository, never()).delete(any());
    }
    
    @Test
    void deactivateStudent_ShouldReturnDeactivatedStudentDTO_WhenStudentExists() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentRepository.save(student)).thenReturn(student);
        when(studentMapper.toDTO(student)).thenReturn(studentDTO);
        
        StudentDTO result = studentService.deactivateStudent(1L);
        
        assertNotNull(result);
        verify(studentRepository).save(student);
        assertFalse(student.getActive());
    }
    
    @Test
    void searchStudents_ShouldReturnMatchingStudents() {
        List<Student> students = Arrays.asList(student);
        when(studentRepository.searchStudents("John")).thenReturn(students);
        when(studentMapper.toDTO(student)).thenReturn(studentDTO);
        
        List<StudentDTO> result = studentService.searchStudents("John");
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("STU001", result.get(0).getStudentId());
    }
    
    @Test
    void getStudentsByGrade_ShouldReturnStudentsInGrade() {
        List<Student> students = Arrays.asList(student);
        when(studentRepository.findByGradeAndActive("10A")).thenReturn(students);
        when(studentMapper.toDTO(student)).thenReturn(studentDTO);
        
        List<StudentDTO> result = studentService.getStudentsByGrade("10A");
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("10A", result.get(0).getGrade());
    }
    
    @Test
    void getActiveStudents_ShouldReturnActiveStudents() {
        List<Student> students = Arrays.asList(student);
        when(studentRepository.findActiveStudents()).thenReturn(students);
        when(studentMapper.toDTO(student)).thenReturn(studentDTO);
        
        List<StudentDTO> result = studentService.getActiveStudents();
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getActive());
    }
}

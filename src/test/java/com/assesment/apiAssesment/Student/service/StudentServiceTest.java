package com.assesment.apiAssesment.Student.service;

import com.assesment.apiAssesment.Student.dto.StudentDTO;
import com.assesment.apiAssesment.Student.entity.Student;
import com.assesment.apiAssesment.Student.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

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
        student.setGrade("Grade 10");
        student.setMobileNumber("+1234567890");
        student.setSchoolName("Springfield High School");
        student.setActive(true);

        studentDTO = new StudentDTO();
        studentDTO.setId(1L);
        studentDTO.setStudentName("John Doe");
        studentDTO.setStudentId("STU001");
        studentDTO.setGrade("Grade 10");
        studentDTO.setMobileNumber("+1234567890");
        studentDTO.setSchoolName("Springfield High School");
        studentDTO.setActive(true);
    }

    @Test
    void createStudent_ShouldReturnStudentDTO_WhenStudentDoesNotExist() {
        when(studentRepository.existsByStudentId("STU001")).thenReturn(false);
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        StudentDTO result = studentService.createStudent(studentDTO);

        assertNotNull(result);
        assertEquals("John Doe", result.getStudentName());
        assertEquals("STU001", result.getStudentId());
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void createStudent_ShouldThrowException_WhenStudentExists() {
        when(studentRepository.existsByStudentId("STU001")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> studentService.createStudent(studentDTO));
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    void getAllStudents_ShouldReturnListOfStudents() {
        List<Student> students = Arrays.asList(student);
        when(studentRepository.findAll()).thenReturn(students);

        List<StudentDTO> result = studentService.getAllStudents();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getStudentName());
    }

    @Test
    void getStudentById_ShouldReturnStudentDTO_WhenStudentExists() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        Optional<StudentDTO> result = studentService.getStudentById(1L);

        assertTrue(result.isPresent());
        assertEquals("John Doe", result.get().getStudentName());
    }

    @Test
    void getStudentById_ShouldReturnEmpty_WhenStudentDoesNotExist() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<StudentDTO> result = studentService.getStudentById(1L);

        assertFalse(result.isPresent());
    }

    @Test
    void getStudentByStudentId_ShouldReturnStudentDTO_WhenStudentExists() {
        when(studentRepository.findByStudentId("STU001")).thenReturn(Optional.of(student));

        Optional<StudentDTO> result = studentService.getStudentByStudentId("STU001");

        assertTrue(result.isPresent());
        assertEquals("John Doe", result.get().getStudentName());
    }

    @Test
    void updateStudent_ShouldReturnUpdatedStudentDTO_WhenStudentExists() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        StudentDTO result = studentService.updateStudent(1L, studentDTO);

        assertNotNull(result);
        assertEquals("John Doe", result.getStudentName());
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void updateStudent_ShouldThrowException_WhenStudentDoesNotExist() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> studentService.updateStudent(1L, studentDTO));
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    void deleteStudent_ShouldDeleteStudent_WhenStudentExists() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        studentService.deleteStudent(1L);

        verify(studentRepository).delete(student);
    }

    @Test
    void deleteStudent_ShouldThrowException_WhenStudentDoesNotExist() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> studentService.deleteStudent(1L));
        verify(studentRepository, never()).delete(any(Student.class));
    }

    @Test
    void deactivateStudent_ShouldReturnDeactivatedStudentDTO_WhenStudentExists() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        StudentDTO result = studentService.deactivateStudent(1L);

        assertNotNull(result);
        assertFalse(result.getActive());
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void searchStudents_ShouldReturnListOfStudents() {
        List<Student> students = Arrays.asList(student);
        when(studentRepository.searchStudents("John")).thenReturn(students);

        List<StudentDTO> result = studentService.searchStudents("John");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getStudentName());
    }

    @Test
    void getStudentsByGrade_ShouldReturnListOfStudents() {
        List<Student> students = Arrays.asList(student);
        when(studentRepository.findByGrade("Grade 10")).thenReturn(students);

        List<StudentDTO> result = studentService.getStudentsByGrade("Grade 10");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Grade 10", result.get(0).getGrade());
    }

    @Test
    void getActiveStudents_ShouldReturnListOfActiveStudents() {
        List<Student> students = Arrays.asList(student);
        when(studentRepository.findByActiveTrue()).thenReturn(students);

        List<StudentDTO> result = studentService.getActiveStudents();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getActive());
    }
}

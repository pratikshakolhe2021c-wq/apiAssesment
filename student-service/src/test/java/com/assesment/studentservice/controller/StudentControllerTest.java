package com.assesment.studentservice.controller;

import com.assesment.studentservice.dto.StudentDTO;
import com.assesment.studentservice.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
class StudentControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private StudentService studentService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private StudentDTO studentDTO;
    
    @BeforeEach
    void setUp() {
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
    void createStudent_ShouldReturnCreated_WhenValidInput() throws Exception {
        when(studentService.createStudent(any(StudentDTO.class))).thenReturn(studentDTO);
        
        mockMvc.perform(post("/api/v1/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.studentId").value("STU001"))
                .andExpect(jsonPath("$.studentName").value("John Doe"));
    }
    
    @Test
    void getAllStudents_ShouldReturnListOfStudents() throws Exception {
        List<StudentDTO> students = Arrays.asList(studentDTO);
        when(studentService.getAllStudents()).thenReturn(students);
        
        mockMvc.perform(get("/api/v1/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].studentId").value("STU001"))
                .andExpect(jsonPath("$[0].studentName").value("John Doe"));
    }
    
    @Test
    void getStudentById_ShouldReturnStudent_WhenStudentExists() throws Exception {
        when(studentService.getStudentById(1L)).thenReturn(Optional.of(studentDTO));
        
        mockMvc.perform(get("/api/v1/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentId").value("STU001"))
                .andExpect(jsonPath("$.studentName").value("John Doe"));
    }
    
    @Test
    void getStudentById_ShouldReturnNotFound_WhenStudentNotExists() throws Exception {
        when(studentService.getStudentById(1L)).thenReturn(Optional.empty());
        
        mockMvc.perform(get("/api/v1/students/1"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void getStudentByStudentId_ShouldReturnStudent_WhenStudentExists() throws Exception {
        when(studentService.getStudentByStudentId("STU001")).thenReturn(Optional.of(studentDTO));
        
        mockMvc.perform(get("/api/v1/students/by-student-id/STU001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentId").value("STU001"))
                .andExpect(jsonPath("$.studentName").value("John Doe"));
    }
    
    @Test
    void getStudentByStudentId_ShouldReturnNotFound_WhenStudentNotExists() throws Exception {
        when(studentService.getStudentByStudentId("STU001")).thenReturn(Optional.empty());
        
        mockMvc.perform(get("/api/v1/students/by-student-id/STU001"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void updateStudent_ShouldReturnUpdatedStudent_WhenStudentExists() throws Exception {
        when(studentService.updateStudent(anyLong(), any(StudentDTO.class))).thenReturn(studentDTO);
        
        mockMvc.perform(put("/api/v1/students/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentId").value("STU001"))
                .andExpect(jsonPath("$.studentName").value("John Doe"));
    }
    
    @Test
    void deleteStudent_ShouldReturnNoContent_WhenStudentExists() throws Exception {
        mockMvc.perform(delete("/api/v1/students/1"))
                .andExpect(status().isNoContent());
    }
    
    @Test
    void deactivateStudent_ShouldReturnDeactivatedStudent_WhenStudentExists() throws Exception {
        when(studentService.deactivateStudent(1L)).thenReturn(studentDTO);
        
        mockMvc.perform(patch("/api/v1/students/1/deactivate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentId").value("STU001"))
                .andExpect(jsonPath("$.active").value(false));
    }
    
    @Test
    void searchStudents_ShouldReturnMatchingStudents() throws Exception {
        List<StudentDTO> students = Arrays.asList(studentDTO);
        when(studentService.searchStudents(anyString())).thenReturn(students);
        
        mockMvc.perform(get("/api/v1/students/search")
                .param("search", "John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].studentId").value("STU001"))
                .andExpect(jsonPath("$[0].studentName").value("John Doe"));
    }
    
    @Test
    void getStudentsByGrade_ShouldReturnStudentsInGrade() throws Exception {
        List<StudentDTO> students = Arrays.asList(studentDTO);
        when(studentService.getStudentsByGrade("10A")).thenReturn(students);
        
        mockMvc.perform(get("/api/v1/students/by-grade/10A"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].studentId").value("STU001"))
                .andExpect(jsonPath("$[0].grade").value("10A"));
    }
    
    @Test
    void getActiveStudents_ShouldReturnActiveStudents() throws Exception {
        List<StudentDTO> students = Arrays.asList(studentDTO);
        when(studentService.getActiveStudents()).thenReturn(students);
        
        mockMvc.perform(get("/api/v1/students/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].studentId").value("STU001"))
                .andExpect(jsonPath("$[0].active").value(true));
    }
}

package com.assesment.studentservice.mapper;

import com.assesment.studentservice.dto.StudentDTO;
import com.assesment.studentservice.entity.Student;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper {
    
    public StudentDTO toDTO(Student student) {
        if (student == null) {
            return null;
        }
        
        StudentDTO dto = new StudentDTO();
        dto.setId(student.getId());
        dto.setStudentName(student.getStudentName());
        dto.setStudentId(student.getStudentId());
        dto.setGrade(student.getGrade());
        dto.setMobileNumber(student.getMobileNumber());
        dto.setSchoolName(student.getSchoolName());
        dto.setActive(student.getActive());
        dto.setCreatedAt(student.getCreatedAt());
        dto.setUpdatedAt(student.getUpdatedAt());
        
        return dto;
    }
    
    public Student toEntity(StudentDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Student student = new Student();
        student.setStudentName(dto.getStudentName());
        student.setStudentId(dto.getStudentId());
        student.setGrade(dto.getGrade());
        student.setMobileNumber(dto.getMobileNumber());
        student.setSchoolName(dto.getSchoolName());
        student.setActive(dto.getActive() != null ? dto.getActive() : true);
        
        return student;
    }
    
    public void updateEntityFromDTO(StudentDTO dto, Student student) {
        if (dto == null || student == null) {
            return;
        }
        
        student.setStudentName(dto.getStudentName());
        student.setStudentId(dto.getStudentId());
        student.setGrade(dto.getGrade());
        student.setMobileNumber(dto.getMobileNumber());
        student.setSchoolName(dto.getSchoolName());
        if (dto.getActive() != null) {
            student.setActive(dto.getActive());
        }
    }
}

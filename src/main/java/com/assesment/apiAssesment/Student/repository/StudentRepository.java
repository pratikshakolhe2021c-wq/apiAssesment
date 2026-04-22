package com.assesment.apiAssesment.Student.repository;

import com.assesment.apiAssesment.Student.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    
    Optional<Student> findByStudentId(String studentId);
    
    List<Student> findByStudentIdContainingIgnoreCase(String studentId);
    
    List<Student> findByStudentNameContainingIgnoreCase(String studentName);
    
    List<Student> findByGrade(String grade);
    
    List<Student> findBySchoolNameContainingIgnoreCase(String schoolName);
    
    List<Student> findByActiveTrue();
    
    List<Student> findByActiveFalse();
    
    @Query("SELECT s FROM Student s WHERE s.studentId LIKE %:search% OR s.studentName LIKE %:search% OR s.mobileNumber LIKE %:search%")
    List<Student> searchStudents(@Param("search") String search);
    
    boolean existsByStudentId(String studentId);
}

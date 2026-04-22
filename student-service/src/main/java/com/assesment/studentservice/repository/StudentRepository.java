package com.assesment.studentservice.repository;

import com.assesment.studentservice.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    
    Optional<Student> findByStudentId(String studentId);
    
    boolean existsByStudentId(String studentId);
    
    @Query("SELECT s FROM Student s WHERE s.active = true")
    List<Student> findActiveStudents();
    
    @Query("SELECT s FROM Student s WHERE s.grade = :grade AND s.active = true")
    List<Student> findByGradeAndActive(@Param("grade") String grade);
    
    @Query("SELECT s FROM Student s WHERE " +
           "LOWER(s.studentName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(s.studentId) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(s.mobileNumber) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Student> searchStudents(@Param("search") String search);
}

package com.assesment.feeservice.repository;

import com.assesment.feeservice.entity.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
    
    Optional<Receipt> findByReceiptNumber(String receiptNumber);
    
    boolean existsByReceiptNumber(String receiptNumber);
    
    List<Receipt> findByStudentId(Long studentId);
    
    List<Receipt> findByAcademicYear(String academicYear);
    
    List<Receipt> findByFeeType(String feeType);
    
    List<Receipt> findByPaymentMethod(String paymentMethod);
    
    @Query("SELECT r FROM Receipt r WHERE r.paymentDate BETWEEN :startDate AND :endDate")
    List<Receipt> findByPaymentDateBetween(@Param("startDate") LocalDateTime startDate, 
                                          @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT COALESCE(SUM(r.amount), 0) FROM Receipt r WHERE r.studentId = :studentId AND r.academicYear = :academicYear")
    BigDecimal getTotalFeesPaidByStudent(@Param("studentId") Long studentId, 
                                        @Param("academicYear") String academicYear);
    
    @Query("SELECT COALESCE(SUM(r.amount), 0) FROM Receipt r WHERE r.academicYear = :academicYear")
    BigDecimal getTotalFeesCollected(@Param("academicYear") String academicYear);
    
    @Query("SELECT r FROM Receipt r WHERE " +
           "LOWER(r.receiptNumber) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(r.studentName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(r.transactionId) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Receipt> searchReceipts(@Param("search") String search);
}

package com.assesment.apiAssesment.Fee.repository;

import com.assesment.apiAssesment.Fee.entity.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
    
    Optional<Receipt> findByReceiptNumber(String receiptNumber);
    
    List<Receipt> findByStudentId(Long studentId);
    
    List<Receipt> findByStudentIdOrderByPaymentDateDesc(Long studentId);
    
    List<Receipt> findByFeeType(String feeType);
    
    List<Receipt> findByAcademicYear(String academicYear);
    
    List<Receipt> findByAcademicYearAndStudentId(String academicYear, Long studentId);
    
    List<Receipt> findByPaymentDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    List<Receipt> findByPaymentMethod(String paymentMethod);
    
    @Query("SELECT r FROM Receipt r WHERE r.studentId = :studentId AND r.feeType = :feeType AND r.academicYear = :academicYear")
    List<Receipt> findByStudentIdAndFeeTypeAndAcademicYear(@Param("studentId") Long studentId, 
                                                          @Param("feeType") String feeType, 
                                                          @Param("academicYear") String academicYear);
    
    @Query("SELECT r FROM Receipt r WHERE r.studentName LIKE %:search% OR r.receiptNumber LIKE %:search% OR r.transactionId LIKE %:search%")
    List<Receipt> searchReceipts(@Param("search") String search);
    
    @Query("SELECT SUM(r.amount) FROM Receipt r WHERE r.studentId = :studentId AND r.academicYear = :academicYear")
    java.math.BigDecimal getTotalFeesPaidByStudentForYear(@Param("studentId") Long studentId, 
                                                         @Param("academicYear") String academicYear);
    
    @Query("SELECT SUM(r.amount) FROM Receipt r WHERE r.academicYear = :academicYear")
    java.math.BigDecimal getTotalFeesCollectedForYear(@Param("academicYear") String academicYear);
    
    @Query("SELECT COUNT(r) FROM Receipt r WHERE r.paymentDate BETWEEN :startDate AND :endDate")
    Long countReceiptsBetweenDates(@Param("startDate") LocalDateTime startDate, 
                                  @Param("endDate") LocalDateTime endDate);
    
    boolean existsByReceiptNumber(String receiptNumber);
}

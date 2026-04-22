package com.assesment.feeservice.service;

import com.assesment.feeservice.client.StudentServiceClient;
import com.assesment.feeservice.dto.ReceiptDTO;
import com.assesment.feeservice.dto.StudentDTO;
import com.assesment.feeservice.entity.Receipt;
import com.assesment.feeservice.exception.ReceiptAlreadyExistsException;
import com.assesment.feeservice.exception.StudentNotFoundException;
import com.assesment.feeservice.mapper.ReceiptMapper;
import com.assesment.feeservice.repository.ReceiptRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeeCollectionServiceTest {
    
    @Mock
    private ReceiptRepository receiptRepository;
    
    @Mock
    private ReceiptMapper receiptMapper;
    
    @Mock
    private StudentServiceClient studentServiceClient;
    
    @Mock
    private CircuitBreakerFactory circuitBreakerFactory;
    
    @Mock
    private CircuitBreaker circuitBreaker;
    
    @InjectMocks
    private FeeCollectionService feeCollectionService;
    
    private Receipt receipt;
    private ReceiptDTO receiptDTO;
    private StudentDTO studentDTO;
    
    @BeforeEach
    void setUp() {
        receipt = new Receipt();
        receipt.setId(1L);
        receipt.setReceiptNumber("RCP-12345678");
        receipt.setStudentId(1L);
        receipt.setStudentName("John Doe");
        receipt.setAcademicYear("2023-24");
        receipt.setFeeType("Tuition");
        receipt.setAmount(new BigDecimal("10000.00"));
        receipt.setPaymentMethod("Cash");
        receipt.setTransactionId("TXN123456");
        receipt.setPaymentDate(LocalDateTime.now());
        receipt.setNotes("Monthly fee");
        receipt.setCreatedAt(LocalDateTime.now());
        receipt.setUpdatedAt(LocalDateTime.now());
        
        receiptDTO = new ReceiptDTO();
        receiptDTO.setId(1L);
        receiptDTO.setReceiptNumber("RCP-12345678");
        receiptDTO.setStudentId(1L);
        receiptDTO.setStudentName("John Doe");
        receiptDTO.setAcademicYear("2023-24");
        receiptDTO.setFeeType("Tuition");
        receiptDTO.setAmount(new BigDecimal("10000.00"));
        receiptDTO.setPaymentMethod("Cash");
        receiptDTO.setTransactionId("TXN123456");
        receiptDTO.setPaymentDate(LocalDateTime.now());
        receiptDTO.setNotes("Monthly fee");
        receiptDTO.setCreatedAt(LocalDateTime.now());
        receiptDTO.setUpdatedAt(LocalDateTime.now());
        
        studentDTO = new StudentDTO();
        studentDTO.setId(1L);
        studentDTO.setStudentName("John Doe");
        studentDTO.setStudentId("STU001");
        studentDTO.setGrade("10A");
        studentDTO.setMobileNumber("1234567890");
        studentDTO.setSchoolName("ABC School");
        studentDTO.setActive(true);
        
        when(circuitBreakerFactory.create(anyString())).thenReturn(circuitBreaker);
    }
    
    @Test
    void collectFee_ShouldReturnReceiptDTO_WhenValidInput() {
        when(receiptRepository.existsByReceiptNumber("RCP-12345678")).thenReturn(false);
        when(circuitBreaker.run(any(), any())).thenReturn(studentDTO);
        when(receiptMapper.toEntity(receiptDTO)).thenReturn(receipt);
        when(receiptRepository.save(receipt)).thenReturn(receipt);
        when(receiptMapper.toDTO(receipt)).thenReturn(receiptDTO);
        
        ReceiptDTO result = feeCollectionService.collectFee(receiptDTO);
        
        assertNotNull(result);
        assertEquals("RCP-12345678", result.getReceiptNumber());
        assertEquals(new BigDecimal("10000.00"), result.getAmount());
        verify(receiptRepository).save(receipt);
    }
    
    @Test
    void collectFee_ShouldThrowException_WhenReceiptNumberExists() {
        when(receiptRepository.existsByReceiptNumber("RCP-12345678")).thenReturn(true);
        
        assertThrows(ReceiptAlreadyExistsException.class, () -> feeCollectionService.collectFee(receiptDTO));
        verify(receiptRepository, never()).save(any());
    }
    
    @Test
    void collectFee_ShouldThrowException_WhenStudentNotFound() {
        when(receiptRepository.existsByReceiptNumber("RCP-12345678")).thenReturn(false);
        when(circuitBreaker.run(any(), any())).thenThrow(new StudentNotFoundException("Student not found"));
        
        assertThrows(StudentNotFoundException.class, () -> feeCollectionService.collectFee(receiptDTO));
        verify(receiptRepository, never()).save(any());
    }
    
    @Test
    void getAllReceipts_ShouldReturnListOfReceiptDTOs() {
        List<Receipt> receipts = Arrays.asList(receipt);
        when(receiptRepository.findAll()).thenReturn(receipts);
        when(receiptMapper.toDTO(receipt)).thenReturn(receiptDTO);
        
        List<ReceiptDTO> result = feeCollectionService.getAllReceipts();
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("RCP-12345678", result.get(0).getReceiptNumber());
    }
    
    @Test
    void getReceiptById_ShouldReturnReceiptDTO_WhenReceiptExists() {
        when(receiptRepository.findById(1L)).thenReturn(Optional.of(receipt));
        when(receiptMapper.toDTO(receipt)).thenReturn(receiptDTO);
        
        Optional<ReceiptDTO> result = feeCollectionService.getReceiptById(1L);
        
        assertTrue(result.isPresent());
        assertEquals("RCP-12345678", result.get().getReceiptNumber());
    }
    
    @Test
    void getReceiptById_ShouldReturnEmpty_WhenReceiptNotExists() {
        when(receiptRepository.findById(1L)).thenReturn(Optional.empty());
        
        Optional<ReceiptDTO> result = feeCollectionService.getReceiptById(1L);
        
        assertFalse(result.isPresent());
    }
    
    @Test
    void getReceiptByReceiptNumber_ShouldReturnReceiptDTO_WhenReceiptExists() {
        when(receiptRepository.findByReceiptNumber("RCP-12345678")).thenReturn(Optional.of(receipt));
        when(receiptMapper.toDTO(receipt)).thenReturn(receiptDTO);
        
        Optional<ReceiptDTO> result = feeCollectionService.getReceiptByReceiptNumber("RCP-12345678");
        
        assertTrue(result.isPresent());
        assertEquals("RCP-12345678", result.get().getReceiptNumber());
    }
    
    @Test
    void getReceiptsByStudentId_ShouldReturnReceiptsForStudent() {
        List<Receipt> receipts = Arrays.asList(receipt);
        when(receiptRepository.findByStudentId(1L)).thenReturn(receipts);
        when(receiptMapper.toDTO(receipt)).thenReturn(receiptDTO);
        
        List<ReceiptDTO> result = feeCollectionService.getReceiptsByStudentId(1L);
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getStudentId());
    }
    
    @Test
    void getReceiptsByAcademicYear_ShouldReturnReceiptsForYear() {
        List<Receipt> receipts = Arrays.asList(receipt);
        when(receiptRepository.findByAcademicYear("2023-24")).thenReturn(receipts);
        when(receiptMapper.toDTO(receipt)).thenReturn(receiptDTO);
        
        List<ReceiptDTO> result = feeCollectionService.getReceiptsByAcademicYear("2023-24");
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("2023-24", result.get(0).getAcademicYear());
    }
    
    @Test
    void getTotalFeesPaidByStudent_ShouldReturnTotalAmount() {
        when(receiptRepository.getTotalFeesPaidByStudent(1L, "2023-24"))
                .thenReturn(new BigDecimal("10000.00"));
        
        BigDecimal result = feeCollectionService.getTotalFeesPaidByStudent(1L, "2023-24");
        
        assertEquals(new BigDecimal("10000.00"), result);
    }
    
    @Test
    void getTotalFeesCollected_ShouldReturnTotalAmount() {
        when(receiptRepository.getTotalFeesCollected("2023-24"))
                .thenReturn(new BigDecimal("50000.00"));
        
        BigDecimal result = feeCollectionService.getTotalFeesCollected("2023-24");
        
        assertEquals(new BigDecimal("50000.00"), result);
    }
    
    @Test
    void searchReceipts_ShouldReturnMatchingReceipts() {
        List<Receipt> receipts = Arrays.asList(receipt);
        when(receiptRepository.searchReceipts("John")).thenReturn(receipts);
        when(receiptMapper.toDTO(receipt)).thenReturn(receiptDTO);
        
        List<ReceiptDTO> result = feeCollectionService.searchReceipts("John");
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("RCP-12345678", result.get(0).getReceiptNumber());
    }
    
    @Test
    void getReceiptsByDateRange_ShouldReturnReceiptsInRange() {
        LocalDateTime startDate = LocalDateTime.now().minusDays(7);
        LocalDateTime endDate = LocalDateTime.now();
        List<Receipt> receipts = Arrays.asList(receipt);
        
        when(receiptRepository.findByPaymentDateBetween(startDate, endDate)).thenReturn(receipts);
        when(receiptMapper.toDTO(receipt)).thenReturn(receiptDTO);
        
        List<ReceiptDTO> result = feeCollectionService.getReceiptsByDateRange(startDate, endDate);
        
        assertNotNull(result);
        assertEquals(1, result.size());
    }
    
    @Test
    void getReceiptsByFeeType_ShouldReturnReceiptsForFeeType() {
        List<Receipt> receipts = Arrays.asList(receipt);
        when(receiptRepository.findByFeeType("Tuition")).thenReturn(receipts);
        when(receiptMapper.toDTO(receipt)).thenReturn(receiptDTO);
        
        List<ReceiptDTO> result = feeCollectionService.getReceiptsByFeeType("Tuition");
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Tuition", result.get(0).getFeeType());
    }
    
    @Test
    void getReceiptsByPaymentMethod_ShouldReturnReceiptsForPaymentMethod() {
        List<Receipt> receipts = Arrays.asList(receipt);
        when(receiptRepository.findByPaymentMethod("Cash")).thenReturn(receipts);
        when(receiptMapper.toDTO(receipt)).thenReturn(receiptDTO);
        
        List<ReceiptDTO> result = feeCollectionService.getReceiptsByPaymentMethod("Cash");
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Cash", result.get(0).getPaymentMethod());
    }
    
    @Test
    void generateReceiptNumber_ShouldReturnUniqueReceiptNumber() {
        when(receiptRepository.existsByReceiptNumber(anyString())).thenReturn(false);
        
        String result = feeCollectionService.generateReceiptNumber();
        
        assertNotNull(result);
        assertTrue(result.startsWith("RCP-"));
        assertEquals(12, result.length());
    }
}

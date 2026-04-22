package com.assesment.apiAssesment.Fee.service;

import com.assesment.apiAssesment.Fee.dto.ReceiptDTO;
import com.assesment.apiAssesment.Fee.entity.Receipt;
import com.assesment.apiAssesment.Fee.repository.ReceiptRepository;
import com.assesment.apiAssesment.Student.entity.Student;
import com.assesment.apiAssesment.Student.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeeCollectionServiceTest {

    @Mock
    private ReceiptRepository receiptRepository;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private FeeCollectionService feeCollectionService;

    private Receipt receipt;
    private ReceiptDTO receiptDTO;
    private Student student;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setId(1L);
        student.setStudentName("John Doe");
        student.setStudentId("STU001");
        student.setActive(true);

        receipt = new Receipt();
        receipt.setId(1L);
        receipt.setReceiptNumber("REC20240101001");
        receipt.setStudentId(1L);
        receipt.setStudentName("John Doe");
        receipt.setFeeType("Tuition Fee");
        receipt.setAmount(new BigDecimal("500.00"));
        receipt.setPaymentDate(LocalDateTime.now());
        receipt.setPaymentMethod("Cash");
        receipt.setTransactionId("TXN001");
        receipt.setAcademicYear("2024-2025");
        receipt.setMonth("January");
        receipt.setQuarter("Q1");
        receipt.setRemarks("January Tuition Fee");
        receipt.setCreatedAt(LocalDateTime.now());
        receipt.setCreatedBy("Admin");

        receiptDTO = new ReceiptDTO();
        receiptDTO.setId(1L);
        receiptDTO.setReceiptNumber("REC20240101001");
        receiptDTO.setStudentId(1L);
        receiptDTO.setStudentName("John Doe");
        receiptDTO.setFeeType("Tuition Fee");
        receiptDTO.setAmount(new BigDecimal("500.00"));
        receiptDTO.setPaymentDate(LocalDateTime.now());
        receiptDTO.setPaymentMethod("Cash");
        receiptDTO.setTransactionId("TXN001");
        receiptDTO.setAcademicYear("2024-2025");
        receiptDTO.setMonth("January");
        receiptDTO.setQuarter("Q1");
        receiptDTO.setRemarks("January Tuition Fee");
        receiptDTO.setCreatedAt(LocalDateTime.now());
        receiptDTO.setCreatedBy("Admin");
    }

    @Test
    void collectFee_ShouldReturnReceiptDTO_WhenStudentExistsAndReceiptDoesNotExist() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(receiptRepository.existsByReceiptNumber("REC20240101001")).thenReturn(false);
        when(receiptRepository.save(any(Receipt.class))).thenReturn(receipt);

        ReceiptDTO result = feeCollectionService.collectFee(receiptDTO);

        assertNotNull(result);
        assertEquals("REC20240101001", result.getReceiptNumber());
        assertEquals("John Doe", result.getStudentName());
        assertEquals(new BigDecimal("500.00"), result.getAmount());
        verify(receiptRepository).save(any(Receipt.class));
    }

    @Test
    void collectFee_ShouldThrowException_WhenStudentDoesNotExist() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> feeCollectionService.collectFee(receiptDTO));
        verify(receiptRepository, never()).save(any(Receipt.class));
    }

    @Test
    void collectFee_ShouldThrowException_WhenReceiptExists() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(receiptRepository.existsByReceiptNumber("REC20240101001")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> feeCollectionService.collectFee(receiptDTO));
        verify(receiptRepository, never()).save(any(Receipt.class));
    }

    @Test
    void getAllReceipts_ShouldReturnListOfReceipts() {
        List<Receipt> receipts = Arrays.asList(receipt);
        when(receiptRepository.findAll()).thenReturn(receipts);

        List<ReceiptDTO> result = feeCollectionService.getAllReceipts();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("REC20240101001", result.get(0).getReceiptNumber());
    }

    @Test
    void getReceiptById_ShouldReturnReceiptDTO_WhenReceiptExists() {
        when(receiptRepository.findById(1L)).thenReturn(Optional.of(receipt));

        Optional<ReceiptDTO> result = feeCollectionService.getReceiptById(1L);

        assertTrue(result.isPresent());
        assertEquals("REC20240101001", result.get().getReceiptNumber());
    }

    @Test
    void getReceiptById_ShouldReturnEmpty_WhenReceiptDoesNotExist() {
        when(receiptRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<ReceiptDTO> result = feeCollectionService.getReceiptById(1L);

        assertFalse(result.isPresent());
    }

    @Test
    void getReceiptByReceiptNumber_ShouldReturnReceiptDTO_WhenReceiptExists() {
        when(receiptRepository.findByReceiptNumber("REC20240101001")).thenReturn(Optional.of(receipt));

        Optional<ReceiptDTO> result = feeCollectionService.getReceiptByReceiptNumber("REC20240101001");

        assertTrue(result.isPresent());
        assertEquals("REC20240101001", result.get().getReceiptNumber());
    }

    @Test
    void getReceiptsByStudentId_ShouldReturnListOfReceipts() {
        List<Receipt> receipts = Arrays.asList(receipt);
        when(receiptRepository.findByStudentIdOrderByPaymentDateDesc(1L)).thenReturn(receipts);

        List<ReceiptDTO> result = feeCollectionService.getReceiptsByStudentId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getStudentId());
    }

    @Test
    void getReceiptsByAcademicYear_ShouldReturnListOfReceipts() {
        List<Receipt> receipts = Arrays.asList(receipt);
        when(receiptRepository.findByAcademicYear("2024-2025")).thenReturn(receipts);

        List<ReceiptDTO> result = feeCollectionService.getReceiptsByAcademicYear("2024-2025");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("2024-2025", result.get(0).getAcademicYear());
    }

    @Test
    void getTotalFeesPaidByStudent_ShouldReturnTotal_WhenReceiptsExist() {
        when(receiptRepository.getTotalFeesPaidByStudentForYear(1L, "2024-2025"))
                .thenReturn(new BigDecimal("500.00"));

        BigDecimal result = feeCollectionService.getTotalFeesPaidByStudent(1L, "2024-2025");

        assertEquals(new BigDecimal("500.00"), result);
    }

    @Test
    void getTotalFeesPaidByStudent_ShouldReturnZero_WhenNoReceiptsExist() {
        when(receiptRepository.getTotalFeesPaidByStudentForYear(1L, "2024-2025"))
                .thenReturn(null);

        BigDecimal result = feeCollectionService.getTotalFeesPaidByStudent(1L, "2024-2025");

        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void getTotalFeesCollected_ShouldReturnTotal_WhenReceiptsExist() {
        when(receiptRepository.getTotalFeesCollectedForYear("2024-2025"))
                .thenReturn(new BigDecimal("1500.00"));

        BigDecimal result = feeCollectionService.getTotalFeesCollected("2024-2025");

        assertEquals(new BigDecimal("1500.00"), result);
    }

    @Test
    void getTotalFeesCollected_ShouldReturnZero_WhenNoReceiptsExist() {
        when(receiptRepository.getTotalFeesCollectedForYear("2024-2025"))
                .thenReturn(null);

        BigDecimal result = feeCollectionService.getTotalFeesCollected("2024-2025");

        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void searchReceipts_ShouldReturnListOfReceipts() {
        List<Receipt> receipts = Arrays.asList(receipt);
        when(receiptRepository.searchReceipts("John")).thenReturn(receipts);

        List<ReceiptDTO> result = feeCollectionService.searchReceipts("John");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getStudentName());
    }

    @Test
    void getReceiptsByDateRange_ShouldReturnListOfReceipts() {
        List<Receipt> receipts = Arrays.asList(receipt);
        LocalDateTime startDate = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 12, 31, 23, 59);
        when(receiptRepository.findByPaymentDateBetween(startDate, endDate)).thenReturn(receipts);

        List<ReceiptDTO> result = feeCollectionService.getReceiptsByDateRange(startDate, endDate);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getReceiptsByFeeType_ShouldReturnListOfReceipts() {
        List<Receipt> receipts = Arrays.asList(receipt);
        when(receiptRepository.findByFeeType("Tuition Fee")).thenReturn(receipts);

        List<ReceiptDTO> result = feeCollectionService.getReceiptsByFeeType("Tuition Fee");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Tuition Fee", result.get(0).getFeeType());
    }

    @Test
    void getReceiptsByPaymentMethod_ShouldReturnListOfReceipts() {
        List<Receipt> receipts = Arrays.asList(receipt);
        when(receiptRepository.findByPaymentMethod("Cash")).thenReturn(receipts);

        List<ReceiptDTO> result = feeCollectionService.getReceiptsByPaymentMethod("Cash");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Cash", result.get(0).getPaymentMethod());
    }

    @Test
    void generateReceiptNumber_ShouldReturnUniqueReceiptNumber() {
        String result = feeCollectionService.generateReceiptNumber();

        assertNotNull(result);
        assertTrue(result.startsWith("REC"));
        assertTrue(result.length() > 10);
    }
}

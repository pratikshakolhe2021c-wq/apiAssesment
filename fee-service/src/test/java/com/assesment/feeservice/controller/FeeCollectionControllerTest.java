package com.assesment.feeservice.controller;

import com.assesment.feeservice.dto.ReceiptDTO;
import com.assesment.feeservice.service.FeeCollectionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
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

@WebMvcTest(FeeCollectionController.class)
class FeeCollectionControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private FeeCollectionService feeCollectionService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private ReceiptDTO receiptDTO;
    
    @BeforeEach
    void setUp() {
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
    }
    
    @Test
    void collectFee_ShouldReturnCreated_WhenValidInput() throws Exception {
        when(feeCollectionService.collectFee(any(ReceiptDTO.class))).thenReturn(receiptDTO);
        
        mockMvc.perform(post("/api/v1/fee/collect")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(receiptDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.receiptNumber").value("RCP-12345678"))
                .andExpect(jsonPath("$.amount").value(10000.00))
                .andExpect(jsonPath("$.studentName").value("John Doe"));
    }
    
    @Test
    void getAllReceipts_ShouldReturnListOfReceipts() throws Exception {
        List<ReceiptDTO> receipts = Arrays.asList(receiptDTO);
        when(feeCollectionService.getAllReceipts()).thenReturn(receipts);
        
        mockMvc.perform(get("/api/v1/fee/receipts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].receiptNumber").value("RCP-12345678"))
                .andExpect(jsonPath("$[0].amount").value(10000.00));
    }
    
    @Test
    void getReceiptById_ShouldReturnReceipt_WhenReceiptExists() throws Exception {
        when(feeCollectionService.getReceiptById(1L)).thenReturn(Optional.of(receiptDTO));
        
        mockMvc.perform(get("/api/v1/fee/receipts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.receiptNumber").value("RCP-12345678"))
                .andExpect(jsonPath("$.amount").value(10000.00));
    }
    
    @Test
    void getReceiptById_ShouldReturnNotFound_WhenReceiptNotExists() throws Exception {
        when(feeCollectionService.getReceiptById(1L)).thenReturn(Optional.empty());
        
        mockMvc.perform(get("/api/v1/fee/receipts/1"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void getReceiptByReceiptNumber_ShouldReturnReceipt_WhenReceiptExists() throws Exception {
        when(feeCollectionService.getReceiptByReceiptNumber("RCP-12345678")).thenReturn(Optional.of(receiptDTO));
        
        mockMvc.perform(get("/api/v1/fee/receipts/by-number/RCP-12345678"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.receiptNumber").value("RCP-12345678"))
                .andExpect(jsonPath("$.amount").value(10000.00));
    }
    
    @Test
    void getReceiptByReceiptNumber_ShouldReturnNotFound_WhenReceiptNotExists() throws Exception {
        when(feeCollectionService.getReceiptByReceiptNumber("RCP-12345678")).thenReturn(Optional.empty());
        
        mockMvc.perform(get("/api/v1/fee/receipts/by-number/RCP-12345678"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void getReceiptsByStudentId_ShouldReturnReceiptsForStudent() throws Exception {
        List<ReceiptDTO> receipts = Arrays.asList(receiptDTO);
        when(feeCollectionService.getReceiptsByStudentId(1L)).thenReturn(receipts);
        
        mockMvc.perform(get("/api/v1/fee/receipts/student/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].studentId").value(1))
                .andExpect(jsonPath("$[0].receiptNumber").value("RCP-12345678"));
    }
    
    @Test
    void getReceiptsByAcademicYear_ShouldReturnReceiptsForYear() throws Exception {
        List<ReceiptDTO> receipts = Arrays.asList(receiptDTO);
        when(feeCollectionService.getReceiptsByAcademicYear("2023-24")).thenReturn(receipts);
        
        mockMvc.perform(get("/api/v1/fee/receipts/academic-year/2023-24"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].academicYear").value("2023-24"))
                .andExpect(jsonPath("$[0].receiptNumber").value("RCP-12345678"));
    }
    
    @Test
    void getTotalFeesPaidByStudent_ShouldReturnTotalAmount() throws Exception {
        when(feeCollectionService.getTotalFeesPaidByStudent(1L, "2023-24"))
                .thenReturn(new BigDecimal("10000.00"));
        
        mockMvc.perform(get("/api/v1/fee/total/student/1/academic-year/2023-24"))
                .andExpect(status().isOk())
                .andExpect(content().string("10000.00"));
    }
    
    @Test
    void getTotalFeesCollected_ShouldReturnTotalAmount() throws Exception {
        when(feeCollectionService.getTotalFeesCollected("2023-24"))
                .thenReturn(new BigDecimal("50000.00"));
        
        mockMvc.perform(get("/api/v1/fee/total/academic-year/2023-24"))
                .andExpect(status().isOk())
                .andExpect(content().string("50000.00"));
    }
    
    @Test
    void searchReceipts_ShouldReturnMatchingReceipts() throws Exception {
        List<ReceiptDTO> receipts = Arrays.asList(receiptDTO);
        when(feeCollectionService.searchReceipts("John")).thenReturn(receipts);
        
        mockMvc.perform(get("/api/v1/fee/receipts/search")
                .param("search", "John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].receiptNumber").value("RCP-12345678"))
                .andExpect(jsonPath("$[0].studentName").value("John Doe"));
    }
    
    @Test
    void getReceiptsByDateRange_ShouldReturnReceiptsInRange() throws Exception {
        LocalDateTime startDate = LocalDateTime.now().minusDays(7);
        LocalDateTime endDate = LocalDateTime.now();
        List<ReceiptDTO> receipts = Arrays.asList(receiptDTO);
        
        when(feeCollectionService.getReceiptsByDateRange(startDate, endDate)).thenReturn(receipts);
        
        mockMvc.perform(get("/api/v1/fee/receipts/date-range")
                .param("startDate", startDate.toString())
                .param("endDate", endDate.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].receiptNumber").value("RCP-12345678"));
    }
    
    @Test
    void getReceiptsByFeeType_ShouldReturnReceiptsForFeeType() throws Exception {
        List<ReceiptDTO> receipts = Arrays.asList(receiptDTO);
        when(feeCollectionService.getReceiptsByFeeType("Tuition")).thenReturn(receipts);
        
        mockMvc.perform(get("/api/v1/fee/receipts/fee-type/Tuition"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].feeType").value("Tuition"))
                .andExpect(jsonPath("$[0].receiptNumber").value("RCP-12345678"));
    }
    
    @Test
    void getReceiptsByPaymentMethod_ShouldReturnReceiptsForPaymentMethod() throws Exception {
        List<ReceiptDTO> receipts = Arrays.asList(receiptDTO);
        when(feeCollectionService.getReceiptsByPaymentMethod("Cash")).thenReturn(receipts);
        
        mockMvc.perform(get("/api/v1/fee/receipts/payment-method/Cash"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].paymentMethod").value("Cash"))
                .andExpect(jsonPath("$[0].receiptNumber").value("RCP-12345678"));
    }
    
    @Test
    void generateReceiptNumber_ShouldReturnUniqueReceiptNumber() throws Exception {
        when(feeCollectionService.generateReceiptNumber()).thenReturn("RCP-12345678");
        
        mockMvc.perform(get("/api/v1/fee/receipt-number/generate"))
                .andExpect(status().isOk())
                .andExpect(content().string("RCP-12345678"));
    }
}

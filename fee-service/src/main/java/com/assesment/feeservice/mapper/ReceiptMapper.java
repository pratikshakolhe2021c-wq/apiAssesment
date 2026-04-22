package com.assesment.feeservice.mapper;

import com.assesment.feeservice.dto.ReceiptDTO;
import com.assesment.feeservice.entity.Receipt;
import org.springframework.stereotype.Component;

@Component
public class ReceiptMapper {
    
    public ReceiptDTO toDTO(Receipt receipt) {
        if (receipt == null) {
            return null;
        }
        
        ReceiptDTO dto = new ReceiptDTO();
        dto.setId(receipt.getId());
        dto.setReceiptNumber(receipt.getReceiptNumber());
        dto.setStudentId(receipt.getStudentId());
        dto.setStudentName(receipt.getStudentName());
        dto.setAcademicYear(receipt.getAcademicYear());
        dto.setFeeType(receipt.getFeeType());
        dto.setAmount(receipt.getAmount());
        dto.setPaymentMethod(receipt.getPaymentMethod());
        dto.setTransactionId(receipt.getTransactionId());
        dto.setPaymentDate(receipt.getPaymentDate());
        dto.setNotes(receipt.getNotes());
        dto.setCreatedAt(receipt.getCreatedAt());
        dto.setUpdatedAt(receipt.getUpdatedAt());
        
        return dto;
    }
    
    public Receipt toEntity(ReceiptDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Receipt receipt = new Receipt();
        receipt.setReceiptNumber(dto.getReceiptNumber());
        receipt.setStudentId(dto.getStudentId());
        receipt.setStudentName(dto.getStudentName());
        receipt.setAcademicYear(dto.getAcademicYear());
        receipt.setFeeType(dto.getFeeType());
        receipt.setAmount(dto.getAmount());
        receipt.setPaymentMethod(dto.getPaymentMethod());
        receipt.setTransactionId(dto.getTransactionId());
        receipt.setPaymentDate(dto.getPaymentDate());
        receipt.setNotes(dto.getNotes());
        
        return receipt;
    }
    
    public void updateEntityFromDTO(ReceiptDTO dto, Receipt receipt) {
        if (dto == null || receipt == null) {
            return;
        }
        
        receipt.setReceiptNumber(dto.getReceiptNumber());
        receipt.setStudentId(dto.getStudentId());
        receipt.setStudentName(dto.getStudentName());
        receipt.setAcademicYear(dto.getAcademicYear());
        receipt.setFeeType(dto.getFeeType());
        receipt.setAmount(dto.getAmount());
        receipt.setPaymentMethod(dto.getPaymentMethod());
        receipt.setTransactionId(dto.getTransactionId());
        receipt.setPaymentDate(dto.getPaymentDate());
        receipt.setNotes(dto.getNotes());
    }
}

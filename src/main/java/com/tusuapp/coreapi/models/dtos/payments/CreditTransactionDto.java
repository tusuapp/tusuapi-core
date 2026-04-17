package com.tusuapp.coreapi.models.dtos.payments;

import com.tusuapp.coreapi.models.CreditTransaction;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreditTransactionDto {

    private Long id;
    private Double amount;
    private String type;        // "CREDIT" or "DEBIT"
    private String description;
    private LocalDateTime createdAt;

    public static CreditTransactionDto from(CreditTransaction tx) {
        CreditTransactionDto dto = new CreditTransactionDto();
        dto.setId(tx.getId());
        dto.setAmount(tx.getAmount());
        dto.setType(tx.getType());
        dto.setDescription(tx.getDescription());
        dto.setCreatedAt(tx.getCreatedAt());
        return dto;
    }
}

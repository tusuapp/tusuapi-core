package com.tusuapp.coreapi.models.dtos.payments;

import com.tusuapp.coreapi.models.PayoutRequest;
import com.tusuapp.coreapi.models.enums.PayoutStatus;
import lombok.Data;

import java.time.Instant;

@Data
public class PayoutResponseDto {
    private Long id;
    private Double amount;
    private String currency;
    private PayoutStatus status;
    private Instant createdAt;
    private Instant updatedAt;

    public static PayoutResponseDto fromEntity(PayoutRequest request) {
        PayoutResponseDto dto = new PayoutResponseDto();
        dto.setId(request.getId());
        dto.setAmount(request.getAmount());
        dto.setCurrency(request.getCurrency());
        dto.setStatus(request.getStatus());
        dto.setCreatedAt(request.getCreatedAt());
        dto.setUpdatedAt(request.getUpdatedAt());
        return dto;
    }
}

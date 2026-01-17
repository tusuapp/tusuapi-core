package com.tusuapp.coreapi.models.dtos.payments;

import com.tusuapp.coreapi.models.enums.PayoutStatus;
import lombok.Data;

@Data
public class PayoutProcessDto {
    private Long requestId;
    private PayoutStatus status; // Expected PAID or REJECTED
    private String adminNotes;
}
